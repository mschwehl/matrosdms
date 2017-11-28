package net.schwehla.matrosdms.rcp.parts;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.Selector;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.InfoItemList;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;
import net.schwehla.matrosdms.rcp.dnd.MyDragListener;
import net.schwehla.matrosdms.rcp.parts.filter.ItemFilter;
import net.schwehla.matrosdms.rcp.parts.helper.AutoResizeTableLayout;
import net.schwehla.matrosdms.rcp.parts.helper.DesktopHelper;
import net.schwehla.matrosdms.rcp.parts.helper.ItemPartElementWrapper;
import net.schwehla.matrosdms.rcp.parts.helper.ItemPartElementWrapper.Type;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkLabelProvider;
import net.schwehla.matrosdms.rcp.swt.labelprovider.LinkOpener;
import net.schwehla.matrosdms.rcp.swt.labelprovider.MultilineLabelProvider;

/**
 * 
 * this part shows all infoitems from one part
 *
 * 
 * 
 * @author martin
 * 
 */

public class ContextInfoItemListPart {

	public ContextInfoItemListPart() {

	}

	InfoContext _infoContext;

	private Text swtTxtSearchfield;

	private TableViewer swtItemlistTableViewer;

	@Inject ItemFilter filter;

	@Inject
	IMatrosServiceService service;

	@Inject
	UISynchronize sync;
	
	@Inject 
	DesktopHelper desktopHelper;
	
    DateFormat df;
	@Inject
	private IEventBroker eventBroker;
	
	
	@Inject Logger logger;

	@Inject

	@Translation

	MatrosMessage messages;


	@Inject 
	EPartService partService;

	@Inject 
	EModelService modelService;
	
	@Inject @Active Shell shell;
	
    @Inject
    EMenuService menuservice;
    
	private Color defaultListColor;
	private Color defaultAttributeColor;
	
	@Inject MApplication application;
	
	@Inject
	 @Named(TranslationService.LOCALE) Locale locale;
	
    
    @Inject ESelectionService selectionService;
    
    MPart currentPart;
    
	Job job;

	@Inject
	@Optional
	private void subscribeTopic_TOPIC__DRAG_INBOX_START(@UIEventTopic(MyEventConstants.TOPIC__DRAG_INBOX_START) Map data) {

		defaultListColor = swtItemlistTableViewer.getTable().getBackground();

		// XXX Das IF macht keinen Sinn
		
		if (swtItemlistTableViewer != null) {
//			// create resources
			
			Display display = Display.getCurrent();
			swtItemlistTableViewer.getTable().setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			swtItemlistTableViewer.getTable().redraw();
		}

	}

	@Inject
	@Optional
	private void subscribeTopic_TOPIC__DRAG_INBOX_FINISHED(@UIEventTopic(MyEventConstants.TOPIC__DRAG_INBOX_FINISHED) Map data) {

		
		// Alte Farbe wieder herstellen
		if (swtItemlistTableViewer.getTable() != null) {
			swtItemlistTableViewer.getTable().setBackground(defaultListColor);
			swtItemlistTableViewer.getTable().redraw();
		}

	}
	
	
	@Inject
	@Optional
	private void subscribeTOPIC_TOPIC__REFRESH_ITEM_STAR(
			@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ITEM_STAR)  InfoItem infoItem) {
		

		if (swtItemlistTableViewer != null && !swtItemlistTableViewer.getTable().isDisposed()) {

			if (_infoContext != null) {

				loadAsyncInfoDetailList(infoItem.getContext());

			} else {

				((java.util.List)swtItemlistTableViewer.getInput()).clear();;


				swtItemlistTableViewer.refresh();

				// viewer.getTree().setFocus();

			}

		}

		
	}

	
	
	@Inject

	@Optional

	private void subscribeTOPIC_CONTEXT_SELECTED_SELECT(
			@UIEventTopic(MyEventConstants.TOPIC_CONTEXT_SELECTED_SELECT) InfoContext infoContext) {

		this._infoContext = infoContext;
		
		// todo: Cancel job

		if (swtItemlistTableViewer != null && !swtItemlistTableViewer.getTable().isDisposed()) {

			if (_infoContext != null) {

				loadAsyncInfoDetailList(infoContext);

			} else {

				((java.util.List)swtItemlistTableViewer.getInput()).clear();;

				swtItemlistTableViewer.refresh();

				// viewer.getTree().setFocus();

			}

		}

	}

	@Inject
	@Optional
	private void subscribeTOPIC_CONTEXT_SELECTED_REMOVE(
			@UIEventTopic(MyEventConstants.TOPIC_CONTEXT_SELECTED_REMOVE) InfoContext infoContext) {

		this._infoContext = infoContext;

		if (swtItemlistTableViewer != null && !swtItemlistTableViewer.getTable().isDisposed()) {

			if (_infoContext != null) {

				loadAsyncInfoDetailList(infoContext);

			} else {

				((java.util.List)swtItemlistTableViewer.getInput()).clear();

				swtItemlistTableViewer.refresh();

				// viewer.getTree().setFocus();

			}

		}

	}

	/**
	 * 
	 * Loading the itemlist for a given Tag-Constellation using
	 * background-actions
	 *
	 * 
	 * 
	 * @param infoContext
	 * 
	 *            the specific Tag-Constellation
	 * 
	 */
	

	private void loadAsyncInfoDetailList(InfoContext infoContext) {
		
		Objects.requireNonNull(infoContext);

		
		// cancel if currently running
		// XXX check if this works !? internet says no
		// http://stackoverflow.com/questions/16986520/how-do-i-stop-a-thread-in-an-eclipse-job
		if (job != null) {
			job.cancel();
		}
		

		// http://www.vogella.com/tutorials/EclipseJobs/article.html#handle-job-cancellation
		
		 job = Job.create(messages.global_job_startjob, monitor -> {
	        SubMonitor subMonitor = SubMonitor.convert(monitor, 1);
	        
	        subMonitor.split(1);
	        
			try {

					InfoItemList storableItemList = service.loadInfoItemList(infoContext, false);
	    		
					sync.asyncExec(new Runnable() {

						@Override

						public void run() {

							// Speedup
							swtItemlistTableViewer.getControl().setRedraw(false);
							// update
							swtItemlistTableViewer.setInput(storableItemList);
							swtItemlistTableViewer.refresh(true);
							
							swtItemlistTableViewer.getControl().setRedraw(true);
							

								// viewer.getTree().setFocus();



							}
						});


	    	
			} catch (MatrosServiceException mse) {

				logger.error(mse);

			}

		

	        // no return value needed when using an ICoreRunnable (since Neon)
	});
	job.schedule();
	
	
	}
	


	@PostConstruct
	public void createPart(Composite parent) {

		df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		 
		parent.setLayout(new GridLayout(1, false));

		swtTxtSearchfield = new Text(parent, SWT.BORDER);
		swtTxtSearchfield.setMessage(messages.itemlistPart_searchfield_helptext);
		swtTxtSearchfield.addModifyListener(new ModifyListener() {

			@Override

			public void modifyText(ModifyEvent e) {

				filter.setSearchString(swtTxtSearchfield.getText());

				swtItemlistTableViewer.refresh(false);

				if (swtItemlistTableViewer.getTable().getItemCount() == 1) {

					swtItemlistTableViewer.setSelection(
							new StructuredSelection(swtItemlistTableViewer.getTable().getItems()[0].getData()));

				}

			}

		});

		swtTxtSearchfield.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// SWT.VIRTUAL makes problems with filtering

		swtItemlistTableViewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

		swtItemlistTableViewer.getTable().addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e)
		    {
		        if (e.stateMask == SWT.CTRL && e.keyCode == 'a') {
		        	swtItemlistTableViewer.getTable().selectAll();
		            e.doit = false;
		        }
		    }
		});



		// Speedup a little bit

		swtItemlistTableViewer.setUseHashlookup(true);

		swtItemlistTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		// initally the List is empty for avoiding npe
		swtItemlistTableViewer.setInput(new ArrayList());

		AutoResizeTableLayout layout = new AutoResizeTableLayout(swtItemlistTableViewer.getTable());

		createColumns(layout);

		// Stetch the Table itself to maximum

		GridData gridData = new GridData();

		gridData.grabExcessHorizontalSpace = true;

		gridData.grabExcessVerticalSpace = true;

		gridData.horizontalAlignment = GridData.FILL;

		gridData.verticalAlignment = GridData.FILL;

		swtItemlistTableViewer.getTable().setLayoutData(gridData);

		swtItemlistTableViewer.addFilter(filter);

		swtItemlistTableViewer.getTable().addMouseListener(new MouseAdapter() {

			@Override

			public void mouseDoubleClick(MouseEvent e) {

				int[] index =  swtItemlistTableViewer.getTable().getSelectionIndices();
				
				if (index.length == 0) {
					return;
				}

				
				InfoItem docWithoutDetails = (InfoItem) swtItemlistTableViewer.getTable().getSelection()[0].getData();
				
				InfoItem detailItem;
				try {
					detailItem = service.loadInfoItemByIdentifier(docWithoutDetails.getIdentifier());
					
					
					ItemPartElementWrapper wrapper = new ItemPartElementWrapper(Type.EXISTING);
					wrapper.setInfoItem(detailItem);
					

					 java.util.Optional <MPart> existing = isExisting(detailItem);
					
					if (existing.isPresent()) {
						
						MPart part = existing.get();
						
						partService.showPart(part, PartState.ACTIVATE);
						
						return;
					} 
					
						
					MPart itemPart = partService.createPart("net.schwehla.matrosdms.rcp.partdescriptor.ItemPart");
					// XXX
					itemPart.setIconURI("pdf");
					
					itemPart.setObject(wrapper);
					
					itemPart.setLabel(detailItem.getName());
					
					// XXX not allow double open

					partService.showPart(itemPart, PartState.ACTIVATE);
				
					
				} catch (MatrosServiceException e1) {
					logger.error(e1);
				}

		

			}

		});
		
		

	       // viewer is a JFace Viewer
		swtItemlistTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      	  
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                    IStructuredSelection selection = swtItemlistTableViewer.getStructuredSelection();
                    selectionService.setSelection(selection.getFirstElement());
            }
                
        });
        

		
		Transfer[] transferTypes = { DomainClassTransfer.getTransfer(InfoItem.class)  };
		
		swtItemlistTableViewer.addDragSupport( DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY,transferTypes
				, new MyDragListener(swtItemlistTableViewer,InfoItem.class)   {
			
			@Override
			public void dragStart(DragSourceEvent event) {

				 currentPart = partService.getActivePart();
	   			  
	   			  MPart items = partService.findPart("net.schwehla.matrosdms.rcp.part.itemstack");
	   			  
	   			  if (items != null) {
	   					partService.showPart(items, PartState.ACTIVATE);
	   			  }

				super.dragStart(event);
			}
			
			
		});
		
		
		


		buildDropTarget(swtItemlistTableViewer.getTable());
				
      	menuservice.registerContextMenu(swtItemlistTableViewer.getTable(),
    			"net.schwehla.matrosdms.rcp.popupmenu.infoitempopup");
      	
				
	}

	private void buildDropTarget(Control component) {
		// Drag 'n' Drop
		
		DropTarget dropTarget = new DropTarget(component, DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);
		
		
	    final FileTransfer fileTransfer = FileTransfer.getInstance();
	       
        // https://georgiangrec.wordpress.com/2013/08/11/swt-composite-drag-and-drop/
		final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

		dropTarget.setTransfer(new Transfer[] { transfer,fileTransfer });
		
		
		dropTarget.addDropListener(new DropTargetAdapter() {

			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub
				super.dropAccept(event);
			}
			
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
			}

			public void drop(DropTargetEvent event) {
				
				
				if (_infoContext == null) {
					
					// create a dialog with ok and cancel buttons and a question icon
					MessageBox dialog =
					        new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK );
					dialog.setText("My info");
					dialog.setMessage("Select context");

					// open dialog and await originalstore selection
					dialog.open();
					
					
				} else {
					
					
					// is it from the desktop ? 
					if (fileTransfer.isSupportedType(event.currentDataType)) {
	                    String[] files = (String[]) event.data;
	                    Arrays.stream(files).forEach( e -> tryOpen(new File(e)));
	                    return;
	                  
	                } 
	               
					
					final StructuredSelection droppedObj =  (StructuredSelection) event.data;
					
					if (droppedObj != null) {
						
						try {
							
							
							Iterator <TreeItem> it = droppedObj.iterator();
							
							while (it.hasNext()) {
								
								TreeItem ti = (TreeItem) it.next();
								File file = (File) ti.getData();
								
								tryOpen(file);
								
							}
							
							
						} catch (Exception e) {
							logger.error(e);
						}
		
					}
				
				}
				

			}


		});
	}
	
	
	

	protected void tryOpen(File file) {
		
		ItemPartElementWrapper wrapper = new ItemPartElementWrapper(Type.NEW);
		wrapper.setInboxFile(file );
		
		// No directory allowed
		if (wrapper.getInboxFile().exists() && wrapper.getInboxFile().isDirectory()) {
			
			// create a dialog with ok and cancel buttons and a question icon
			MessageBox dialog =
			        new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK );
			dialog.setText("Directory");
			dialog.setMessage("Select a file and not a folder");

			// open dialog and await originalstore selection
			dialog.open();
			
			return;
			
		}
			
		
		
		MPart itemPart = partService.createPart("net.schwehla.matrosdms.rcp.partdescriptor.ItemPart");
		String fileName = wrapper.getInboxFile().getName();
		
		if (fileName.contains(".")) {
			fileName = fileName.split("\\.")[0];
		}
		
		InfoItem infoItem = new InfoItem( _infoContext , Identifier.createNEW(),fileName );
		wrapper.setInfoItem(infoItem);
		
		itemPart.setObject(wrapper);
		
		itemPart.setLabel(messages.contextlistpart_tab_newpart);

		 java.util.Optional <MPart> existing = isExisting(infoItem);
		
		if (existing.isPresent()) {
			
			MPart part = existing.get();
			
			partService.showPart(part, PartState.ACTIVATE);
			
			return;
		} 
		
		
		// XXX not allow double open
		partService.showPart(itemPart, PartState.ACTIVATE);
		
	
		
		
	}

	private java.util.Optional <MPart>  isExisting(InfoItem infoItem ) {
		

        List<MPart> parts = modelService.findElements(application, MPart.class, EModelService.IN_ANY_PERSPECTIVE,
                        new Selector()
                        {
                            @Override
                            public boolean select(MApplicationElement element)
                            {
                            	
                            	if (element instanceof MPart) {
                            		
                            		if (ItemPart.class.isInstance(  ((MPart) element).getObject() )) {
                            			
                            			ItemPart p = (ItemPart)   ((MPart) element).getObject() ;
        	
                            			try {
                            				
                            				// Already open ? Than the Filenames and the context must be same
                            				
                            				if (Type.NEW.equals( p._wrapper.getType() )) {
                            					
                            					return p._wrapper.getInfoItem().getName().equals( infoItem.getName()) && 
                            							
                            							 p._wrapper.getInfoItem().getContext().equals(infoItem.getContext())
                            							
                            							;
                            							
                            					
                            				}
                            				
											return infoItem.getIdentifier().equals(p._wrapper.getInfoItem().getIdentifier());
										} catch (Exception e) {
											// Should not happen
										}
                            		
                            		}
                            		
                            		
                            	}
                 
                            	return  false;
                            
                            }
                        });
        
        if (parts != null && parts.size() > 0 ) {
        	return  java.util.Optional.ofNullable(parts.get(0))  ;
        }
        
        return java.util.Optional .empty();
		
	}

	
	private void createColumns(AutoResizeTableLayout layout) {

		swtItemlistTableViewer.getTable().setLayout(layout);

		swtItemlistTableViewer.getTable().setHeaderVisible(true);

		swtItemlistTableViewer.getTable().setLinesVisible(true);

		TableViewerColumn colIcon = new TableViewerColumn(swtItemlistTableViewer, SWT.NONE);

		colIcon.getColumn().setText(messages.itemlistPart_colIcon);

		colIcon.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(15));

		
		
		LinkOpener <InfoItem> linkHandler = new LinkOpener<InfoItem>() {
			@Override
			public void openLink(InfoItem rowObject) {
				
	        	try {
            	 	String local = desktopHelper.getLocallink(rowObject);
                	desktopHelper.openUrl(local);
            	}catch(Exception ex) {
            		logger.error(ex);
            	}
           
			}
		};
		
		colIcon.setLabelProvider(new LinkLabelProvider(new ColumnLabelProvider() {
			
			@Override

			public String getText(Object element) {

				InfoItem item = (InfoItem) element;
				
				return item.getMetadata().getMimetype();

			}
			
		}, linkHandler));
		
		/*
		
		colIcon.setLabelProvider(new ColumnLabelProvider() {

			@Override

			public String getText(Object element) {

				InfoItem item = (InfoItem) element;
				
				return item.getMetadata().getMimetype();

			}
			
		});

		*/
	
		
		
		TableViewerColumn colDate = new TableViewerColumn(swtItemlistTableViewer, SWT.NONE);

		colDate.getColumn().setText(messages.itemlistPart_colDate);

		colDate.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(20));

		colDate.setLabelProvider(new ColumnLabelProvider() {

			@Override

			public String getText(Object element) {

				InfoItem item = (InfoItem) element;
				
				if (item.getIssueDate() != null) {
					return df.format(item.getIssueDate());
				} else {
					return df.format(item.getDateCreated());
				}
				
			}
			
		});
		
		
		
		TableViewerColumn colType = new TableViewerColumn(swtItemlistTableViewer, SWT.LEFT);

		colType.getColumn().setText(messages.contextlistpart_table_col_type);

		colType.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(25));
		
		
		MultilineLabelProvider <InfoItem> artprovider = new MultilineLabelProvider<>(swtItemlistTableViewer.getTable() ,
				
				 e -> {
					 	StringBuffer sb = new StringBuffer();
					 	
						Iterator<InfoKategory> itwas =  e.getTypList().iterator();
						
						while (itwas.hasNext()) {
							sb.append(itwas.next().getName());
							if (itwas.hasNext()) {
								sb.append("\n");
							}
						}
				
					 	return sb;
					 	
					 	}
				 
				);
		
		
		colType.setLabelProvider( artprovider );

		

		TableViewerColumn colName = new TableViewerColumn(swtItemlistTableViewer, SWT.NONE);

		colName.getColumn().setText(messages.contextlistpart_table_col_name);

		colName.getColumn().setMoveable(true);

		layout.addColumnData(new ColumnWeightData(30));

		colName.setLabelProvider(new ColumnLabelProvider() {

			@Override

			public String getText(Object element) {

				InfoItem c = (InfoItem) element;
				return  c.getName(); 
			}

		});

		TableViewerColumn sumAttributes = new TableViewerColumn(swtItemlistTableViewer, SWT.LEFT);

		sumAttributes.getColumn().setText(messages.contextlistpart_table_col_store);

		sumAttributes.getColumn().setMoveable(true);
		
		MultilineLabelProvider <InfoItem> provider = new MultilineLabelProvider<>(swtItemlistTableViewer.getTable() ,
				
				 e -> {
					 	StringBuffer sb = new StringBuffer();
					 	
					 	if (e.getStoreIdentifier() != null) {
					 		try { // cached, should not be so bad
								sb.append(service.getOriginalStoreByIdentifer(e.getStoreIdentifier()).getName());
							} catch (MatrosServiceException e1) {
								logger.error(e1);
							}
					 	}
					 	
					 	if (e.getStoreItemNumber() != null) {
						 	sb.append( "\n").append(e.getStoreItemNumber());					 
					 	}
					 	return sb;
					 	
				 	}
				 
				);
		
		
		sumAttributes.setLabelProvider( provider );
		
				
	
		
		// ---
//		
//		sumAttributes.setLabelProvider(new ColumnLabelProvider() {
//
//			@Override
//
//			public String getText(Object element) {
//
//				InfoItem c = (InfoItem) element;
//
//			    if (c.getStoreItemNumber() != null) {
//			    	
//			    	StringBuffer sb = new StringBuffer();
//			    	
//			    	InfoOrginalstore store = masterdataService.findInfoOrginalstoreByIdentifier(c.getStoreIdentifier());
//			    	
//			    	if (store != null) {
//			    		
//			    	}
//			    	
//			    	return c.getStoreItemNumber(); 
//			    } else {
//			    	return "";
//			    }
//			   
//				
//
//			}
//
//		});

		
		
		
		layout.addColumnData(new ColumnWeightData(25));

	}

//	@Focus
//
//	public void setFocus() {
//
//		swtItemlistTableViewer.getControl().setFocus();
//
//	}



}
