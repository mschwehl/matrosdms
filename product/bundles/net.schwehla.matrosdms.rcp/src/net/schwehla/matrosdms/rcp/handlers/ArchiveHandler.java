 
package net.schwehla.matrosdms.rcp.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;

public class ArchiveHandler {

	@Inject Logger logger;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject IMatrosServiceService matrosService;
	
    @Execute
    public void archivate(@Optional
            @Named(IServiceConstants.ACTIVE_SELECTION) InfoContext context,
            @Optional
            @Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
            @Optional INotificationService notificationService) {
    	
    	if (context != null) {
    		
			MessageBox dialog =
			        new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText("Archivate");
			dialog.setMessage("You want really archive " + context.getName() +" ?");

			// open dialog and await user selection
			int result = dialog.open();
			
			if (result == SWT.OK) {
				
				try {
						matrosService.archivareContext(context.getIdentifier());
						
						 // Notify the List to be refreshed
						 eventBroker.post(MyEventConstants.TOPIC__REFRESH_CONTEXT_ARCHIV, context);

						 
						NotificationNote note = new NotificationNote();
						note.setHeading("Element achivated " + context.getName());
						
										
						notificationService.openPopup(note);
					
				} catch (Exception e) {
					logger.error(e);
				}
				
			}
			


        	}

    }
	

		
}