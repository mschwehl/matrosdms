package net.schwehla.matrosdms.rcp.toolcontrol;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;

import net.schwehla.matrosdms.domain.admin.MatrosUser;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.resourcepool.IMatrosResource;


// http://stackoverflow.com/questions/35063724/how-to-add-perspective-bar-switcher-to-pure-eclipse-4-rcp-application
public class UserToolControl {
	


	 @Inject
	 IMatrosResource poolOfResources;

	@Inject MApplication app;
	
	private Composite comp;

	Control toolParent;
	
	IPropertyChangeListener propertyChangeListener;
	MToolControl psME;

	 ToolBar toolbar ;
	
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

	    Label lblStatustext = new Label(comp, SWT.NONE);
	    
	    if (matrosUser != null) {
	    	lblStatustext.setText(matrosUser.getName());
	    }
	
	}
	
	

		

}