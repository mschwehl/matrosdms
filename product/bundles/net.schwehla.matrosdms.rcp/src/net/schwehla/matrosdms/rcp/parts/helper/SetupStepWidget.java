package net.schwehla.matrosdms.rcp.parts.helper;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

@Creatable
public class SetupStepWidget extends Composite {

	@Inject
	IEclipseContext context;
	
	@Inject
	@Named(IServiceConstants.ACTIVE_SHELL) Shell  shell;

	
	SetupStepWizzard wizz;
	

	// needed for e4 ContextInjectionFactory.make
	@Inject
	public SetupStepWidget(Composite parent) {
		super(parent, SWT.NONE);
	}
	



	public void init( String label, String subtext , Class  <? extends IWizard> wizzard ) {
	
	
        GridLayoutFactory.swtDefaults().numColumns(1).applyTo(this);

        
        GridData compositeTypeGridLayout = new GridData();
		compositeTypeGridLayout.verticalAlignment = SWT.TOP;
		compositeTypeGridLayout.horizontalAlignment = SWT.FILL;
		compositeTypeGridLayout.grabExcessHorizontalSpace = true;
		compositeTypeGridLayout.grabExcessVerticalSpace = true;
		compositeTypeGridLayout.minimumHeight  = 50;
		
		this.setLayoutData(compositeTypeGridLayout);
       
        
        Link link = new Link(this, SWT.UNDERLINE_LINK);
        link.setText("<a>" + label + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
        link.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	activate();
            }
        });

        Label description = new Label(this, SWT.WRAP);
        description.setText(subtext);
        
        link.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
        description.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));	
		
    //    GridDataFactory.fillDefaults().grab(true, false).applyTo(description);
        
        registerWizzard(wizzard);

		
	}

	private void registerWizzard( Class <? extends IWizard> wizzard) {
		
//        IEclipseContext tempContextMessages = EclipseContextFactory.create();
//        tempContextMessages.set(Shell.class, shell, tempContextMessages);
// 

		IWizard wizzInstance =   ContextInjectionFactory.make(wizzard, context);
		
		
        IEclipseContext tempContext = EclipseContextFactory.create();
        tempContext.set(Shell.class, shell);
        tempContext.set(IWizard.class, wizzInstance);

        
        wizz = (SetupStepWizzard) ContextInjectionFactory.make(SetupStepWizzard.class, context, tempContext);


		
	}
	
	
	 class SetupStepWizzard extends WizardDialog {

		 // needed that E4 finds the constructor
		 @Inject
		public SetupStepWizzard(Shell parentShell, IWizard newWizard) {
			super(parentShell, newWizard);
			// TODO Auto-generated constructor stub
		}
		
	}

	
	public void activate() {
        wizz.open();
	}


}
