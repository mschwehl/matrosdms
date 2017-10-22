 
package net.schwehla.matrosdms.bootstrap.addons;

import java.lang.reflect.Proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.prefs.BackingStoreException;

import net.schwehla.matrosdms.bootstrap.splash.Reciever;
import net.schwehla.matrosdms.bootstrap.splash.SplashShell;
import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.admin.MatrosUser;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.persistenceservice.internal.MatrosServerProxy;
import net.schwehla.matrosdms.rcp.MatrosNoServerconfigServiceException;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;

// Anleihen von ABUCHEN
@SuppressWarnings("restriction")
@Singleton
public class StartupAddon implements Reciever {
	

	 

	final SplashShell shell = new SplashShell( this); 

    
      
    @Inject
    Logger logger;

	@Inject
	IMatrosServiceService service;
	
	
    @Inject
    @Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS)
    IEclipsePreferences preferences;
    
    
	@Inject
	@Translation
	MatrosMessage messages;
	
    @Inject
    IEventBroker eventBroker;
    
    @Inject
    IApplicationContext appContex;
    
    @Inject    
    IEclipseContext context ;

    
	
    int events;
    
    @Inject
    StartupAddon( IEventBroker eventBroker) {
    	

	 	
        eventBroker.subscribe(UIEvents.ALL_SUB_TOPICS,
                new EventHandler() {
                        @Override
                        public void handleEvent(Event event) {
                        	shell.worked(events++, event);
                        }
        });
        
        
        
    	
    }
    
    private Shell splashShell;

    
    @Inject
	@Optional
	@PostContextCreate
	public void checkSetupComplete(IEclipseContext context, MApplication application,  EModelService modelService ,  Display display) {
	
//    	Platform.getInstanceLocation().isSet();
    	if (context.containsKey("BOOTSHELL")) {
    		
    		Shell s = (Shell) context.get("BOOTSHELL");
    		
    		if (!s.isDisposed()) {
    			
    			
    			shell.setLocation(s.getLocation());
    			shell.setSize(s.getSize());
    			
        		s.setVisible(false);
        		s.dispose();
    		}
    		
    		
    	}
    	
        eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
                new EventHandler() {
                        @Override
                        public void handleEvent(Event event) {
                               shell.close();
                               shell.dispose();
                               eventBroker.unsubscribe(this);

                        }
        		});
        

        	
	 	try {
	     
	 		
	 	 	if (((MatrosServerProxy) Proxy.getInvocationHandler(service)).isDBFileexistent()) {
				logger.info(messages.startupaddon_database_found );
			
							
				if (service.isDatabaseSetupComplete()) {
					
			    	shell.setPersistedState(  preferences.get(MyGlobalConstants.Preferences.LAST_LOGIN, "") , Boolean.parseBoolean( preferences.get(MyGlobalConstants.Preferences.LAST_LOGIN_CHECKBOX, "FALSE")  ));

			    	 shell.forceActive();
			    	shell.activate(SplashShell.COMPONENT_LOGIN);

			    	

			  
			    	shell.runEventLoop();
					 
					// Display main Application
					modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.mainwindowcontainer" , application).setToBeRendered(true);
					modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.mainwindowcontainer" , application).setVisible(true);
					
					modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.welcome" , application).setToBeRendered(false);
					modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.welcome" , application).setVisible(false);
					
	 				  
					// TODO: Log file found
					return; 
		
				}
	 		}  else {
				logger.warn("No Database found");
				
				
		    	shell.activate(SplashShell.COMPONENT_PROGRESS);
		    	
	 		}
			 
	 	} catch (MatrosNoServerconfigServiceException e) {
			logger.warn(e,"no serverconfig.properties found");
		} catch (Exception e) {
			
			if (e.getMessage().contains("The file is locked")) {
				logger.error(e,"Error in Startup: Databasefile is locked!" + e.getMessage());
			} else {
				logger.error(e,"Error in Startup");				
			}
			
		}

		
		// Show the welcome-screen
		modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.mainwindowcontainer" , application).setToBeRendered(false);
		modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.mainwindowcontainer" , application).setVisible(false);
		
		modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.welcome" , application).setToBeRendered(true);
		modelService.find( "net.schwehla.matrosdms.rcp.partsashcontainer.welcome" , application).setVisible(true);
		
	
	}
	
	


    
	public void addContext(MatrosUser u) {

		InfoContext _localContext = new InfoContext(Identifier.createNEW(),  "dropfieldstore" );
		_localContext.init();
		
		context.set(MyEventConstants.ACTIVE_DROPFIELD_CONSTELLATION_CONTEXT, _localContext);
		context.declareModifiable(MyEventConstants.ACTIVE_DROPFIELD_CONSTELLATION_CONTEXT);
		
		if (u != null) {
			
    		context.set(MyEventConstants.ACTIVE_LOGGED_IN_USER, u);
    		context.declareModifiable(MyEventConstants.ACTIVE_LOGGED_IN_USER);
    		
    		
		}
		

	}





	@Override
	public void fireCredentialCheck(MatrosConnectionCredential cred) {

		  
        try {
        	
        	MatrosUser u = service.checkLogin(cred);
        	
        	if (u != null) {
               	((MatrosServerProxy) Proxy.getInvocationHandler(service)).setUser(u);
               	
               	shell.setLoggedIn(true);
            	shell.activate(SplashShell.COMPONENT_PROGRESS);
            	
            	addContext(u);
            	
     
        	} else {
             	shell.updateLoginFeedback("user unknown");
        	}
        		        	
        } catch (MatrosServiceException e) {
        	shell.updateLoginFeedback(e.getMessage());
		}
		
		
	}



	@Override
	public void fireRememberState(String login, boolean checkbox) {
		
        try
        {
            preferences.put(MyGlobalConstants.Preferences.LAST_LOGIN, login);
            preferences.put(MyGlobalConstants.Preferences.LAST_LOGIN_CHECKBOX, Boolean.toString(checkbox));
            preferences.flush();
        }
        catch (BackingStoreException e)
        {
            logger.error(e);
        }

		
	}

	

}
