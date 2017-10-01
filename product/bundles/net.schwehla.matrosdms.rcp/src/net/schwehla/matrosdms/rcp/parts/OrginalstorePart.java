 
package net.schwehla.matrosdms.rcp.parts;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.parts.filter.ItemFilter;
import net.schwehla.matrosdms.rcp.parts.helper.MatrosTableBuilder;

public class OrginalstorePart {

    List <InfoOrginalstore> _allAttributes;
	private Text txtInput;
    private TableViewer tableViewer;
    
    
    @Inject
    private IMatrosServiceService service;
    

    @Inject
	@Translation
	MatrosMessage messages;
	@Inject
    ItemFilter filter;

	@Inject ESelectionService selectionService;
	
	@Inject 
	Logger logger;
	
	@Inject
	@Optional
	private void subscribeTOPIC__REFRESH_ORIGINALSTORE_ADD(
			@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ORIGINALSTORE_ADD) InfoOrginalstore type) {

		if (tableViewer != null && !tableViewer.getTable().isDisposed()) {

	        try {
	        	  _allAttributes.clear();
	        	  _allAttributes.addAll(service.loadInfoStoreList() );
				
			} catch (MatrosServiceException e1) {
				logger.error(e1);
			}

	        tableViewer.refresh(true);
	          

		}

	}

	

    @Inject
	private IEventBroker eventBroker;
	
	@PostConstruct
    public void createComposite(Composite parent) {
          parent.setLayout(new GridLayout(1, false));


          
          txtInput = new Text(parent, SWT.BORDER);
          txtInput.setMessage(messages.part_attribute_search_background);
          txtInput.addModifyListener(new ModifyListener() {
        	  
        	  
        	@Override
        	public void modifyText(ModifyEvent e) {
        		
        		filter.setSearchString(txtInput.getText());
        		tableViewer.refresh(false);
        	
        			
        	}
        	
        	
        	  
//                 @Override
//                 public void modifyText(ModifyEvent e) {
//                        dirty.setDirty(true);
//                 }
                 
                 
          });
          


          txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

          tableViewer = new TableViewer(parent, 
          	    SWT.H_SCROLL |
        	    SWT.V_SCROLL |
        	    SWT.FULL_SELECTION |SWT.BORDER );

          
          tableViewer.setContentProvider(ArrayContentProvider.getInstance());
          
          MatrosTableBuilder <InfoOrginalstore> builder = new MatrosTableBuilder<InfoOrginalstore>(tableViewer);
          builder.makeColumn("Name").setFunction(e -> e.getName()).append(50);
          builder.makeColumn("Shortname").setFunction(e -> e.getShortname()).append(50);
          builder.build();
     
          
          
          GridData gridData = new GridData();
          gridData.grabExcessHorizontalSpace = true;
          gridData.grabExcessVerticalSpace = true;
          gridData.horizontalAlignment = GridData.FILL;
          gridData.verticalAlignment = GridData.FILL;
          tableViewer.getTable().setLayoutData(gridData);
          
          
          
          try {
			_allAttributes = service.loadInfoStoreList();
		
			
		} catch (MatrosServiceException e1) {
			logger.error(e1);
		}

          
          
          tableViewer.setInput(_allAttributes);
 
          
          tableViewer.refresh();
          
          
          
//        tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
    
          tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
        	  
  
        	  
           @Override
           public void selectionChanged(SelectionChangedEvent event) {
        	   
        	      IStructuredSelection selection = tableViewer.getStructuredSelection();
                  selectionService.setSelection(selection.getFirstElement());
           }
           
           
         });
          

          
          
  		// Step 1: Get JFace's LocalSelectionTransfer instance
  		final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
          
          
      	// Drag and Drop
  		DragSource dragSource = new DragSource(tableViewer.getControl(), DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY);

  		dragSource.setTransfer(new Transfer[] { transfer });

  		dragSource.addDragListener(new DragSourceAdapter() {

  			@Override
  			public void dragStart(DragSourceEvent event) {
  				
  				eventBroker.send(MyEventConstants.TOPIC__DRAG_ORIGINALSTORE_START,new HashMap());
  				
  				super.dragStart(event);
  			}

  			@Override
  			public void dragFinished(DragSourceEvent event) {
  				
  				// post ist asynchron
  				eventBroker.send(MyEventConstants.TOPIC__DRAG_ORIGINALSTORE_FINISHED,new HashMap());

  				super.dragFinished(event);
  			}

  			public void dragSetData(DragSourceEvent event) {

  				Table table = (Table) dragSource.getControl();

  				TableItem[] selection = table.getSelection();

  				if (selection != null && selection.length == 1) {

  					// xxx

  					TableItem i = selection[0];
  					transfer.setSelection(new StructuredSelection(i));
  					
  					
  				} else {
  					
  				}

  	

  			}

  		});
  		  
          tableViewer.addFilter(filter);
          
          
    }
    

    

//    @Focus
//    public void setFocus() {
//          tableViewer.getTable().setFocus();
//    }

 
    

    

    
}