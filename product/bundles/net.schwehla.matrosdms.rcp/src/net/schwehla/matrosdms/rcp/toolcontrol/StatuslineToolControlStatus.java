package net.schwehla.matrosdms.rcp.toolcontrol;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.SideValue;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import net.schwehla.matrosdms.domain.core.idm.MatrosUser;
import net.schwehla.matrosdms.rcp.MyEventConstants;



public class StatuslineToolControlStatus  {
	
	
	// http://omersezer.com/2016/04/01/eclipse-rcp-e4-status-progress-bar-implementation/
	

	@Inject Logger logger;


	@Inject
	MApplication application;
	
	private Composite comp;

	Control toolParent;
	
	IPropertyChangeListener propertyChangeListener;
	MToolControl psME;


	
	  Label lblStatustext;
	  
	@PostConstruct
	void createWidget(IEclipseContext context, Composite parent, MToolControl toolControl, 
			@Optional
			@Named(MyEventConstants.ACTIVE_LOGGED_IN_USER) MatrosUser matrosUser) {
		psME = toolControl;
		MUIElement meParent = psME.getParent();
		
		int orientation = SWT.HORIZONTAL;
		if (meParent instanceof MTrimBar) {
			MTrimBar bar = (MTrimBar) meParent;
			if (bar.getSide() == SideValue.RIGHT || bar.getSide() == SideValue.LEFT)
				orientation = SWT.VERTICAL;
		}
		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout(orientation));

	    lblStatustext = new Label(comp, SWT.BORDER | SWT.SHADOW_NONE  );
	    
	    if (matrosUser != null) {
		    lblStatustext.setText(matrosUser.getName());
	    }
	    

	    

	}
	
	

		




	@Inject
	@Optional
	public void  getEvent(@UIEventTopic("STATUSBAR") String message) {
	    updateInterface(message); 
	}


	  
	  public void updateInterface(String message)
	    {
	        try{
	            Display.getDefault().asyncExec(new Runnable() {
	              @Override
	              public void run() {
	                 try{
	                	 lblStatustext.setText(message);  
	                	 lblStatustext.getParent().layout(true, true); 
	                    }
	                    catch(Exception exc){
	        	        	logger.error(exc);
	                    }               
	              }
	            });
	        }
	        catch(Exception exception){
	        	logger.error(message);
	        }   
	    }



	  
}