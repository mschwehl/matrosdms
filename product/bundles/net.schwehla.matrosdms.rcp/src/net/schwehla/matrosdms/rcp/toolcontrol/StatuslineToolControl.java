package net.schwehla.matrosdms.rcp.toolcontrol;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;



public class StatuslineToolControl  {
	
	
	// http://omersezer.com/2016/04/01/eclipse-rcp-e4-status-progress-bar-implementation/
	



	@Inject
	MApplication application;

	
	public void  getEvent(@UIEventTopic("STATUSBAR") String message) {
	    updateInterface(message); 
	}
	
	
	@Inject
	private IEventBroker eventBroker;
	  
	ProgressBar progressBar;
	 Label lblNewLabel;

	  @PostConstruct
	  public void postConstruct(final Composite parent) {
	    parent.setLayout(new GridLayout(1, false));
	    
	    Composite composite = new Composite(parent, SWT.NONE);
	    GridData gd_composite = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
	    gd_composite.minimumHeight = 18;
	    composite.setLayoutData(gd_composite);
	    composite.setLayout(new GridLayout(2, false));
	    
	    lblNewLabel = new Label(composite, SWT.NONE);
	    lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    lblNewLabel.setText("Background");
	    
	    progressBar = new ProgressBar(composite, SWT.NONE);
	    progressBar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    
	    
	
		
	}
	  
	  
	  public void updateInterface(String message)
	    {
	        try{
	            Display.getDefault().asyncExec(new Runnable() {
	              @Override
	              public void run() {
	                 try{
	                	 lblNewLabel.setText(message);  
	                    }
	                    catch(Exception exc){
	                        System.out.println(exc);
	                    }               
	              }
	            });
	        }
	        catch(Exception exception){
	            System.out.println(exception);
	        }   
	    }



	  
}