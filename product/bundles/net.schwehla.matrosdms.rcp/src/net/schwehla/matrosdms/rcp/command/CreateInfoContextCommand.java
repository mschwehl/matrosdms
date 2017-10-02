 
package net.schwehla.matrosdms.rcp.command;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItemListProxy;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.dialog.CreateNewContextDialog;

public class CreateInfoContextCommand {
	
	
	public CreateInfoContextCommand() {
		
	}

	@Inject IMatrosServiceService service;
	
	@Inject IEclipseContext context;
	
	@Inject	IEventBroker eventBroker;
	
	@Inject Logger logger;

	/**
	 * The Local Context is in relation with the dropfieldpart
	 * 
	 * @param _localContext
	 * @param parentShell
	 */
	

	@Execute
	public void execute(@Optional @Named(MyEventConstants.ACTIVE_DROPFIELD_CONSTELLATION_CONTEXT) InfoContext _localContext,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		
	
		if (_localContext != null) {
			
			CreateNewContextDialog dialog = ContextInjectionFactory.make(CreateNewContextDialog.class, context);
				
		    // get the new values from the dialog
		    if (dialog.open() == Window.OK) {
		     
	
		      InfoContext matroxInfocontext = new InfoContext(Identifier.createNEW(),dialog.getBaseElement());
		      
		      matroxInfocontext.getDictionary().putAll( _localContext.getDictionary());
		      
				try {
					service.createContext(matroxInfocontext);
					
					matroxInfocontext.setStorableInfoItemContainerListProxy( new InfoItemListProxy(matroxInfocontext));
					matroxInfocontext.getStorableInfoItemContainerListProxy().setCount(0);
					
					// Asynchron damit die GUI nicht blokiert
					eventBroker.post(MyEventConstants.TOPIC_CONTEXT_CREATED, matroxInfocontext);
					
				} catch (MatrosServiceException e1) {
					logger.error(e1);
				}
				
	
		    }
			
		}
	
	}
	
	@CanExecute
	public boolean canExecute() {
		// XXX check for duplicates in Conntext
		return true;
	}
	
		
}