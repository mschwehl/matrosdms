package net.schwehla.matrosdms.rcp.wizzard;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.InfoItemList;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.progress.IProgressConstants;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.wizzard.export.page.ExportWizardFilenamePage;
import net.schwehla.matrosdms.rcp.wizzard.export.page.ExportWizardFilterPage;
import net.schwehla.matrosdms.rcp.wizzard.export.page.ExportWizardPageSummary;
import net.schwehla.matrosdms.rcp.wizzard.model.ExportWizzardData;

// https://wiki.eclipse.org/FAQ_What_is_the_purpose_of_job_families%3F

@Creatable
public class ExportWizzard extends Wizard {

	public static final String ALL_DATABASE = "ALL_DATABASE";
	public static final String CONTEXTLIST 	= "CONTEXTLIST";
	public static final String SELECTION 	= "SELECTION";
	public static final String FULL_BACKUP 	= "FULL_BACKUP";
	
	
	@Inject
	@Optional INotificationService notificationService;
	
	// get UISynchronize injected as field
	@Inject UISynchronize sync;
	
	
	@Inject
	IMatrosServiceService service;

	@Inject  StatusReporter statusReporter;
	
	
	@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell;
	


	@Inject Logger logger;
	
	// must be the constructor, else null
	MatrosMessage messages;
		
	@Inject 
	ExportWizardFilterPage exportFilterPage;
		
	@Inject 
	ExportWizardFilenamePage exportFileAndPatternPage; 
		

	@Inject 
	ExportWizardPageSummary exportSummary;
		

		
	@Inject
	public ExportWizzard(@Translation MatrosMessage messages) {
		super();
		this.messages = messages;
		setWindowTitle(messages.wizzard_user);
		
		setNeedsProgressMonitor(true);

	}
	
	@Override
	public void addPages() {
		addPage(exportFilterPage);
		addPage(exportFileAndPatternPage);
		addPage(exportSummary);
	}

	@Override
	public boolean canFinish() {
	
		if(getContainer().getCurrentPage() == exportSummary ) {
			return true;	
		} else {
			return false;
		}
	}

	@Override
	public boolean performFinish() {
		

			ExportWizzardData data = new ExportWizzardData();

			exportFilterPage.applyDataTo(data);
			exportFileAndPatternPage.applyDataTo(data);
	
			
			executeJob(data);

			
		return true;
	}
	
	
	
	StringBuffer error = new StringBuffer();

	// http://blog.eclipse-tips.com/2008/08/adding-iaction-to-job.html
	private void executeJob(ExportWizzardData data) {


				
		Job job = new Job("export") {

	

			boolean allOk = true;
			
			  
			@Override

			protected IStatus run(IProgressMonitor monitor) {

				
				setProperty(IProgressConstants.KEEPONE_PROPERTY, Boolean.TRUE);
				setProperty(IProgressConstants.NO_IMMEDIATE_ERROR_PROMPT_PROPERTY, true);
				
				
				Action jobAction = new Action("ExportRun" , SWT.UNDERLINE_LINK) {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						// maybe open folder
						super.run();
					}
				};
				
				jobAction.setDescription("Jobresult");
				setProperty(IProgressConstants.ACTION_PROPERTY, jobAction);
			
				List<InfoContext> contextList = null;
				error = new StringBuffer();
				
				try {
					
					 // export
					// XXX
					System.out.println( service.getMetadata() );
					
					 contextList = service.loadInfoContextList(true);
				} catch (MatrosServiceException e1) {
					logger.error(e1, "Cannot load Contextlist");
					return new Status(IStatus.ERROR , "MATROS", "Export job with Error: " + e1);
				}
     			
				//
				
			     SubMonitor subMonitor = SubMonitor.convert(monitor,contextList.size());
		        
		                 try {
		                	 
		                   for (int i=0; i < contextList.size(); i++) {
		                	   
		                	   InfoContext context = contextList.get(i);
		   					   InfoItemList itemlist = service.loadInfoItemList(context, true);
		   					   
		   								
							   for (InfoItem element : itemlist) {
								   
								   try {
									   Path parent = exportElement(data.getPath(), data.getPattern(), context, element);
									   
								   } catch (Exception e) {
									   error.append("Error: " + e + "\n");
									   logger.error(e , "Cannot export " + element.getName());
									   allOk = false;
								   }
								   
		   				
							   }
								
		   					   subMonitor.worked(i);
		                	   
		                   }
		            	 
		                 } catch (Exception e) {
		                	 
		 					logger.error(e, "Cannot export");
							return new Status(IStatus.ERROR , "MATROS", "Export job with Error: " + e);
		                    
		                 } finally {
		      					monitor.done();
		                 }
		                 
		                 if (error.length() > 0) {
		             		Status status = new Status(IStatus.ERROR, "SetupClass", error.toString()); //$NON-NLS-2$
		                    statusReporter.report(status, StatusReporter.LOG );
		                 }

	                	 sync.asyncExec(new Runnable() {
							
							@Override
							public void run() {
								
					
							     if (allOk) {

										NotificationNote note = new NotificationNote();
										note.setHeading("Export finished ok");
										notificationService.openPopup(note);
										
							     } else {
						
										NotificationNote note = new NotificationNote();
										note.setHeading("Export finished with error");
										notificationService.openPopup(note);
							    	 
							     }
							}
						});
	
	                	 
		                 
	     				return allOk ?  new Status(IStatus.OK , "MATROS", "Export job OK") //$NON-NLS-1$ 
	    						:  new Status(IStatus.ERROR , "MATROS", "Export job with Error");//$NON-NLS-1$

				
		

			}


		};
		
		
		job.setPriority(Job.LONG);
		job.setSystem(false);
		job.schedule();

	}

	


	private Path exportElement(String targetpath, String pattern, InfoContext context , InfoItem element) throws Exception {


		// Create or retrieve an engine
		JexlEngine jexl = new JexlBuilder().create();

		// Create an expression
		String jexlExp = pattern;

		// Create a context and add data
		JexlContext jc = new MapContext();
		jc.set("context", context); //$NON-NLS-1$
		jc.set("item", element); //$NON-NLS-1$


		Object evaluatedPath = jexl.createScript(jexlExp).execute(jc);

		// Now evaluate the expression, getting the result
		// Object o = e.evaluate(jc);

		Path p = Paths.get(targetpath + File.separator + evaluatedPath);

		// Creates Folders if not exists
		Files.createDirectories(
				Paths.get(new File(targetpath + File.separator + evaluatedPath).getParent()));

		// same name exists in map -- usually shall not  happen
		int i = 1;
		while (Files.exists(p)) {
			p = Paths.get(p.getParent() + File.separator + ((i++) + "_") + p.getFileName());
		}

		
		try (
			FileInputStream fos = service.getStreamedContent(element.getIdentifier());) {
			
				Files.copy(fos, p , StandardCopyOption.REPLACE_EXISTING);

				return p.getParent();
		} 
		
	}

		

}
