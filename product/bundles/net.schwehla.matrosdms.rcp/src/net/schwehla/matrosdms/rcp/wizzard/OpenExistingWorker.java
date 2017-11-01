package net.schwehla.matrosdms.rcp.wizzard;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
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
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.operation.IRunnableWithProgress;

import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;

@Creatable
@Singleton
public class OpenExistingWorker implements IRunnableWithProgress {

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

	boolean error;

	 
	@Inject
	@Translation
	MatrosMessage messages;
		
    private static final String FORCE_CLEAR_PERSISTED_STATE = "model.forceClearPersistedState"; //$NON-NLS-1$
	  
	 
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		
		// https://github.com/alblue/com.packtpub.e4/blob/master/com.packtpub.e4.clock.ui/src/com/packtpub/e4/clock/ui/handlers/HelloHandler.java
	     SubMonitor subMonitor = SubMonitor.convert(monitor,3);
        
                 try {
                	 
                     // sleep a second
            		 subMonitor.subTask("Reading Metadata");
            	 	 Thread.sleep(500);
            	 	  	 
            	 	 // parsing xml
            	     
            	  	subMonitor.worked(1);
             	 	 
            	  //--------------------------------------------------      
            	  	
            	  // XXX check if database exists on this place	
            	  	
                    // sleep a second
           		 subMonitor.subTask("TASK2");
           		 
           		 
           	 	 Thread.sleep(500);
            	  	
				
            	     
              
            	   	subMonitor.worked(1);
            	     
            	  //--------------------------------------------------      
            	  	
     
            	  	
            	   	
            	  //--------------------------------------------------      
            	  	
            	  	
                    // sleep a second
           		 subMonitor.subTask("TASK 3");
           	 	 Thread.sleep(500);
           	 	 
           	 	 // XXX
            	 
//           	 	 for (ScannedFilesDirectory store:  masterData.getScannedFilesDirectory()) {
//           	 		
//           	   	 	 service.setScannedFilesDirectory(store);
//           	    	 logger.info("ScannedFilesDirectory " + store.getName() + " created ");
//           	   	 	 
//           	 	 }
//        	 	 
        
            	   	subMonitor.worked(1);
            	     
            	  //--------------------------------------------------      
            	   	
            	   	
            	  
               	 	 
//--------------------------------------------------            	 	  	
            	 
                	 	 
                	 
                 } catch (Exception e) {
                	 
                		Status status = new Status(IStatus.ERROR, "SetupExisting", "Programming bug?", e); //$NON-NLS-2$
                		statusReporter.report(status, StatusReporter.LOG );
                		
                		error = true;
                    
                 } finally {
      					monitor.done();
      			        System.setProperty(IWorkbench.CLEAR_PERSISTED_STATE, Boolean.TRUE.toString());
                 }


	}

}
