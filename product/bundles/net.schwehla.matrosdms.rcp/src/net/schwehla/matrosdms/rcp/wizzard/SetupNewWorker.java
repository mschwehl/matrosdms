package net.schwehla.matrosdms.rcp.wizzard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.core.idm.MatrosUser;
import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.persistenceservice.internal.MatrosServerProxy;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.parts.helper.MatrosPreferenceInbox;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;

@Creatable
@Singleton
public class SetupNewWorker implements IRunnableWithProgress {

	@Inject
	IMatrosServiceService service;
	
	@Inject Masterdata masterData;
	
	@Inject  StatusReporter statusReporter;
	
	@Inject
	@Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS) 
	IEclipsePreferences preferences ;

	
	@Inject Logger logger;
	 
	// get UISynchronize injected as field
	@Inject UISynchronize sync;
	
	
	@Inject IWorkbench workbench;
	
	
	@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell;

	boolean error;

	 
	@Inject
	@Translation
	MatrosMessage messages;
		
    private static final String FORCE_CLEAR_PERSISTED_STATE = "model.forceClearPersistedState"; //$NON-NLS-1$
	  
	 
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		
		try {
			preferences.put(FORCE_CLEAR_PERSISTED_STATE, Boolean.TRUE.toString());
			preferences.flush();
		} catch (BackingStoreException e1) {
			logger.error(e1);
		}
	    
		
		// https://github.com/alblue/com.packtpub.e4/blob/master/com.packtpub.e4.clock.ui/src/com/packtpub/e4/clock/ui/handlers/HelloHandler.java
	     SubMonitor subMonitor = SubMonitor.convert(monitor,7);
        
                 try {
                	 
                     // sleep a second
            		 subMonitor.subTask(messages.setupworker_saving_inbox);
            	 	 Thread.sleep(500);
            	 	  	 
            	     sync.syncExec(new Runnable() {
                         @Override
                         public void run() {
                             
                          	 String inboxPath = masterData.getInboxList().stream().map(MatrosPreferenceInbox::getPath)
                          			 .collect(Collectors.joining(MyGlobalConstants.Preferences.DELIMITER));
                          	 
                          	preferences.put(MyGlobalConstants.Preferences.INBOX_PATH, inboxPath);
                          	
                       
                          	try {
								preferences.flush();
							} catch (BackingStoreException e1) {
								logger.error(e1);
							}
                       
                        
                         }
            	     });
            	     
            	  	subMonitor.worked(1);
             	 	 
            	  //--------------------------------------------------      
            	  	
            	  
            	  	
            	    // sleep a second
           		 subMonitor.subTask(messages.setupworker_saving_processed_folder);
           	 	 Thread.sleep(500);
           	 	  	 
           	     sync.syncExec(new Runnable() {
                        @Override
                        public void run() {
                            
                         	String processedFolder = masterData.getAppSettings().getProcessedFolder();
                         	preferences.put(MyGlobalConstants.Preferences.PROCESSED_PATH, processedFolder);
                         	
                         	try {
								preferences.flush();
							} catch (BackingStoreException e1) {
								logger.error(e1);
							}
                      
                       
                        }
           	     });
           	     
           	  	subMonitor.worked(1);
           	  	
           	  	
           	  	
            	  	
                    // sleep a second
           		 subMonitor.subTask(messages.setupworker_create_database);
           		 
           		 
           	 	 Thread.sleep(500);
           	 	 
           	 	 
           	    ((MatrosServerProxy) Proxy.getInvocationHandler(service)).createAppDirectoryAndSubdirs(masterData.getAppSettings());
           	    
           	    
					MatrosConnectionCredential dbCredentials = new MatrosConnectionCredential();
					
					dbCredentials.setDbPath(masterData.getAppSettings().getDbPath());
					dbCredentials.setDbPasswd(masterData.getDbConnection().getDbPasswd());	
					dbCredentials.setDbUser(masterData.getDbConnection().getDbUser());
			
				 	((MatrosServerProxy) Proxy.getInvocationHandler(service)).updateConfigIniFileProperties( masterData.getAppSettings().getAppPath(),  dbCredentials );
				    ((MatrosServerProxy) Proxy.getInvocationHandler(service)).createDatabaseFile(dbCredentials);
            	     
              
            	   	subMonitor.worked(1);
            	     
            	  //--------------------------------------------------      
            	  	
            	  	
                    // sleep a second
           		 subMonitor.subTask(messages.setupworker_create_tables);
           	 	 Thread.sleep(500);
            	  	
           	 	 logger.info("Database created ");
         	    ((MatrosServerProxy) Proxy.getInvocationHandler(service)).createEmptyDatabase();

         	    
          		 subMonitor.subTask(messages.setupworker_create_initalValues);
           	 	 Thread.sleep(500);

           	 	 
           	 	 
           	 	 service.setupInitialCategories();
       	   
            	   	subMonitor.worked(1);
            	     
            	  //--------------------------------------------------      
            	  	
                    // sleep a second
           		 subMonitor.subTask(messages.setupworker_create_config);
           	 	 Thread.sleep(500);

           	 	 	// Stores info in Table CONFIG
    	 	   	    service.updateCloudSettings(masterData.getCloudSettings());
            	   	subMonitor.worked(1);
            	     
            	  //--------------------------------------------------      
            	  	
            	  	
                    // sleep a second
           		 subMonitor.subTask(messages.setupworker_create_documentstore_filesystem);
           	 	 Thread.sleep(500);
           	 	 
           	 	 
          	    ((MatrosServerProxy) Proxy.getInvocationHandler(service)).createCloudDirectoryAndSubdirs(masterData.getCloudSettings());
           	 	 
        
            	   	subMonitor.worked(1);
            	     
            	  //--------------------------------------------------      
            	   	
            	   	
            	  	
                    // sleep a second
           		 subMonitor.subTask(messages.setupworker_create_user);
           	 	 Thread.sleep(500);
           	 	 
           	 	 
            	     for (MatrosUser user: masterData.getUserList()) {
            	    	 
            	    	 service.createUser(user);
            	    	 logger.info("User " +  user.getName() + " created ");
            	    	 
            	     }

            	 	 subMonitor.worked(1);
            	 	  	
            	 	 
                     // sleep a second
               		 subMonitor.subTask(messages.setupworker_create_originalstore);
               	 	 Thread.sleep(500);
                	 
               	 	 for (InfoOrginalstore store:  masterData.getOrignalStoreList()) {
               	 		
               	   	 	 service.createOriginalStore(store);
               	    	 logger.info("OrignalStore " + store.getName() + " created ");
               	   	 	 
               	 	 }
               	 	 
//--------------------------------------------------            	 	  	
            	 
                	 	 
                	 
                 } catch (Exception e) {
                	 
                		Status status = new Status(IStatus.ERROR, "SetupClass", "Programming bug?", e); //$NON-NLS-2$
                		statusReporter.report(status, StatusReporter.LOG );
                		
                		error = true;
                    
                 } finally {
      					monitor.done();
      			        System.setProperty(IWorkbench.CLEAR_PERSISTED_STATE, Boolean.TRUE.toString());
                 }
                 
                 
                 sync.asyncExec(new Runnable() {

						@Override
						public void run() {
							boolean restart = MessageDialog.openQuestion(activeShell, "Setup executed, restart?",
									"Setup have been executed. Do you want to restart?");
							if (restart) {
								workbench.restart();
							}
						}
					});

                

	}

}
