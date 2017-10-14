package net.schwehla.matrosdms.rcp.wizzard;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import net.schwehla.matrosdms.domain.admin.E_CLOUDCRYPTION;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageApplicationSettings;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageInbox;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageInfoItemOriginalstore;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageProcessedFilesFolder;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageServerCloudstore;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageServerCryptExternal;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageServerUser;
import net.schwehla.matrosdms.rcp.wizzard.setup.page.SetupPageSummary;

@Creatable
public class SetupNewWizzard extends Wizard   {

	
	@Inject
	IEclipseContext context;
	
// must be the constructor, else null
	MatrosMessage messages;

	@Inject 
	SetupPageServerUser userpage; 
	
	@Inject 
	SetupPageInbox inboxpage; 
	
	@Inject 
	SetupPageProcessedFilesFolder setupPageProcessedFilesFolder;
	
	@Inject 
	SetupPageSummary summarypage;
	
	@Inject 
	SetupPageInfoItemOriginalstore originalstorepage; 
	
	@Inject 
	SetupPageApplicationSettings applicationSettingsPage; 
	
	@Inject 
	SetupPageServerCloudstore cloudStorePage; 
	
	@Inject 
	SetupPageServerCryptExternal setupPageServerCryptExternal;
	
	

	
	@Inject
	IMatrosServiceService service;
	
	@Inject
	Masterdata masterData;
	
	@Inject
	SetupNewWorker workClass ;
	
	@Inject Logger logger;
	
	@Inject
	public SetupNewWizzard(@Translation MatrosMessage messages) {
		super();
		this.messages = messages;
		setWindowTitle(messages.wizzard_user);
		
		setNeedsProgressMonitor(true);
		
		// registering masterdata for detail-pages
		
	}
	
	@Override
	public void addPages() {
		

		// APP-PATH
		addPage(applicationSettingsPage); 
		
		// Cloud-PATH
		addPage(cloudStorePage); 
		
		// 7zip and others
		addPage(setupPageServerCryptExternal); 
		
		// INBOX
		addPage(inboxpage);
		
		// PROCESSED-FILES
		addPage(setupPageProcessedFilesFolder);
				
		// USER
		addPage(userpage);
		
		// ORIGINALSTORE
		addPage(originalstorepage); 
		
		// PRINT AND SUMMARY
	   	addPage(summarypage);

		
	}
	
	
	
	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		
	    if (cloudStorePage == currentPage) {
	    	
	    	if ( masterData.getCloudSettings().getCryptSettings().getCryption() == E_CLOUDCRYPTION.EXTERNAL) {
		       return setupPageServerCryptExternal;
		    } else {
		    	return inboxpage;
		    }
	    	
	    }

	    return super.getNextPage(currentPage);
	}
	
	


	@Override
	public boolean performFinish() {
		
		// store the inbox
		
		// XXX: cancel shall not modify inbox
		
		
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
