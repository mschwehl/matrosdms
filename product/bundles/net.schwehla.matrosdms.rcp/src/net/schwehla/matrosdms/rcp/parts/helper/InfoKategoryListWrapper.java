package net.schwehla.matrosdms.rcp.parts.helper;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.domain.util.ObjectCloner;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dialog.CreateNewKategoryMatrosTitleAreaDialog;
import net.schwehla.matrosdms.rcp.dialog.RenameKategoryMatrosTitleAreaDialog;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;
import net.schwehla.matrosdms.rcp.dnd.MyDragListener;
import net.schwehla.matrosdms.rcp.parts.filter.TagSmartTreeFilter;
import net.schwehla.matrosdms.rcp.swt.contentprovider.TaggraphContentprovider;
import net.schwehla.matrosdms.rcp.swt.filteredcomposite.FilteredTree;
import net.schwehla.matrosdms.rcp.swt.filteredcomposite.PatternFilter;
import net.schwehla.matrosdms.rcp.swt.labelprovider.TaggraphLabelProvider;


@Creatable
public class InfoKategoryListWrapper {
	


	@Inject
	public InfoKategoryListWrapper() {
		
	}

	@Inject Logger logger;
	
	@Inject
	private IMatrosServiceService service;

	@Inject
	private IEventBroker eventBroker;

	// use field injection for the service
	@Inject
	ESelectionService selectionService;

	@Inject
	MApplication application;


	@Inject
	IEclipseContext context;
	
	
	InfoKategory root ;
	Identifier  givenRoot ;
	
	boolean editMode = false;
	
	
	// Checkbox if context-aware
	boolean _filterToContext = false;

	private TreeViewer _treeviewer;
	FilteredTree filteredTree;
	
	boolean showRoot;
	
	Composite treeComposite ;
	
	
	public TreeViewer getTreeViewer() {
		return _treeviewer;
	}

	
	@Inject
	@Optional
	/**
	 * OK, double-clicked on one Element, eg. Car
	 * @param modelType
	 */
	private void subscribeTopicTOPIC_TAGGRAPH_DOUBLEKLICK_ADD_ELEMENT(@UIEventTopic(MyEventConstants.TOPIC_REFRESH_INFOKATEGORY_MODIFIED) InfoKategory type) {

		if (type != null 
				&& _treeviewer != null && ! _treeviewer.getTree().isDisposed()) {
			
			
			try {
				root = new ObjectCloner<InfoKategory>().cloneThroughSerialize(service.getInfoKategoryByIdentifier(type.getIdentifier()));
				setRootAndRefresh(type);
			} catch (MatrosServiceException e) {
				logger.error(e);
			}
		
		}
	    
	}
	
	
	public InfoKategoryListWrapper init(Composite parent, Identifier type, boolean showRoot) throws MatrosServiceException {

		givenRoot = type;		
		this.showRoot = showRoot;
		
		init(parent);
		root = new ObjectCloner<InfoKategory>().cloneThroughSerialize(service.getInfoKategoryByIdentifier(type));

		
		setRootAndRefresh(root);
		
		return this;
		
	}

	public void refresh() {
		_treeviewer.refresh(false);
		
	}

	public void setRootAndRefresh(InfoKategory root) {

		
		if (_treeviewer != null ) {
			
			if (showRoot) {
				InfoKategory pseudoRoot = new InfoKategory(Identifier.createNEW(),"PSEUDOROOT");
				pseudoRoot.connectWithChild(root);
				
				// DIRTY-HACK: should work without this line
				root.setParents(new ArrayList<>());
				_treeviewer.setInput(pseudoRoot);
				
			} else {
				_treeviewer.setInput(root);
			}
			
	 		_treeviewer.refresh();
		}
		
	}


	
	
	public Control getComponent() {
		return treeComposite;
	}

	private void init(Composite parent) {

		treeComposite = new Composite(parent,  SWT.NONE);
		treeComposite.setData(this);
		
		treeComposite.setLayout( new GridLayout(1, false));
  	    treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		
		PatternFilter patternFilter = new PatternFilter();
		patternFilter.setIncludeLeadingWildcard(true);
		

		// Needs to be crated via context because of ContextListController
		TagSmartTreeFilter smartTagFilter =  ContextInjectionFactory.make( TagSmartTreeFilter.class, context);
		
		filteredTree = new FilteredTree(treeComposite, SWT.NONE | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, patternFilter, smartTagFilter);
		

		
		_treeviewer = filteredTree.getViewer();
		_treeviewer.getTree().setHeaderVisible(false);
		
		_treeviewer.getTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {

				TreeItem[] selection = _treeviewer.getTree().getSelection();

				if (! editMode && selection != null && selection.length == 1) {
					TreeItem i = selection[0];
					InfoKategory k = (InfoKategory) i.getData();
					eventBroker.send(MyEventConstants.TOPIC_TAGGRAPH_DOUBLEKLICK_ADD_ELEMENT, k);
				}

			}
		});
		
	//	 DND
		
		Transfer[] transferTypes = { DomainClassTransfer.getTransfer(InfoKategory.class)};
			
		_treeviewer.addDragSupport( DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY,transferTypes
				, new MyDragListener(_treeviewer,InfoKategory.class)   {
			
			@Override
			public void dragStart(DragSourceEvent event) {
				
	//			 XXX evtl contextlist open
				super.dragStart(event);
			}
			
			
			@Override
			public void dragFinished(DragSourceEvent event) {
				
				/*
				
				IStructuredSelection se = _treeviewer.getStructuredSelection();
				
				Set cloned = new HashSet<>(se.toList());
				((List)_treeviewer.getInput() ). removeAll(cloned);
				
				_treeviewer.refresh();
				
				*/
				
			}

			
		});
		
		
		
		_treeviewer.setContentProvider(new TaggraphContentprovider());
		_treeviewer.setLabelProvider(
				new DelegatingStyledCellLabelProvider(new TaggraphLabelProvider()));

		_treeviewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = _treeviewer.getStructuredSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});


		
		// Create the popup menu
		  MenuManager menuMgr = new MenuManager();
		  Menu menu = menuMgr.createContextMenu(_treeviewer.getControl());
		  menuMgr.addMenuListener(new IMenuListener() {
		    @Override
		    public void menuAboutToShow(IMenuManager manager) {
		      if(_treeviewer.getSelection().isEmpty()) {
		        return;
		      }

		      if(_treeviewer.getSelection() instanceof IStructuredSelection) {
		        IStructuredSelection selection = (IStructuredSelection)_treeviewer.getSelection();
		        
		    
		        InfoKategory infoKategory = (InfoKategory)selection.getFirstElement();

		      
		          manager.add(new Action() {
		        	  
		        	  public String getText() { return "edit"; };
		        	  
			          @Override
			          public void run() {
			        
			        	  

			        			
			        			RenameKategoryMatrosTitleAreaDialog dialog = ContextInjectionFactory.make(RenameKategoryMatrosTitleAreaDialog.class, context);
			        			
			        			dialog.setTitle(infoKategory.getName());
			        			dialog.setClonedRoot(infoKategory);
			        			
			        			
			        			dialog.open();
			        			
			        			if (dialog.getLastSavedElementIdentifier() != null) {

			        				try {
			        					
			        					
			        					
			        					InfoKategory x = new ObjectCloner<InfoKategory>().cloneThroughSerialize(service.getInfoKategoryByIdentifier(root.getIdentifier()));
			        					root = x;
			        					setRootAndRefresh(root);
			        					
			        					InfoKategory element = root.getSelfAndAllTransitiveChildren().stream().filter(treeElement -> Objects.equals(treeElement.getIdentifier(), dialog.getLastSavedElementIdentifier())).findFirst().orElse(null);
			        						
			        					if ( element != null) {

			        						_treeviewer.setExpandedState(element.getParents().get(0),true);
			        					
			        						_treeviewer.setAutoExpandLevel( _treeviewer.ALL_LEVELS);
			        						_treeviewer.setSelection( new StructuredSelection(element ));
			        						_treeviewer.expandToLevel(element , _treeviewer.ALL_LEVELS);
			        					
			        					}
			        					
			        					
			        				} catch (MatrosServiceException e) {
			        					logger.error(e);
			        				}
			        					
			        			}
			        		
			        		
			        		
			        		}
			        		
			    
		          });
		          
		          
		          
		          manager.add(new Separator("xxx"));
		          
		          
		          manager.add(new Action() {
		        	  public String getText() { return "archivare"; };
		          });
		          
		          manager.add(new Action() {
		        	  public String getText() { return "delete permanent"; };
		          });
		   
		      }
		    }
		  });

		  menuMgr.setRemoveAllWhenShown(true);
		  _treeviewer.getControl().setMenu(menu);
		  
		
		  _treeviewer.getControl().addKeyListener(new KeyAdapter() {
		    	
		      public void keyPressed(KeyEvent event) {
		    	  
		    	   
		        if (event.keyCode == SWT.F2 && _treeviewer.getTree().getSelectionCount() == 1) {
		          final TreeItem item = _treeviewer.getTree().getSelection()[0];

		          // rename-popup

		          MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Confirm", "Please confirm");
		          
		    
		      }
		      }}
		  );
		  
		
		
	}
	
	/**
	 * Creates a new Kategory on Request
	 */

	public void createNewKategoryInTree() {
		
		CreateNewKategoryMatrosTitleAreaDialog dialog = ContextInjectionFactory.make(CreateNewKategoryMatrosTitleAreaDialog.class, context);
		
		dialog.setTitle(root.getName());
		dialog.setClonedRoot(root);
		
		if (! _treeviewer.getStructuredSelection().isEmpty()) {
			dialog.setSelectedRoot((InfoKategory) _treeviewer.getStructuredSelection().getFirstElement());
		} else {
			dialog.setSelectedRoot(root);
		}
		
		
		dialog.open();
		
		if (dialog.getLastSavedElementIdentifier() != null) {

			try {
				
				InfoKategory x = new ObjectCloner<InfoKategory>().cloneThroughSerialize(service.getInfoKategoryByIdentifier(root.getIdentifier()));
				root = x;
				setRootAndRefresh(root);
				
				InfoKategory element = root.getSelfAndAllTransitiveChildren().stream().filter(treeElement -> Objects.equals(treeElement.getIdentifier(), dialog.getLastSavedElementIdentifier())).findFirst().orElse(null);
				  
					
				if ( element != null) {

					_treeviewer.setExpandedState(element.getParents().get(0),true);
				
					_treeviewer.setAutoExpandLevel( _treeviewer.ALL_LEVELS);
					_treeviewer.setSelection( new StructuredSelection(element ));
					_treeviewer.expandToLevel(element , _treeviewer.ALL_LEVELS);

				
				}
				
				
			} catch (MatrosServiceException e) {
				logger.error(e);
			}
				
		}
	
	
	
	}

	public void setExpanded(boolean bool) {
		
		// http://stackoverflow.com/questions/40417487/java-swt-tree-expand-all-collapse-all
		_treeviewer.expandAll();
		_treeviewer.getTree().layout(true);
		
	}
	

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}



	public Identifier getRootIdentifier() {
		return givenRoot;
	
	}
}
