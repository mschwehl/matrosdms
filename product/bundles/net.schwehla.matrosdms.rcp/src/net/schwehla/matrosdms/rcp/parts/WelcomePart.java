 
package net.schwehla.matrosdms.rcp.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.wizzard.OpenExistingWizzard;
import net.schwehla.matrosdms.rcp.wizzard.SetupNewWizzard;
import net.schwehla.matrosdms.resourcepool.IMatrosResource;

public class WelcomePart {

	
//	
	 @Inject
	 IMatrosResource poolOfResources;


	
	@Inject IMatrosServiceService service;
	
	@Inject IEclipseContext context;
	
	@Inject
	private IEventBroker eventBroker;
	
	
	@Inject
	@Translation
	MatrosMessage messages;


	
	
	@Inject Shell activeShell;


   	WizardDialog dialog;
   	
	@Inject
	public WelcomePart() {
	
	}
	

	
	
	@PostConstruct
	public void createComposite(Composite parent) {
		
		
// userlist erzeugen		

		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Composite composite_header = new Composite(composite, SWT.NONE);
		composite_header.setLayout(new RowLayout(SWT.HORIZONTAL));
		composite_header.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		composite_header.setBounds(0, 0, 64, 64);
		
		Label lblWelcomeToMatros = new Label(composite_header, SWT.NONE);
		lblWelcomeToMatros.setText("Welcome to Matros");
//		
	    Label image = new Label(composite_header, SWT.NONE);
        image.setBackground(composite_header.getBackground());
   
		image.setImage(poolOfResources.getImage(IMatrosResource.Images.NOTE));
	    
        
		
		Composite composite_choose_existing_or_new = new Composite(composite, SWT.NONE);
		composite_choose_existing_or_new.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		composite_choose_existing_or_new.setLayout(new RowLayout(SWT.VERTICAL));
		
		Link link_open_existing_installation = new Link(composite_choose_existing_or_new, SWT.NONE);
		link_open_existing_installation.setText("<a>Open existing Installation</a>");
		
		link_open_existing_installation.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	
       
            	OpenExistingWizzard setupOpenExistingWizzard = ContextInjectionFactory.make(OpenExistingWizzard.class, context);
            	dialog = new WizardDialog(activeShell, setupOpenExistingWizzard);
            	dialog.create();
        		dialog.open();
            }
        });
        
		
		Label lblNewLabel = new Label(composite_choose_existing_or_new, SWT.NONE);
		lblNewLabel.setText("Open an existing Installation ");
		
		Link link_create_new_database = new Link(composite_choose_existing_or_new, SWT.NONE);
		link_create_new_database.setText("<a>Create new Database</a>");
		

		
		Label lblNewLabel_1 = new Label(composite_choose_existing_or_new, SWT.NONE);
		lblNewLabel_1.setText("Create a new Database ");
		
	
		
		Composite composite_help = new Composite(composite, SWT.NONE);
		composite_help.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		
		link_create_new_database.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
            	
       
            	SetupNewWizzard setupNewWizzard = ContextInjectionFactory.make(SetupNewWizzard.class, context);
            	dialog = new WizardDialog(activeShell, setupNewWizzard);
            	dialog.create();
        		dialog.open();
   
        		
    		  
            }
        });
		
	}
}