 
package net.schwehla.matrosdms.rcp.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoEvent;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.parts.filter.ItemFilter;
import net.schwehla.matrosdms.rcp.parts.helper.MatrosTableBuilder;

public class EventPart {

    List <InfoEvent> _allEvents;
    
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
	private IEventBroker eventBroker;
	
	@Inject
	UISynchronize sync;
	
	
	    	
		 MPart _part;
    
	@PostConstruct
    public void createComposite(Composite parent, MPart part) {
		
		this._part = part;
    	
		

          parent.setLayout(new GridLayout(1, false));


          
          txtInput = new Text(parent, SWT.BORDER);
          txtInput.setMessage(messages.part_event_search_background);
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
          
          MatrosTableBuilder <InfoEvent> builder = new MatrosTableBuilder<InfoEvent>(tableViewer);
          builder.makeColumn("Name").setFunction(e -> e.getName()).append(30);
          builder.makeColumn("Scheduled").setFunction(e -> e.getDateScheduled() ).append(30);
          builder.makeColumn("Target").setFunction(e -> (e.getItem() != null ? e.getItem().getName() : "")  ).append(30);

          builder.build();
     
          
          
          GridData gridData = new GridData();
          gridData.grabExcessHorizontalSpace = true;
          gridData.grabExcessVerticalSpace = true;
          gridData.horizontalAlignment = GridData.FILL;
          gridData.verticalAlignment = GridData.FILL;
          tableViewer.getTable().setLayoutData(gridData);
          
          
          
          try {
        	  _allEvents = service.loadEventList();
        	  
        	  
		
			
		} catch (MatrosServiceException e1) {
			logger.error(e1);
		}
          
          
      	sync.asyncExec(new Runnable() {

			@Override

			public void run() {

		          _part.setLabel("Events: " + _allEvents.size());
		          ((Composite)_part.getWidget()).redraw();
			}
			
      	});


          
          tableViewer.setInput(_allEvents);
 
          
          tableViewer.refresh();
          
          
          
//        tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
    
          tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
        	  
  
        	  
           @Override
           public void selectionChanged(SelectionChangedEvent event) {
        	   
        	      IStructuredSelection selection = tableViewer.getStructuredSelection();
                  selectionService.setSelection(selection.getFirstElement());
           }
           
           
         });
          


  		  
          tableViewer.addFilter(filter);
          
          
    }
    

    

//    @Focus
//    public void setFocus() {
//          tableViewer.getTable().setFocus();
//    }

 
    

    

    
}