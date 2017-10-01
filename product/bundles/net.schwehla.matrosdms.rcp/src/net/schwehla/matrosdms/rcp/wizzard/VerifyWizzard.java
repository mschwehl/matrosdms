package net.schwehla.matrosdms.rcp.wizzard;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.util.VerifyMessage;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.progress.IProgressConstants;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.wizzard.resultwriter.HtmlVerifyResultWriter;
import net.schwehla.matrosdms.rcp.wizzard.verify.page.VerifyWizardPage;

@Creatable
public class VerifyWizzard extends Wizard {

	public static final String CONTENT 			= "CONTENT";
	public static final String FILESIZE_ONLY 	= "FILESIZE_ONLY";


	@Inject
	@Optional INotificationService notificationService;
	

	@Inject Logger logger;
	

	
	// must be the constructor, else null
	MatrosMessage messages;


	
	@Inject 
	VerifyWizardPage verifypage;
		
		
	@Inject
	public VerifyWizzard(@Translation MatrosMessage messages) {
		super();
		this.messages = messages;
		setWindowTitle(messages.wizzard_user);
		
		setNeedsProgressMonitor(true);
		
		// registering masterdata for detail-pages
		
	}
	
	@Override
	public void addPages() {
		addPage(verifypage);
	}
	

	
	@Inject
	HtmlVerifyResultWriter htmlWriter;


		

	@Override
	public boolean performFinish() {
		


			VerifyMessage data = new VerifyMessage();

			verifypage.applyDataTo(data);
	
			
			
			executeJob(data);

			
			return true;


	}
		
	
	// get UISynchronize injected as field
	@Inject UISynchronize sync;
	
	
	@Inject
	IMatrosServiceService service;


	
	
	@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell;
	
	
	
	// http://blog.eclipse-tips.com/2008/08/adding-iaction-to-job.html
	private void executeJob(VerifyMessage data) {

		
				
		Job job = new Job("verify") {

			// https://wiki.eclipse.org/FAQ_What_is_the_purpose_of_job_families%3F
			
			boolean allOk = false;
			
			  
			@Override

			protected IStatus run(IProgressMonitor monitor) {

				
				Action a = new Action("Verifyjob " + System.currentTimeMillis()) {
				    @Override
				    public void run() {
				    	
				        MessageDialog.openInformation(new Shell(), "Job Status", "Some partial results processed can be displayed here");    
				    }
			    };
			    
			   a.setToolTipText("TODO"); 
				
//				setProperty(IProgressConstants.KEEPONE_PROPERTY, Boolean.TRUE);
				setProperty(IProgressConstants.NO_IMMEDIATE_ERROR_PROMPT_PROPERTY, true);
				setProperty(IProgressConstants.ACTION_PROPERTY, a );
			
			
			 

				// If you want to update the UI

				sync.asyncExec(new Runnable() {

					@Override

					public void run() {

				

							// https://github.com/alblue/com.packtpub.e4/blob/master/com.packtpub.e4.clock.ui/src/com/packtpub/e4/clock/ui/handlers/HelloHandler.java
						     SubMonitor subMonitor = SubMonitor.convert(monitor,7);
						     
						     
						     subMonitor.beginTask("My job is working...", 100);
					        
					                 try {
					                	 
					                	
					         				allOk = service.verifyDatabase(data);
					         				
					         			} catch (MatrosServiceException e2) {
					         				logger.error(e2);
					         			 } catch (Exception e) {
					                		Status status = new Status(IStatus.ERROR, "Verify", "Programming bug?", e); //$NON-NLS-2$
						                 } finally {
						      					monitor.done();
						                 }

					            
					                	 sync.asyncExec(new Runnable() {
											
											@Override
											public void run() {
												
												
							
									
											     if (allOk) {

														NotificationNote note = new NotificationNote();
														note.setHeading("Verify finished ok");
														notificationService.openPopup(note);
														
											     } else {
										
														NotificationNote note = new NotificationNote();
														note.setHeading("Verify finished with error");
														notificationService.openPopup(note);
											    	 
											     }
											}
										});
					

					}

				});

			
			
				return allOk ?  new Status(IStatus.OK , "MATROS", "Job OK") //$NON-NLS-1$ 
						:  new Status(IStatus.ERROR , "MATROS", "Job JOB_WITH_ERROR");//$NON-NLS-1$
		

			}

		};
		
		
		job.setPriority(Job.LONG);
		job.setSystem(false);
		job.schedule();

	}



}
