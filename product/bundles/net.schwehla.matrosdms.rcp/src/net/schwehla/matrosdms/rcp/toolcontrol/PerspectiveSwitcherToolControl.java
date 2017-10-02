package net.schwehla.matrosdms.rcp.toolcontrol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.SideValue;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import net.schwehla.matrosdms.resourcepool.IMatrosResource;


public class PerspectiveSwitcherToolControl {
	



	@Inject
	private ECommandService commandService;
	
	@Inject
	private EHandlerService handlerService;

	@Inject
	private EModelService modelService;

	
	 @Inject
	 IMatrosResource poolOfResources;

	@Inject MApplication app;
	
	private Composite comp;

	Control toolParent;
	
	IPropertyChangeListener propertyChangeListener;
	MToolControl psME;

	 ToolBar toolbar ;
	
	@PostConstruct
	void createWidget(IEclipseContext context, Composite parent, MToolControl toolControl) {
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

		
		List<MPerspectiveStack> appPerspectiveStacks = modelService.findElements(app, null, MPerspectiveStack.class, null);

		toolbar = new ToolBar(parent,orientation);


	     
	        
		
		if (appPerspectiveStacks.size() > 0) {
			for (MPerspectiveStack stack : appPerspectiveStacks)
				for (MPerspective perspective : stack.getChildren()) {
					if (perspective.isToBeRendered()) {
						
						ToolItem tmp  = new ToolItem(toolbar, SWT.RADIO );
						tmp.setText( perspective.getLabel() );
						
						tmp.setImage(poolOfResources.getImage(IMatrosResource.Images.NOTE));
				
						tmp.setData(perspective.getElementId() );
						
						
						tmp.addSelectionListener(new SelectionAdapter()
				        {
				            @Override
				            public void widgetSelected(SelectionEvent e)
				            {
				            	

				            	Map<String, Object> parameters = new HashMap<>(1);
								parameters.put("net.schwehla.matrosdms.rcp.commandparameter.id", tmp.getData().toString());
								
								ParameterizedCommand command = commandService.createCommand("net.schwehla.matrosdms.rcp.command.switchperspectiveCommand",
										parameters);
								handlerService.executeHandler(command);
				    		  
				            }
				        });

					}
				}
		}

	}
	
	
}