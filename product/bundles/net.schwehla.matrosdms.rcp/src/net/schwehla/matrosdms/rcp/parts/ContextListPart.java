package net.schwehla.matrosdms.rcp.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.controller.InfoContextListController;
import net.schwehla.matrosdms.rcp.dialog.MoveItemToNewContextDialog;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;
import net.schwehla.matrosdms.rcp.dnd.MyDropListener;
import net.schwehla.matrosdms.rcp.parts.filter.InfoContextAndVisibleFilter;
import net.schwehla.matrosdms.rcp.parts.helper.AutoResizeTableLayout;
import net.schwehla.matrosdms.rcp.swt.filteredcomposite.FilteredTable;


/**
 * list of all maps, can be filtered by string or tags
 * @author Martin
 */

public class ContextListPart {

    private Text swtTxtSerarchfield;
    private TableViewer swtTableViewer;

    @Inject
    private IMatrosServiceService service;
	
	@Inject
	@Translation
	MatrosMessage messages;

	@Inject 
	Logger logger;
    
    InfoContext currentContext;
    

    
    @Inject ESelectionService selectionService;
    

    
    @Inject
    InfoContextListController listController;
    

    
    @Inject
    EMenuService menuservice;
    
	@Inject
	private IEventBroker eventBroker;
	  

	
	@Inject
	ItemDropper itemDropper;
	
	
	
	
	@Inject
	@Optional
	private void subscribeTOPIC_REFRESH_CONTEXTLIST(@UIEventTopic(MyEventConstants.TOPIC_REFRESH_CONTEXTLIST) InfoContext type) {

	
		swtTableViewer.refresh(true);
		selectFirst();
		
		
	//	swtContextListTableViewer.getTable().setFocus();
		
	}
	

	
	
	@Inject
	@Optional
	private void subscribeTopic_TOPIC__REFRESH_ITEM_ADD(@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ITEM_ADD) InfoItem type) {
		
		if (type != null ) {
			
			if (swtTableViewer != null && !swtTableViewer.getTable().isDisposed() ) {

				if (type != null) {
					
						type.getContext().getStorableInfoItemContainerListProxy().setCount(type.getContext().getStorableInfoItemContainerListProxy().getCount() + 1 );
						
						swtTableViewer.refresh(true);
						
						// XXX NOT POSSIBLE at virtual tables -> Disposed
//						swtContextListTableViewer.refresh(type.getContext());
						selectFirst();

				} 
			}
		
		}
	
	}
	
	
	@Inject
	@Optional
	private void subscribeTopic_TOPIC__REFRESH_ITEM_STAR(@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ITEM_ARCHIV) InfoItem type) {
		
		if (type != null ) {
			
			if (swtTableViewer != null && !swtTableViewer.getTable().isDisposed()) {

				if (type != null) {
					
						type.getContext().getStorableInfoItemContainerListProxy().setCount(type.getContext().getStorableInfoItemContainerListProxy().getCount() - 1 );
						
						swtTableViewer.refresh(true);
						// XXX NOT POSSIBLE WITH VIRTUAL TABLE -> Exposed
						// swtContextListTableViewer.refresh(type.getContext());
						selectFirst();

				} 
			}
		
		}
	
	}
	

	
	
	
	private void selectFirst() {
		
		// Convenience : If there is only one Element than select it
		if (! swtTableViewer.getTable().isDisposed() 
				&& swtTableViewer.getSelection().isEmpty()  
				&& swtTableViewer.getTable().getItemCount() > 0)  {
				
				
				if ( swtTableViewer.getTable().getSelectionCount() == 0) {
					
					try {
						
						
						// don't work with virtual tables :-(
						// swtContextListTableViewer.getTable().getItem(0).getData();
						
						// maybe this helps
						InfoContext infoContext = (InfoContext) getTopRow(swtTableViewer.getTable()) ;
						
						if (infoContext != null) {
							swtTableViewer.setSelection( new StructuredSelection(infoContext));
							eventBroker.post(MyEventConstants.TOPIC_CONTEXT_SELECTED_SELECT, infoContext );
						} 

					} catch(Exception e) {
						logger.error(e, "cannot set first row");
					}
				}
			
				


		}
	}
	
	
	public InfoContext getTopRow( Table table )
	{
		Rectangle rect = table.getClientArea ();
		int headerHeight = table.getHeaderHeight ();
		Point p = new Point( rect.x, rect.y + headerHeight + 1 );
		
		TableItem item = table.getItem(p);
		
		if (item != null) 		
		 return (InfoContext) table.getItem(p).getData();
		
		return null;
	}
	
	
	
	@Inject IEclipseContext context;
    
    @PostConstruct
    public void createComposite(Composite parent) {
    	
          parent.setLayout(new GridLayout(1, false));

          // TODO: Verhalten bei Virtual untersuchen
          
          FilteredTable ft = new FilteredTable(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL, new InfoContextAndVisibleFilter());
          
          swtTableViewer = ft.getViewer();
           swtTableViewer.setUseHashlookup(true);         
       
          
          swtTableViewer.setContentProvider(ArrayContentProvider.getInstance());
          
          AutoResizeTableLayout layout = new AutoResizeTableLayout(swtTableViewer.getTable());
          createColumns(layout);
          
          // Stetch the Table itself to maximum
          GridData gridData = new GridData();
          gridData.grabExcessHorizontalSpace = true;
          gridData.grabExcessVerticalSpace = true;
          gridData.horizontalAlignment = GridData.FILL;
          gridData.verticalAlignment = GridData.FILL;
          swtTableViewer.getTable().setLayoutData(gridData);
          
          

	      swtTableViewer.setInput(listController.getList());
	      listController.reload();
 
          swtTableViewer.refresh(true);
          
          selectFirst();
          
          swtTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
        	  
           @Override
           public void selectionChanged(SelectionChangedEvent event) {
                  
             IStructuredSelection selection =
                (IStructuredSelection) event.getSelection();
             
	             if (selection != null) {
	 				eventBroker.post(MyEventConstants.TOPIC_CONTEXT_SELECTED_SELECT, selection.getFirstElement());
	             } else {
		 			eventBroker.post(MyEventConstants.TOPIC_CONTEXT_SELECTED_REMOVE,null);
	             }
           }
           
         });
          

          
 
          
          
       // viewer is a JFace Viewer
          swtTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
        	  
              @Override
              public void selectionChanged(SelectionChangedEvent event) {
                      IStructuredSelection selection = swtTableViewer.getStructuredSelection();
                      selectionService.setSelection(selection.getFirstElement());
              }
                  
          });
          

  	    final FileTransfer fileTransfer = FileTransfer.getInstance();
  	    
  		Transfer[] transferTypes = { DomainClassTransfer.getTransfer(InfoItem.class) , fileTransfer	};
  		
  		

	    
  		
  		swtTableViewer.addDropSupport( DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY, transferTypes, 
  				new MyDropListener(swtTableViewer, transferTypes) {
  			
  			// moving one item to an other context
  			

			
  			
  			@Override
  					public void drop(DropTargetEvent event) {
		  				
  				
		  				if (fileTransfer.isSupportedType(event.currentDataType)) {
		                    String[] files = (String[]) event.data;
		                    
		                    System.out.println("dropped"); //$NON-NLS-1$
		                    return; 
		                  
		                }
		  				
		  				
		  				
		  				if (event.item != null && event.item.getData() != null) {
		  					
		  					
		  					if (LocalSelectionTransfer.class.isInstance( event.currentDataType)) {
		  						
		  						System.out.println(event.item.getData() );
		  						
		  					}
		  					
		  			
		  					// event.item is the cell from the ContextList
		  					if (event.item.getData() instanceof InfoContext) {
		  						
		  						handleInfocontextDrop(event);
		  					}
				  	
		  				}
		  				
		  		 		
  	            
  					}


			private void handleInfocontextDrop(DropTargetEvent event) {
			
					List given = (List) event.data;
				
					InfoContext conny = (InfoContext) event.item.getData();
					ArrayList<InfoItem> intersection = new ArrayList<InfoItem>(given);
					
					
				    try {
					intersection.removeAll(service.loadInfoItemList(conny, false));

					// OK, we we move items to the new context
					if (intersection.size() > 0) {
						
						
						// first of all get the ok
						
						
						MoveItemToNewContextDialog dialog = ContextInjectionFactory.make(MoveItemToNewContextDialog.class, context);
						dialog.setTitle("wanna realy move?");

						int result = dialog.open();

						// ok
						if (result == Window.OK) {
							

							service.moveInfoitems(conny.getIdentifier(), intersection);
				
							// increase selected context
							conny.getStorableInfoItemContainerListProxy().setCount(
									conny.getStorableInfoItemContainerListProxy().getCount() + intersection.size());
							
							// decrease the counter by the other contexts
							for (InfoItem i : intersection) {
								
								i.getContext().getStorableInfoItemContainerListProxy().setCount(
										i.getContext().getStorableInfoItemContainerListProxy().getCount() - 1);

								// change listelement with current
								i.setContext(conny);
			 	 				eventBroker.send(MyEventConstants.TOPIC__REFRESH_ITEM_MODIFIED,i);
								
							}
							
							// refresh list
							eventBroker.send(MyEventConstants.TOPIC_CONTEXT_SELECTED_SELECT,conny);
							
							
							
							IStructuredSelection tmp = swtTableViewer.getStructuredSelection();
											 	 		

			 				// repaint the contextlist
							swtTableViewer.refresh(true);
							
							if (tmp != null) {
								swtTableViewer.setSelection(tmp,true);
							}
							
							
						}
						
						
				
						
						// post reload
					}
					
  	       
  	            	
				} catch (MatrosServiceException e) {
					logger.error(e);
				}
					
					
				
			}

  		});
  		
           
      	menuservice.registerContextMenu(swtTableViewer.getTable(),
    			"net.schwehla.matrosdms.rcp.popupmenu.contextlistpopup");
      	
          


    }
    

    /**
    * Create the columns to be used in the table
    */
    private void createColumns(AutoResizeTableLayout layout) {
    	
    	  
          swtTableViewer.getTable().setLayout(layout);
          swtTableViewer.getTable().setHeaderVisible(true);
          swtTableViewer.getTable().setLinesVisible(true);
          
     
          TableViewerColumn colTagging = new TableViewerColumn(swtTableViewer, SWT.LEFT);
          colTagging.getColumn().setText(messages.contexlistpart_col_map);
          layout.addColumnData(new ColumnWeightData(100));
          
          
          
          colTagging.setLabelProvider(new OwnerDrawLabelProvider(){
        	  
      		private void buildContent(InfoContext entry, StringBuffer sb) {
				Iterator<InfoKategory> itwer =  entry.getTagList(MyGlobalConstants.ROOT_WER).iterator();
				
				while (itwer.hasNext()) {
					sb.append(itwer.next().getName());
					if (itwer.hasNext()) {
						sb.append("+");
					}
				}
				
				sb.append("\n");
				
				
				Iterator<InfoKategory> itwas =  entry.getTagList(MyGlobalConstants.ROOT_WAS).iterator();
				
				while (itwas.hasNext()) {
					sb.append(itwas.next().getName());
					if (itwas.hasNext()) {
						sb.append("+");
					}
				}
				
				sb.append("\n");
				
				
				
				
				Iterator<InfoKategory> itwo =  entry.getTagList(MyGlobalConstants.ROOT_WO).iterator();
				
				while (itwo.hasNext()) {
					sb.append(itwo.next().getName());
					if (itwo.hasNext()) {
						sb.append("+");
					}
				}
				
				sb.append("\n");
			}
        	  	
        	  
		    	  // http://stackoverflow.com/questions/26463520/eclipse-ownerdrawlabelprovider-makes-selection-background-dark-blue
		    	  @Override
		    	  protected void erase(Event event, Object element) 
		    	  {
		    	    // Don't call super to avoid selection draw
		    		// Elswise the Background-Color is wrong  
		    	  }

                 @Override
                 protected void measure(Event event, Object element) {
                	 
                	 	// Element is filtered
                	 	if (element == null) {
                	 		return;
                	 	}
                	 
                        event.width = swtTableViewer.getTable().getColumn(event.index).getWidth();
                        if (event.width == 0) {
                        	   return;
                        }
                            
                        InfoContext entry = (InfoContext) element;
                        
                        StringBuffer sb = new StringBuffer();
                        
                        buildContent(entry, sb);
                        
                         
               
                        Point size = event.gc.textExtent(sb.toString() );
                        event.height = size.y ; 

                 }

		

                 @Override
                 protected void paint(Event event, Object element) {
                	 
	             	 	// Element is filtered
	             	 	if (element == null) {
	            	 		return;
	            	 	}
             	 	

                        InfoContext entry = (InfoContext) element;
                        
                        StringBuffer sb = new StringBuffer();
                        
                        buildContent(entry, sb);
                        
                        
                        event.gc.drawText(sb.toString() , event.x, event.y, true);
                 }}
                        
          );

                 
          
          
          TableViewerColumn colName = new TableViewerColumn(swtTableViewer, SWT.NONE);
          colName.getColumn().setText(messages.contextlistpart_table_col_name);
          
          layout.addColumnData(new ColumnWeightData(300));          
          
          colName.setLabelProvider(new ColumnLabelProvider() {
                 
                 @Override
                 public String getText(Object element) {
                	 
                	 InfoContext c = (InfoContext) element;
                	 
                   return c.getName();  // no string representation, we only want to display the image
                 }
                 
          });    
                 
                 
          TableViewerColumn sum = new TableViewerColumn(swtTableViewer, SWT.LEFT);

          sum.getColumn().setText(messages.contextlistpart_table_col_itemcount);
          
          sum.setLabelProvider(new ColumnLabelProvider() {
                 
                 @Override
                 public String getText(Object element) {
                	 
                	 InfoContext c = (InfoContext) element;
                	 
                	 return "" + c.getStorableInfoItemContainerListProxy().getCount();
                 }
                 
          });    
          
          layout.addColumnData(new ColumnWeightData(50));     
          
    }
    

    @Focus
    public void setFocus() {
          swtTableViewer.getTable().setFocus();
          selectFirst();
    }
    
    
 
    
}

