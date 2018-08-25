package net.schwehla.matrosdms.rcp.parts.helper;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
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

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dnd.DomainClassTransfer;
import net.schwehla.matrosdms.rcp.dnd.MyDragListener;
import net.schwehla.matrosdms.rcp.parts.filter.ItemFilter;


@Creatable
public class InfoAttributeListWrapper {
	
	@Inject
    ItemFilter filter;

	@Inject
	public InfoAttributeListWrapper() {
		
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
	@Translation
	MatrosMessage messages;
    
	@Inject
	IEclipseContext context;
	
	

    List <AttributeType> _allAttributes;
	private Text txtInput;
    private TableViewer tableViewer;
    
	
	
	@Inject
	@Optional
	private void subscribeTOPIC__REFRESH_ATTRIBUTE_ADD(
			@UIEventTopic(MyEventConstants.TOPIC__REFRESH_ATTRIBUTE_ADD) AttributeType type) {

		if (tableViewer != null && !tableViewer.getTable().isDisposed()) {

	        try {
	        	  _allAttributes.clear();
	        	  _allAttributes.addAll(service.loadAttributeTypeList() );
				
			} catch (MatrosServiceException e1) {
				logger.error(e1);
			}

	        tableViewer.refresh();
	          

		}

	}

	



	public void refresh() {
		tableViewer.refresh(false);
		
	}


	public void init(Composite parent) {
        parent.setLayout(new GridLayout(1, false));


        
        txtInput = new Text(parent, SWT.BORDER);
        txtInput.setMessage(messages.part_attribute_search_background);
        txtInput.addModifyListener(new ModifyListener() {
      	  
      	  
      	@Override
      	public void modifyText(ModifyEvent e) {
      		
      		filter.setSearchString(txtInput.getText());
      		tableViewer.refresh(false);
      	
      			
      	}
      	
      	
      	  
//               @Override
//               public void modifyText(ModifyEvent e) {
//                      dirty.setDirty(true);
//               }
               
               
        });
        


        txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        tableViewer = new TableViewer(parent, 
        	    SWT.H_SCROLL |
      	    SWT.V_SCROLL |
      	    SWT.FULL_SELECTION |SWT.BORDER );

        
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        
        MatrosTableBuilder <AttributeType> builder = new MatrosTableBuilder<AttributeType>(tableViewer);
        builder.makeColumn("Name").setFunction(e -> e.getName()).append(50);
        builder.makeColumn("Type").setFunction(e -> e.getType().name()).append(50);
        builder.addSorter();
   
        
        
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        tableViewer.getTable().setLayoutData(gridData);
        
        
        
        try {
			_allAttributes = service.loadAttributeTypeList();
		
			
		} catch (MatrosServiceException e1) {
			logger.error(e1);
		}

        
        
        tableViewer.setInput(_allAttributes);

        
        tableViewer.refresh();
        
        
        
//      tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
  
        tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
      	  

      	  
         @Override
         public void selectionChanged(SelectionChangedEvent event) {
      	   
      	      IStructuredSelection selection = tableViewer.getStructuredSelection();
                selectionService.setSelection(selection.getFirstElement());
         }
         
         
       });
        
        
		// Step 1: Get JFace's LocalSelectionTransfer instance

		Transfer[] transferTypes = { DomainClassTransfer.getTransfer(InfoBaseElement.class)};
		
	
        
        tableViewer.addDragSupport( DND.DROP_MOVE | DND.DROP_LINK | DND.DROP_COPY,transferTypes
				, new MyDragListener(tableViewer,InfoBaseElement.class)   {
			
			@Override
			public void dragStart(DragSourceEvent event) {
				
  				// coloring
  				eventBroker.send(MyEventConstants.TOPIC__DRAG_ATTRIBUTE_START,new HashMap());
  				
  				super.dragStart(event);
			}
			
			
			@Override
			public void dragFinished(DragSourceEvent event) {
				
  				// post ist asynchron
  				// coloring
  				eventBroker.send(MyEventConstants.TOPIC__DRAG_ATTRIBUTE_FINISHED,new HashMap());

  				super.dragFinished(event);
				
			}

			
		});
		
		
	
        tableViewer.addFilter(filter);
        
        
  }
	

}
