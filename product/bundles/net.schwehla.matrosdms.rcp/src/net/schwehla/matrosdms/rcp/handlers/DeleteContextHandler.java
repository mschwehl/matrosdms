 
package net.schwehla.matrosdms.rcp.handlers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.Selector;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;

public class DeleteContextHandler {
	
	@Inject Logger logger;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject IMatrosServiceService matrosService;

	
	@Execute
	public void execute(EModelService modelService , MApplication application	,
			EPartService partService
			, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) InfoContext infoContext,
            @Optional
            @Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
            @Optional INotificationService notificationService
            ) {
		

		if (infoContext == null) {
			return;
		}



		 java.util.Optional <MPart> existing = isExisting(application,modelService,infoContext);
			
		  if (existing.isPresent()) {
				
				MPart part = existing.get();
				partService. hidePart(part);
				
   		}  
		  
		  
		  
		 	if (infoContext != null) {
	    		
				MessageBox dialog =
				        new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
				dialog.setText("DELETE");
				dialog.setMessage("You want really delete " + infoContext.getName() +" ?");

				// open dialog and await user selection
				int result = dialog.open();
				
				if (result == SWT.OK) {
					
					try {
							matrosService.deleteContext(infoContext.getIdentifier());
							
							 // Notify the List to be refreshed
							 eventBroker.post(MyEventConstants.TOPIC__REFRESH_CONTEXT_ARCHIV, infoContext);

							 
							NotificationNote note = new NotificationNote();
							note.setHeading("Element deleted " + infoContext.getName());
											
							notificationService.openPopup(note);
						
					} catch (Exception e) {
						logger.error(e);
					}
					
				}
				


	        	}
		 	
		  
		  
	}
	
	

	private java.util.Optional <MPart>  isExisting( MApplication application, EModelService modelService, InfoContext infoItem ) {
		

        List<MPart> parts = modelService.findElements(application, MPart.class, EModelService.IN_ANY_PERSPECTIVE,
                        new Selector()
                        {
                            @Override
                            public boolean select(MApplicationElement element)
                            {
                            	
                            	if (element instanceof MPart) {
                            		
                            		if (InfoContext.class.isInstance(  ((MPart) element).getObject() )) {
                            			
                            			InfoContext p = (InfoContext)   ((MPart) element).getObject() ;
        	
                            			return infoItem.getIdentifier().equals(p.getIdentifier());
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
		
}