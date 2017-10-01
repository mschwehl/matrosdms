package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import javax.inject.Inject;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.domain.admin.E_CLOUDCRYPTION;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.wizzard.model.setup.Masterdata;

@Creatable
public class SetupPageServerCloudstore extends WizardPage {

	@Inject
	Masterdata masterData;
	
	@Inject
	@Translation
	MatrosMessage messages;
	private Text txtPath;
	private Button btn7zip;
	private Button btnNoCrypt;
	private Button btnInternalCryption;


	/**
	 * Create the wizard.
	 */
	@Inject
	public SetupPageServerCloudstore(@Translation MatrosMessage messages) {
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
		
		Label lblCloudPath = new Label(compositeTreeAndButtons, SWT.NONE);
		lblCloudPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCloudPath.setText("CloudPath");
		
		txtPath = new Text(compositeTreeAndButtons, SWT.BORDER);
		txtPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnFind = new Button(compositeTreeAndButtons, SWT.NONE);
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			    DirectoryDialog dialog = new DirectoryDialog( Display.getDefault().getActiveShell());
			    
			    String result = dialog.open();
			    
			    if (result != null) {
			    	
				    masterData.getCloudSettings().setPath(result);
				    txtPath.setText(result);

				    txtPath.redraw();
				 
				    getContainer().updateButtons();
			    }
			    
			    
			}
		});
		btnFind.setText("Find");
		
		Label lblType = new Label(compositeTreeAndButtons, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblType.setText("Type");
		
		Group group = new Group(compositeTreeAndButtons, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		btn7zip = new Button(group, SWT.RADIO);
		btn7zip.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btn7zip.setText("External (7Zip) - RECOMENDED");
		
		btnInternalCryption = new Button(group, SWT.RADIO);
		btnInternalCryption.setText("Internal Cryption");
		
		btnNoCrypt = new Button(group, SWT.RADIO);
		btnNoCrypt.setText("No Encryption (NOT RECOMENDED)");
		new Label(compositeTreeAndButtons, SWT.NONE);
		

		bind();
		
	}
	
     
     
     private void bind() {
	
     		
    	 IObservableValue <String> txtPathObservable = WidgetProperties.text(SWT.Modify)
 			    .observe(txtPath);

 		txtPath.addDisposeListener(e -> txtPathObservable.dispose());
 		
        IObservableValue <String> pojoObserverPath = PojoProperties.value("path").observe(masterData.getCloudSettings());

        
        IObservableValue buttonObserve7zip = WidgetProperties.selection().observe(btn7zip);
        IObservableValue buttonObserveInternal = WidgetProperties.selection().observe(btnInternalCryption);
        IObservableValue buttonObserveNo = WidgetProperties.selection().observe(btnNoCrypt);
        
        // Binding
        selectedRadioButtonObservable = new SelectObservableValue();
        
        selectedRadioButtonObservable.addOption(select1 , buttonObserve7zip);
        selectedRadioButtonObservable.addOption(select2 , buttonObserveInternal);
        selectedRadioButtonObservable.addOption(select3 , buttonObserveNo);

        
        ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(btn7zip);
        
        sideEffectFactory.create(pojoObserverPath::getValue, txtPathObservable::setValue);
        sideEffectFactory.create(txtPathObservable::getValue,  (e) -> { masterData.getCloudSettings().setPath(e);});
        
        
        
        sideEffectFactory.create(txtPathObservable::getValue , e -> { 
        	getContainer().updateButtons(); });
        
                
        sideEffectFactory.create(selectedRadioButtonObservable::getValue, e -> {

//        	
        	String x = "" + e;
        	
        	switch (x) {
			case "1":
				
				masterData.getCloudSettings().getCryptSettings().setCryption( E_CLOUDCRYPTION.EXTERNAL );
	        	
				break;

			case "2":
				
				masterData.getCloudSettings().getCryptSettings().setCryption( E_CLOUDCRYPTION.INTERNAL );
	        	
				break;
			
			case "3":
				
				masterData.getCloudSettings().getCryptSettings().setCryption( E_CLOUDCRYPTION.NONE );
	        	
				break;
					
				default:
					
					masterData.getCloudSettings().getCryptSettings().setCryption(null);
					break;
				}
        	
		    	getContainer().updateButtons();
		    
        	}
        );
    	 
		
	}

     
     SelectObservableValue selectedRadioButtonObservable ;

     static String select1="1"; //$NON-NLS-1$
     static String select2="2"; //$NON-NLS-1$
     static String select3="3"; //$NON-NLS-1$
     

	@Override
    public boolean isPageComplete() {
    	return masterData.getCloudSettings().isComplete();
    }
}
