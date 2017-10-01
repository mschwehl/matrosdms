package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.binding.MatrosBinder;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;

@Creatable
public class SetupPageServerCryptExternal extends WizardPage {

	@Inject
	Masterdata masterData;
	
	@Inject
	@Translation
	MatrosMessage messages;
	private Text txtPathExternalExe;
	private Text textPassword;
	private Text textCrypt;
	private Text textDecrypt;
	private Button btnFind;


	/**
	 * Create the wizard.
	 */
	@Inject
	public SetupPageServerCryptExternal(@Translation MatrosMessage messages) {
		super("wizardPage");
		setTitle("Welcome to Matros");
		setDescription("Document mangement System");
	}

	


	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		
		if (masterData.getCloudSettings() == null) {
			masterData.setCloudSettings(new CloudSettings());
		}
		
		Composite compositeInboxPage = new Composite(parent, SWT.NULL);

		setControl(compositeInboxPage);
		compositeInboxPage.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(compositeInboxPage, SWT.NONE);
		
		TabItem tbtmInbox = new TabItem(tabFolder, SWT.FILL);
		tbtmInbox.setText(messages.setupPageDocumentstore_title);
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText(messages.setupPageDocumentstore_detail);
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(3, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblExternalProgram = new Label(compositeTreeAndButtons, SWT.NONE);
		lblExternalProgram.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblExternalProgram.setText("External Programm");
		
		txtPathExternalExe = new Text(compositeTreeAndButtons, SWT.BORDER);
		txtPathExternalExe.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnFind = new Button(compositeTreeAndButtons, SWT.NONE);
		btnFind.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			    FileDialog dialog = new FileDialog( Display.getDefault().getActiveShell());
			    
			    String result = dialog.open();
			    
			    if (result != null) {
			        txtPathExternalExe.setText(result);
				    txtPathExternalExe.redraw();
				    getContainer().updateButtons();
			    }
			    
			    
			}
		});
		btnFind.setText("Find");
		
		Label lblPassword = new Label(compositeTreeAndButtons, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password");
		
		textPassword = new Text(compositeTreeAndButtons, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSneak = new Button(compositeTreeAndButtons, SWT.NONE);
		btnSneak.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSneak.setText("Look");
		
		Label lblNewLabel_1 = new Label(compositeTreeAndButtons, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Template");
		
		Combo combo = new Combo(compositeTreeAndButtons, SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeTreeAndButtons, SWT.NONE);
		
		Label lblNewLabel = new Label(compositeTreeAndButtons, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
		lblNewLabel.setText("CommandLine");
		
		CTabFolder tabFolder_1 = new CTabFolder(compositeTreeAndButtons, SWT.BORDER);
		tabFolder_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder_1.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmCryptSettings = new CTabItem(tabFolder_1, SWT.NONE);
		tbtmCryptSettings.setText("cryption");
		
		Composite composite = new Composite(tabFolder_1, SWT.NONE);
		tbtmCryptSettings.setControl(composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		textCrypt = new Text(composite, SWT.BORDER);
		textCrypt.setText("");
		
		CTabItem tbtmDecryptSettins = new CTabItem(tabFolder_1, SWT.NONE);
		tbtmDecryptSettins.setText("decryption");
		
		Composite composite_1 = new Composite(tabFolder_1, SWT.NONE);
		tbtmDecryptSettins.setControl(composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		textDecrypt = new Text(composite_1, SWT.BORDER);
		new Label(compositeTreeAndButtons, SWT.NONE);
		new Label(compositeTreeAndButtons, SWT.NONE);
		
		Button btnTest = new Button(compositeTreeAndButtons, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnTest.setText("Test settings");
		new Label(compositeTreeAndButtons, SWT.NONE);
		

		bind();
		
	}
	
     
     
     private void bind() {
	
     		        
         MatrosBinder bindingNewStype = new MatrosBinder(btnFind);
         
 		 bindingNewStype.bindComposite(textPassword, true)
		.toModelTwoWay(masterData.getCloudSettings().getCryptSettings(), "password", (x) -> { masterData.getCloudSettings().getCryptSettings().setPassword( (String) x); } ).build();
	
 		 bindingNewStype.bindComposite(txtPathExternalExe, true)
		.toModelTwoWay(masterData.getCloudSettings().getCryptSettings(), "exePath", (x) -> { masterData.getCloudSettings().getCryptSettings().setExePath( (String) x); } ).build();
 		 
 		 bindingNewStype.bindComposite(textCrypt, true)
		.toModelTwoWay(masterData.getCloudSettings().getCryptSettings(), "exeEncryptline", (x) -> { masterData.getCloudSettings().getCryptSettings().setExeEncryptline( (String) x); } ).build();
		
 		 
 		 bindingNewStype.bindComposite(textDecrypt, true)
		.toModelTwoWay(masterData.getCloudSettings().getCryptSettings(), "exeDecryptline", (x) -> { masterData.getCloudSettings().getCryptSettings().setExeDecryptline( (String) x); } ).build();
 		  		 
 		bindingNewStype.invokeAfterChange( (x) -> { getContainer().updateButtons(); });
	    	

 		 
	}




	@Override
    public boolean isPageComplete() {
    	return masterData.getCloudSettings().getCryptSettings().isComplete();
    }
}
