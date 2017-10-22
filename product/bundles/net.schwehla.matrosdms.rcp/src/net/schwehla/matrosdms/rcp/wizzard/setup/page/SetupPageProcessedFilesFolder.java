package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.admin.AppSettings;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.binding.MatrosBinder;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;

@Creatable
public class SetupPageProcessedFilesFolder extends WizardPage {

	@Inject
	@Translation
	MatrosMessage messages;
	
	
	@Inject
	Masterdata masterData;
	

	
    
    private Text txtProcessedFolderPath;

	
	/**
	 * Create the wizard.
	 */
	public SetupPageProcessedFilesFolder() {
		super("wizardPage");
		setTitle("Welcome to Matros");
		setDescription("Processed Files Folder");
	}


	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {

		
		if (masterData.getAppSettings() == null) {
			masterData.setAppSettings(new AppSettings());
		}
		
		
		Composite compositeInboxPage = new Composite(parent, SWT.NULL);

		setControl(compositeInboxPage);
		compositeInboxPage.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(compositeInboxPage, SWT.NONE);
		
		TabItem tbtmInbox = new TabItem(tabFolder, SWT.FILL);
		tbtmInbox.setText(messages.wizzard_processedFolderTabLabel);
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText(messages.wizzard_processedFolderDescription);
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(1, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		

		
		Composite composite = new Composite(compositeTreeAndButtons, SWT.NONE);
	
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		Label lblAppPath = new Label(composite, SWT.NONE);
		lblAppPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAppPath.setText("Processed Folder");
		
		txtProcessedFolderPath = new Text(composite, SWT.BORDER);
		txtProcessedFolderPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		

		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				

				
			    DirectoryDialog dialog = new DirectoryDialog( Display.getDefault().getActiveShell());
			    
			    String result = dialog.open();
			    
			    if (result != null) {
			    	
			    	txtProcessedFolderPath.setText(result);
			    }
		
			}
		});
		btnNewButton.setText(messages.wizzard_database_dbpath);
		

		bind();
		
	}
	
     
     
     private void bind() {
	
     		        
         MatrosBinder bindingNewStype = new MatrosBinder(super.getControl());
         
         
 		 bindingNewStype.bindComposite(txtProcessedFolderPath, true)
		.toModelTwoWay(masterData.getAppSettings().getProcessedFolder() , "processedFolder", (x) -> { masterData.getAppSettings().setProcessedFolder( (String) x); } ).build();
	
 		  		 
 		bindingNewStype.invokeAfterChange( (x) -> { getContainer().updateButtons(); });
	    	

 		 
	}
}
