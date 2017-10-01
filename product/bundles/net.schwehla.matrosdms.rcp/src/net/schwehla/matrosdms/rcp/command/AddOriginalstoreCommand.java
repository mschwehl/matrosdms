 
package net.schwehla.matrosdms.rcp.command;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dialog.OriginalstoreDialog;

public class AddOriginalstoreCommand {
	
	@Inject IMatrosServiceService service;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	Logger logger;
	
	
	@Execute
	public void execute(IEclipseContext context, @Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		
		InfoOrginalstore orginalNew = new InfoOrginalstore(Identifier.createNEW(),"");
		
        OriginalstoreDialog dialog = new OriginalstoreDialog(Display.getCurrent().getActiveShell(), orginalNew );
	       
			    dialog.create();
				  
			     // get the new values from the dialog
                if (dialog.open() == Window.OK) {
                	
                	
              


        			try {
        				
        			  	service.createOrignalStore(orginalNew);
        	             
        				eventBroker.send(MyEventConstants.TOPIC__REFRESH_ORIGINALSTORE_ADD, 
        						service.loadOrginalstoreByIdentifier(orginalNew.getIdentifier()));
        						
        			} catch (MatrosServiceException e) {
        				logger.error(e, "Cannot refresh Attributes"); //$NON-NLS-1$
        				eventBroker.send(MyEventConstants.TOPIC__REFRESH_ORIGINALSTORE_ADD, null);
        			}
        				
        		
				    
                }
    
	}
	
	
	
	
}