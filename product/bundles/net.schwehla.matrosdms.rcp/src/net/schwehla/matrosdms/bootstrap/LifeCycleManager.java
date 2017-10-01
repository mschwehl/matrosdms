package net.schwehla.matrosdms.bootstrap;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.Selector;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;

import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;


/**
 * 
 * @author Martin
 *
 */
public class LifeCycleManager {
	
	  private static final String MODEL_VERSION = "model.version"; //$NON-NLS-1$
	    private static final String FORCE_CLEAR_PERSISTED_STATE = "model.forceClearPersistedState"; //$NON-NLS-1$
	    
	    @Inject
	    Logger logger;

	    @Inject
	    @Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS)
	    IEclipsePreferences preferences;

	    
        

	    
	    // no access to Matrosservice ... why ?

		@PreSave
		public void doPreSave(MApplication app, EPartService partService, EModelService modelService, IEclipseContext context) {
		        saveModelVersion();
		        removeMartosPartsWithoutPersistedFile(app, partService, modelService);
		        
		        // https://groups.google.com/forum/#!topic/h2-database/PjzSa1HM28c
		        
		        IMatrosServiceService service = context.get(IMatrosServiceService.class);
		        
		        try {
					service.backup();
				} catch (MatrosServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        /*
		        Shutdown, backing up database
		        2017-04-14 20:10:10 database: closing C:/temp/db/matrosdms from shutdown hook
		        2017-04-14 20:10:10 database: disconnecting session #3
		        2017-04-14 20:10:10 database: disconnected session #3
		        2017-04-14 20:10:10 database: closing C:/temp/db/matrosdms
		        2017-04-14 20:10:10 database: closed

		        */
		        
		        
		}
	
	
		  private void removeMartosPartsWithoutPersistedFile(MApplication app, EPartService partService,
				EModelService modelService) {
			  
			  

		        List<MPart> parts = modelService.findElements(app, MPart.class, EModelService.IN_ANY_PERSPECTIVE,
		                        new Selector()
		                        {
		                            @Override
		                            public boolean select(MApplicationElement element)
		                            {
		                            	if (element.getTags() != null) {
		                            		
		                            		return element.getTags().stream()
		                            	            .anyMatch(t -> t.equals(MyGlobalConstants.TAG_REMOVEONCLOSE));
		                            	}
		                            	
		                            	return  false;
		                            
		                            }
		                        });

		        for (MPart part : parts)
		        {
		            MElementContainer<MUIElement> parent = part.getParent();

		            if (parent != null) {
		                
			            if (parent.getSelectedElement().equals(part)) {
			                parent.setSelectedElement(null);			            	
			            }
			            parent.getChildren().remove(part);
		            }
		        
		        }
		    
			
		}


		@PostContextCreate
		    public void doPostContextCreate(IEclipseContext context) 
		    {
			
//			Platform.getInstanceLocation().isSet();
			
			// http://www.vogella.com/tutorials/EclipsePreferences/article.html#workspace-location
			


//				
//			
//	
            try {
	            	
			Shell s = new Shell(Display. getDefault(), SWT.TOOL | SWT.NO_TRIM  );
			
			MatrosSplash sp = new MatrosSplash(s);
	    			
	    			
	    			context.set("BOOTSHELL",  s);
	 		        
	    			sp.show();
	 		        
	            	
//	                // check if the instance location is already set,
//	    	        // otherwise setting another one will throw an IllegalStateException
	    	        if (!Platform.getInstanceLocation().isSet()) {
	    	            String defaultPath = System.getProperty("user.home");
//
	    	            // build the desired path for the workspace
	    	            String path = defaultPath + "/" + "xxx" + "/workspace/";
	    	            URL instanceLocationUrl = new URL("file", null, path);
//	            	
					Platform.getInstanceLocation().set(instanceLocationUrl, false);
//					
	    	        }
	    	        
//	    	        
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//	     
//	        
	        
	        
			
		        checkForJava8();
		        removeClearPersistedStateFlag();
		        checkForModelChanges();
		        checkForRequestToClearPersistedState();

		        
// XXX beizeiten schauen wie ich richtig bootstappen kann... laden von Lucene dauert beim ersten aufruf :-(
		        
//		        BundleContext bundleContext = FrameworkUtil.getBundle( ILuceneService.class).getBundleContext();
//
//			     ServiceReference reference = bundleContext.getServiceReference( ILuceneService.class.getName() );
//			     ILuceneService service = (ILuceneService) bundleContext.getService( reference );
//			     
//			     service.bootstrap();

			     
			    
		  // ???      setupEventLoopAdvisor(context);
		    }
		
		
		  
			
			

		private void saveModelVersion()
		{
		    String modelVersion = preferences.get(MODEL_VERSION, Version.emptyVersion.toString());
		    String programVersion = FrameworkUtil.getBundle(this.getClass()).getVersion().toString();
		
		    if (!modelVersion.equals(programVersion))
		    {
		        try
		        {
		            preferences.put(MODEL_VERSION, programVersion);
		            preferences.flush();
		        }
		        catch (BackingStoreException e)
		        {
		            logger.error(e);
		        }
		    }
		}
		
		
		/**
		 * If no Database is present then show the Wizzard
		 * 
		 * 
		 * @param context
		 * @param application
		 * @param modelService
		 */
		
		   private void checkForJava8()
		    {
		        // if the java version is < 8, show a message dialog because otherwise
		        // the application would silently not start
		
		        double version = Double.parseDouble(System.getProperty("java.specification.version")); 
		
		        if (version < 1.8)
		        {
		            MessageDialog.openInformation(Display.getDefault().getActiveShell(),"Java 8",
		                            ">= Java 8");
		            throw new UnsupportedOperationException("The minimum Java version required is Java 8"); 
		        }
		    }



			private void removeClearPersistedStateFlag()
			{
			    // the 'old' update mechanism edited the ini file *after* the upgrade
			    // and added the -clearPersistedState flag. The current mechanism does
			    // not need it, hence it must be remove if present
			
			    // not applicable on Mac OS X because only update is not supported
			    if (org.eclipse.core.runtime.Platform.OS_MACOSX.equals(org.eclipse.core.runtime.Platform.getOS()))
			        return;
			
			    try
			    {
			        IniFileManipulator iniFile = new IniFileManipulator();
			        iniFile.load();
			        iniFile.unsetClearPersistedState();
			        if (iniFile.isDirty())
			            iniFile.save();
			    }
			    catch (IOException ignore)
			    {
			        // ignore: in production, it will anyway be removed during the next
			        // update; in development, it will annoy to always report this error
			    }
			}


			private void checkForModelChanges()
			{
			    Version modelVersion = Version.parseVersion(preferences.get(MODEL_VERSION, null));
			    Version programVersion = FrameworkUtil.getBundle(this.getClass()).getVersion();
			
			    if (!modelVersion.equals(programVersion))
			    {
			        logger.info(MessageFormat.format(
			                        "Detected model change from version {0} to version {1}; clearing persisted state", //$NON-NLS-1$
			                        modelVersion.toString(), programVersion.toString()));
			        
			        System.setProperty(IWorkbench.CLEAR_PERSISTED_STATE, Boolean.TRUE.toString());
			    }
			}


			private void checkForRequestToClearPersistedState()
			{
			    boolean forceClearPersistedState = Boolean
			                    .parseBoolean(preferences.get(FORCE_CLEAR_PERSISTED_STATE, Boolean.FALSE.toString()));
			
			    if (forceClearPersistedState)
			    {
			        logger.info(MessageFormat.format("Clearing persisted state due to ''{0}=true''", //$NON-NLS-1$
			                        FORCE_CLEAR_PERSISTED_STATE));
			        System.setProperty(IWorkbench.CLEAR_PERSISTED_STATE, Boolean.TRUE.toString());
			
			        try
			        {
			            preferences.remove(FORCE_CLEAR_PERSISTED_STATE);
			            preferences.flush();
			        }
			        catch (BackingStoreException e)
			        {
			            logger.error(e);
			        }
			    }
			}

			


			    @ProcessRemovals
			    public void removeDnDAddon(MApplication app)
			    {
			        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=394231#c3
			        for (MAddon addon : new ArrayList<MAddon>(app.getAddons()))
			        {
			            String contributionURI = addon.getContributionURI();
			            if (contributionURI.contains("ui.workbench.addons.minmax.MinMaxAddon") //$NON-NLS-1$
			                            || contributionURI.contains("ui.workbench.addons.splitteraddon.SplitterAddon")) //$NON-NLS-1$
			            {
			                app.getAddons().remove(addon);
			            }
			        }
			    }
		   
	
		   
}
