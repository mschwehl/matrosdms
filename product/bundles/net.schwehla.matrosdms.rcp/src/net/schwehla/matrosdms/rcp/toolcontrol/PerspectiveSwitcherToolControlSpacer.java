package net.schwehla.matrosdms.rcp.toolcontrol;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;


// http://stackoverflow.com/questions/35063724/how-to-add-perspective-bar-switcher-to-pure-eclipse-4-rcp-application
public class PerspectiveSwitcherToolControlSpacer {
	

		  @PostConstruct
		  public void postConstruct(final Composite parent)
		  {
		    Composite body = new Composite(parent, SWT.NONE);

		    body.setLayout(new FillLayout());
		  }
		  
		

}