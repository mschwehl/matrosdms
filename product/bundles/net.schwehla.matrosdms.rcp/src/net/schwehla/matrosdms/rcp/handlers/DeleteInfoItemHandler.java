 
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

import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyEventConstants;

public class DeleteInfoItemHandler {


	@Inject Logger logger;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject IMatrosServiceService matrosService;
	
    @Execute
    public void archivate(@Optional
            @Named(IServiceConstants.ACTIVE_SELECTION) InfoItem infoItem,
            @Optional
            @Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
            @Optional INotificationService notificationService) {
    	
    	if (infoItem != null) {
    		
			MessageBox dialog =
			        new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText("DELETE");
			dialog.setMessage("You want really delete " + infoItem.getName() +" ?");

			// open dialog and await user selection
			int result = dialog.open();
			
			if (result == SWT.OK) {
				
				try {
						matrosService.deleteInfoElement(infoItem.getIdentifier());
						
						 // Notify the List to be refreshed
						 eventBroker.post(MyEventConstants.TOPIC__REFRESH_ITEM_MODIFIED, infoItem);

						 
						NotificationNote note = new NotificationNote();
						note.setHeading("Element deleted " + infoItem.getName());
										
						notificationService.openPopup(note);
					
				} catch (Exception e) {
					logger.error(e);
				}
				
			}
			


        	}

    }
	

		

	
		
}