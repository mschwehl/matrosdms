 
package net.schwehla.matrosdms.rcp.command;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dialog.CreateNewAttributeMatrosTitleAreaDialog;

public class AddAttributeCommand {
	
	@Inject IMatrosServiceService service;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	Logger logger;
	
	
	@Execute
	public void execute(IEclipseContext context, @Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		
		
		CreateNewAttributeMatrosTitleAreaDialog dialog = ContextInjectionFactory.make(CreateNewAttributeMatrosTitleAreaDialog.class, context);
		dialog.setTitle("Attributes");
		
		dialog.open();
		
		if (dialog.getLastSavedElementIdentifier() != null) {

			try {
				eventBroker.send(MyEventConstants.TOPIC__REFRESH_ATTRIBUTE_ADD, 
						service.loadAttributeTypeByIdentifier(dialog.getLastSavedElementIdentifier()));
			} catch (MatrosServiceException e) {
				logger.error(e, "Cannot refresh Attributes"); //$NON-NLS-1$
				eventBroker.send(MyEventConstants.TOPIC__REFRESH_ATTRIBUTE_ADD, null);
			}
				
		}
	}
	
	
	
	
	
	
}