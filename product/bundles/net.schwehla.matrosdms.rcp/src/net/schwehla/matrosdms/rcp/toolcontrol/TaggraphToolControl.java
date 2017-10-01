package net.schwehla.matrosdms.rcp.toolcontrol;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;



public class TaggraphToolControl {
	
	
	// http://stackoverflow.com/questions/29107514/is-there-a-better-way-to-get-to-workbench-context-in-an-e4-application
	
	@Inject
	MApplication application;

	
	@Inject
	private IEventBroker eventBroker;
	  

	
	  @PostConstruct
	  public void postConstruct(final Composite parent) {
	    parent.setLayout(new GridLayout(1, false));
		
	    
	
		
	}
}