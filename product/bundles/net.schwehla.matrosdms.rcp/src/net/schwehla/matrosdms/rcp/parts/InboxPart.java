package net.schwehla.matrosdms.rcp.parts;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.lucene.ILuceneService;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.parts.helper.DesktopHelper;
import net.schwehla.matrosdms.rcp.swt.filteredcomposite.FilteredTree;
import net.schwehla.matrosdms.rcp.swt.filteredcomposite.PatternFilter;
import net.schwehla.matrosdms.rcp.swt.labelprovider.FileModifiedLabelProvider;
import net.schwehla.matrosdms.rcp.swt.labelprovider.FileSizeLabelProvider;
import net.schwehla.matrosdms.resourcepool.IMatrosResource;

/**
 * Inbox-View
 * @author Martin
 *
 */

// XXX 
// TODO: https://stackoverflow.com/questions/31706058/get-large-directory-content-faster-java-io-file-alternatives

public class InboxPart {
	
	List <File> rootFiles = new ArrayList<>();

	
	@Inject
	private IEventBroker eventBroker;
	
	
	@Inject  StatusReporter statusReporter;
	@Inject Logger logger;
	
	
	@Inject
	@Translation
	MatrosMessage messages;
	
	// XXX refactor that bootstrap in a background-process
	// This fastens first open of an inbox
    @Inject
    ILuceneService indexService;
    

	@Inject IMatrosServiceService matrosService;
	
	@Inject UISynchronize sync;
	
    
	@Inject 
	DesktopHelper desktopHelper;
    
	 @Inject
	 IMatrosResource poolOfResources;    
	
		@Inject
		@Optional
		@Active
		Shell shell;

		@Inject
		@Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS) 
		IEclipsePreferences preferences ;

		@Inject INotificationService notificationService;
		
	// Auto-Track changes
	// http://www.vogella.com/tutorials/EclipsePreferences/article.html
//
	@Inject
	@Optional
	public void trackInboxSettings(@Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS
	        , value = MyGlobalConstants.Preferences.INBOX_PATH)
	        String inboxPathTmp) {
					
			if (viewer != null && !viewer.getTree().isDisposed() && inboxPathTmp != null) {
	
				addInboxRoots();
			}
			
	}
		
	
	@Inject
	@Optional
	/**
	 * OK, double-clicked on one Element, eg. Car
	 * @param modelType
	 */
	private void subscribeTopicTOPIC_TAGGRAPH_DOUBLEKLICK_ADD_ELEMENT(@UIEventTopic(MyEventConstants.TOPIC_REFRESH_INBOX_FILE_MOVED) File file) {
		
		// todo
		viewer.refresh(true);
	}
	

	private TreeViewer viewer;

	FilteredTree filteredTree;
	
	private static final DateFormat dateFormat = DateFormat.getDateInstance();

	/**
	 * @wbp.parser.entryPoint
	 */
	@PostConstruct
	public void createControl(Composite parent) {
		
		
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new FillLayout());
		
		// XXX sollte im Bootstrap-code sein 
		indexService.bootstrap();
		
		
		PatternFilter patternFilter = new PatternFilter();
		patternFilter.setIncludeLeadingWildcard(true);

		filteredTree = new FilteredTree(c, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, patternFilter);

		viewer = filteredTree.getViewer();
		viewer.getTree().setHeaderVisible(true);

//
		
		TreeViewerColumn mainColumn = new TreeViewerColumn(viewer, SWT.NONE);
		mainColumn.getColumn().setText(messages.InboxPart_name);
		mainColumn.getColumn().setWidth(300);
		mainColumn.setLabelProvider(
				new DelegatingStyledCellLabelProvider(new ViewLabelProvider(createImageDescriptor())));

		TreeViewerColumn modifiedColumn = new TreeViewerColumn(viewer, SWT.NONE);
		modifiedColumn.getColumn().setText(messages.InboxPart_lastModified);
		modifiedColumn.getColumn().setWidth(100);
		modifiedColumn.getColumn().setAlignment(SWT.RIGHT);
		modifiedColumn
				.setLabelProvider(new DelegatingStyledCellLabelProvider(new FileModifiedLabelProvider(dateFormat)));

		TreeViewerColumn fileSizeColumn = new TreeViewerColumn(viewer, SWT.NONE);
		fileSizeColumn.getColumn().setText(messages.InboxPart_size);
		fileSizeColumn.getColumn().setWidth(100);
		fileSizeColumn.getColumn().setAlignment(SWT.RIGHT);
		fileSizeColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new FileSizeLabelProvider()));
		
		//
		
		viewer.setContentProvider(new ViewContentProvider());

	//
	

		
		// Drag and Drop
		DragSource source = new DragSource(viewer.getControl(),  DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);
		

//
//			source.setDragSourceEffect(new DragSourceEffect(viewer.getControl()) {
//  				@Override
//  				public void dragStart(DragSourceEvent event) {
//  					super.dragStart(event);
//  					event.image = shell.getDisplay().getSystemImage(SWT.ICON_WARNING);
//  					event.
//  				
//  				}
//  			});
//  			
//			
	    
		
	  		// Step 1: Get JFace's LocalSelectionTransfer instance
	  		final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
	  		source.setTransfer(new Transfer[] { transfer });
	  		
	  		
	  		

	  		source.addDragListener(new DragSourceAdapter() {
	  			
	 

			@Override
			public void dragStart(DragSourceEvent event) {
				
				eventBroker.send(MyEventConstants.TOPIC__DRAG_INBOX_START,new HashMap());
				
				super.dragStart(event);
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				
				// post ist asynchron
				eventBroker.send(MyEventConstants.TOPIC__DRAG_INBOX_FINISHED,new HashMap());

				super.dragFinished(event);
			}

			public void dragSetData(DragSourceEvent event) {

				Tree table = (Tree) source.getControl();

				TreeItem[] selection = table.getSelection();

				if (selection != null) {

					if (selection.length > 4) {
						
						// create a dialog with ok and cancel buttons and a question icon
						MessageBox dialog =
						        new MessageBox(shell, SWT.ICON_WARNING | SWT.OK );
						dialog.setText(messages.InboxPart_toMuchElements);
						dialog.setMessage(messages.InboxPart_justFourElements);

						// open dialog and await originalstore selection
						int val = dialog.open();
						
						if (val == Window.OK) {
							return;
						}
					}
					
					
					transfer.setSelection(new StructuredSelection(selection));
  			
				}
	
			}

		});
	  		
    //
	
	  		viewer.getTree().addMouseListener(new MouseAdapter() {

				@Override

				public void mouseDoubleClick(MouseEvent e) {

		
		        	try {
						File doc = (File) viewer.getTree().getSelection()[0].getData();
						
						// Clone else there will be file-lock
	                	String clonePath = desktopHelper.getInboxNonBlockingLink(doc);
	                	desktopHelper.openUrl(clonePath);
	                	
	                	
	            	}catch(Exception ex) {
	            		logger.error(ex);
	            	}

				}

			});
	  		
		viewer.setInput(rootFiles);
		
		addMenu(viewer);
		
		addInboxRoots();
	}
	private void addMenu(TreeViewer viewer) {


		
		// Create the popup menu
		  MenuManager menuMgr = new MenuManager();
		  Menu menu = menuMgr.createContextMenu(viewer.getControl());
		  menuMgr.addMenuListener(new IMenuListener() {
		    @Override
		    public void menuAboutToShow(IMenuManager manager) {
		      if(viewer.getSelection().isEmpty()) {
		        return;
		      }

		      

		      
	          manager.add(new Action() {
	        	  
	        	  public String getText() { return "refresh"; };
	        	  
		          @Override
		          public void run() {
		        	  
		        	  viewer.refresh(true);
		          }
		        		
		    
	          });
	          
	          
	          
	          manager.add(new Separator("-----"));
	          
	          
		      
		      
		      if(viewer.getSelection() instanceof IStructuredSelection) {
		        IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		        
		    
		        File file = (File)selection.getFirstElement();

		      
		          manager.add(new Action() {
		        	  
		        	  public String getText() { return "Move to not processed"; };
		        	  
			          @Override
			          public void run() {
			        	  
			      		
	 						NotificationNote note = new NotificationNote();
	 			
	 				

			        	  
			        	try {
							desktopHelper.moveFromInbox(file);
							
							viewer.remove(selection.getFirstElement());
							viewer.refresh(true);
							
							note.setHeading("Dokument verschoben");
							notificationService.openPopup(note);
	 						
							
						} catch (MatrosServiceException e) {
							  MessageDialog.openError(Display.getDefault().getActiveShell(),"Move" , e.getMessage());
							  
								viewer.refresh(true);
								
						}
			        		
						
			          }
			        		
			    
		          });
		          
		          
		          
		          manager.add(new Separator("-----"));
		          
		          manager.add(new Action() {
		        	  
		        	  public String getText() { return "Check for Duplicate"; };
		        	  
			          @Override
			          public void run() {
			        	  
			        	  
			        	   if(! (viewer.getSelection() instanceof IStructuredSelection)
			        			   && (IStructuredSelection)viewer.getSelection() != null ) {
							return;
						         
					      }
			        	   
			          
			        	  final File file = (File)selection.getFirstElement() ;
		        	  
				      if (file == null) {
				    	  return;
				      }
		        		
		      			
				      
						try {
							
						    sync.asyncExec(new Runnable() {
						    	
						    	 @Override
							      public void run() {

						    		 MatrosMetadata mmd;
									try {
										
										 List <InfoItem> doubles = new ArrayList <>();
							    	
										mmd = indexService.parseMetadata(file);
										doubles = matrosService.checkForDuplicate(mmd);
										if (doubles != null && ! doubles.isEmpty()) {
											
										  	
										     
											MessageBox dialog =
											        new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
											dialog.setText("My info");
											dialog.setMessage("File gibt es schon: " + doubles.get(0).getName() + "-> " + doubles.get(0).getContext().getName() );

											dialog.open();
											
									
								      } else {
								    	  
								    	  
								  		NotificationNote note = new NotificationNote();
								  		note.setHeading("Dokument kann gespeichert werden"  );
								  		note.setBody( mmd != null ? mmd.getSha256() : "kein Hash verfügbar" );
										notificationService.openPopup(note);
											
								    	  
								      }
										
										
									} catch (Exception e) {
										
								  		NotificationNote note = new NotificationNote();
								  		note.setHeading("Exception occured"  );
								  		note.setBody( e.getMessage() );
										notificationService.openPopup(note);
										
										throw new RuntimeException(e);
									}
										
										
								
								    };
								     
						    	 });
							
						} catch (Exception e) {
							  MessageDialog.openError(Display.getDefault().getActiveShell(),"Doublecheck" , e.getMessage());
								
						}
						
			
					    	
						
		        	  
		        	  
		          }
		        		
		    
			          
		        		  
		          	  });
		          
		          
		          
		        
		   
		      }
		    }
		  });

		  menuMgr.setRemoveAllWhenShown(true);
		  viewer.getControl().setMenu(menu);
		
	}


	
	private void addInboxRoots() {
		
		rootFiles.clear();
	

		
		String inbox =  preferences.get(MyGlobalConstants.Preferences.INBOX_PATH, "" ); //$NON-NLS-1$
		String[] inboxArray = inbox.split(MyGlobalConstants.Preferences.DELIMITER);
		
		try {
			

			
			for (String tmp : inboxArray) {
				
				File f = new File(tmp);
				if (f.exists() && f.isDirectory()) {
					rootFiles.add(f);
				} else {
					logger.error(messages.InboxPart_noInboxSpecified + tmp);
				}
				
			}
			
		} catch (Exception e) {
			logger.error(e,"inbox " + inbox); //$NON-NLS-1$
		}
		

		viewer.refresh(true);
	}

	// XXX global resource monitor
	private ImageDescriptor createImageDescriptor() {
		Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
		URL url = org.eclipse.core.runtime.FileLocator.find(bundle, new org.eclipse.core.runtime.Path(messages.InboxPart_icon), null);
		return ImageDescriptor.createFromURL(url);
	}

	class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			
			if (inputElement instanceof ArrayList) {
				return ((ArrayList) inputElement).toArray();
			}
			
			return new File[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			File file = (File) parentElement;
			return file.listFiles();
		}

		@Override
		public Object getParent(Object element) {
			File file = (File) element;
			return file.getParentFile();
		}

		@Override
		public boolean hasChildren(Object element) {
			File file = (File) element;
			if (file.isDirectory()) {
				return true;
			}
			return false;
		}

	}

	class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

		private ImageDescriptor directoryImage;
		private ResourceManager resourceManager;

		public ViewLabelProvider(ImageDescriptor directoryImage) {
			this.directoryImage = directoryImage;
		}

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof File) {
				File file = (File) element;
				StyledString styledString = new StyledString(getFileName(file));
				String[] files = file.list();
				if (files != null) {
					styledString.append(" (" + files.length + ") ", StyledString.COUNTER_STYLER); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return styledString;
			}
			return null;	
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof File) {
				if (((File) element).isDirectory()) {
		//			return getResourceManager().createImage(directoryImage);
				}
			}

			return super.getImage(element);
		}

		@Override
		public void dispose() {
			// garbage collection system resources
			if (resourceManager != null) {
				resourceManager.dispose();
				resourceManager = null;
			}
		}

		protected ResourceManager getResourceManager() {
			if (resourceManager == null) {
				resourceManager = new LocalResourceManager(JFaceResources.getResources());
			}
			return resourceManager;
		}

		private String getFileName(File file) {
			String name = file.getName();
			return name.isEmpty() ? file.getPath() : name;
		}
	}



	@Focus
	public void setFocus() {
	//	viewer.getControl().setFocus();
	}
	
	@PostConstruct
	public void addHooks() {

	}
}
