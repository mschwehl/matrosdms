package net.schwehla.matrosdms.rcp.wizzard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.wizard.Wizard;

import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.parts.helper.MatrosPreferenceInbox;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageApplicationSettings;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageInbox;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageInfoItemOriginalstore;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageServerCloudstore;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageSummary;

@Creatable
public class OpenExistingWizzard extends Wizard   {


// must be the constructor, else null
	MatrosMessage messages;

	

	@Inject 
	SetupPageInbox inboxpage; 
	
	@Inject 
	SetupPageSummary summarypage;
	
	
	@Inject 
	SetupPageInfoItemOriginalstore originalstorepage; 
	
	@Inject 
	SetupPageApplicationSettings serverdatabasepage; 
	
	@Inject 
	SetupPageServerCloudstore documentstorepage; 
	

	@Inject
	IEclipseContext context;
	
	@Inject
	IMatrosServiceService service;

	
	@Inject
	OpenExistingWorker workClass ;
	

	@Inject Logger logger;
	

	
	@Inject
	public OpenExistingWizzard(@Translation MatrosMessage messages
			, Masterdata masterData
			, @Preference(nodePath =  MyGlobalConstants.Preferences.NODE_COM_MATROSDMS) IEclipsePreferences prefs) {
		
		super();
		this.messages = messages;
		setWindowTitle(messages.wizzard_user);
		
		setNeedsProgressMonitor(true);
		
		masterData.getInboxList().clear();
		
		
		String inbox =  prefs.get(MyGlobalConstants.Preferences.INBOX_PATH, "" );
		String[] inboxArray = inbox.split(MyGlobalConstants.Preferences.DELIMITER);
		
		try {
			
			for (String tmp : inboxArray) {
				
				File f = new File(tmp);
				if (f.exists() && f.isDirectory()) {
					MatrosPreferenceInbox mpi = new MatrosPreferenceInbox();
					mpi.setPath( f.getCanonicalPath());
					masterData.getInboxList().add(mpi);
				}
				
			}
			
		} catch (Exception e) {
			logger.error(e,"inbox " + inbox);
		}
		
		

		
	}
	
	@Override
	public void addPages() {
		
// client settings
		addPage(inboxpage);
		
		addPage(serverdatabasepage); 
		addPage(documentstorepage); 
	   	
	   	addPage(summarypage);
		
	}
	


	@Override
	public boolean performFinish() {
		
		// store the inbox
		
		// todo: Cancel soll modell nicht �ndern -> listerner !?
		
		
		try {
			
			getContainer().run(true, true, workClass);
			executeWithSuccess = true;
			
			return true;

			
		} catch (InvocationTargetException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		
		return false;
	}
	
	

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}

	@Override
	public boolean canFinish() {
	
		if(getContainer().getCurrentPage() == summarypage) {
			return true;	
		} else {
			return false;
		}
	}


	boolean executeWithSuccess = false;
	boolean isExecuteWithSuccess() {
		return executeWithSuccess;
	}


	
}
