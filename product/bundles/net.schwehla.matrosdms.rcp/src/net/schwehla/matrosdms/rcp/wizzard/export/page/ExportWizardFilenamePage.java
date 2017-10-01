package net.schwehla.matrosdms.rcp.wizzard.export.page;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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

import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.wizzard.model.ExportWizzardData;


@Creatable
public class ExportWizardFilenamePage extends WizardPage {

	@Inject
	@Translation
	MatrosMessage messages;
	
	@Inject
	@Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS) 
	IEclipsePreferences preferences ;
	
	
	Button btnMetadata ;
	private Text txtPath;
	private Text txtPattern;
	private Button btnCrypt;
	

	
	/**
	 * Create the wizard.
	 */
	@Inject
	public ExportWizardFilenamePage(@Translation MatrosMessage messages) {
		super(messages.wizzard_export_title);
		setTitle(messages.wizzard_export_title);
		setDescription(messages.wizzard_export_description);
	}



	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite compositeInboxPage = new Composite(parent, SWT.NULL);

		setControl(compositeInboxPage);
		compositeInboxPage.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(compositeInboxPage, SWT.NONE);
		
		TabItem tbtmInbox = new TabItem(tabFolder, SWT.FILL);
		tbtmInbox.setText(messages.wizzard_filemessage);
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText(messages.wizzard_filemessage_description);
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(2, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblPath = new Label(compositeTreeAndButtons, SWT.NONE);
		lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPath.setText(messages.wizzard_export_path);
		
		Composite composite = new Composite(compositeTreeAndButtons, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		gl_composite.verticalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		txtPath = new Text(composite, SWT.BORDER);
		txtPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPattern = new Label(compositeTreeAndButtons, SWT.NONE);
		lblPattern.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblPattern.setText(messages.wizzard_export_pattern);
		
		Button btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
						DirectoryDialog dialog = new DirectoryDialog( Display.getDefault().getActiveShell());
						dialog.setFilterPath(txtPath.getText());
							
					    String result = dialog.open();
					    
					    if (result != null) {
					    	txtPath.setText(result);
						    txtPath.redraw();
						    getContainer().updateButtons();
					    }
					    
					}
				});
				
		
		btnBrowse.setText(messages.wizzard_export_browse);
		
		txtPattern = new Text(compositeTreeAndButtons, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txtPattern.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblMetadata = new Label(compositeTreeAndButtons, SWT.NONE);
		lblMetadata.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMetadata.setText(messages.wizzard_export_include_metadata);
		
	    btnMetadata = new Button(compositeTreeAndButtons, SWT.CHECK);
		
		Label lblCrypted = new Label(compositeTreeAndButtons, SWT.NONE);
		lblCrypted.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCrypted.setText(messages.wizzard_export_crypted);
		
		btnCrypt = new Button(compositeTreeAndButtons, SWT.CHECK);

		 init();
	
		 bind();
	}

	
	private void bind() {

 		
 		
   	 IObservableValue <String> txtPathObservable = WidgetProperties.text(SWT.Modify)
			    .observe(txtPath);
		
   	 IObservableValue <String> txtPatternObservable = WidgetProperties.text(SWT.Modify)
			    .observe(txtPattern);
		
   	 

		
		// track whether the selection list is empty or not
		// (viewerSelectionObservable.isEmpty() is a tracked getter!)
		IObservableValue<Boolean> hasSelectionObservable = ComputedValue
				.create(() -> txtPathObservable.getValue() != null && txtPatternObservable.getValue() != null);

		// once the selection state(Boolean) changes the ISideEffect will
		// update the button
		ISideEffect deleteButtonEnablementSideEffect = ISideEffect.create(hasSelectionObservable::getValue,
				
				e -> { getContainer().updateButtons(); } );
			

		txtPath.addDisposeListener(e -> hasSelectionObservable.dispose());
		txtPath.addDisposeListener(e -> deleteButtonEnablementSideEffect.dispose());
		txtPath.addDisposeListener(e -> txtPathObservable.dispose());
		txtPath.addDisposeListener(e -> txtPatternObservable.dispose());
		
	}




	private void init() {
		
		
		 String path = preferences.get(MyGlobalConstants.Preferences.EXPORT_PATH, "");
		 String pattern = preferences.get(MyGlobalConstants.Preferences.EXPORT_PATTERN, "context.name + '/' + item.metadata.filename");		
	    
		 txtPath.setText(path);
		 txtPattern.setText(pattern);
	}

	public ExportWizzardData applyDataTo(ExportWizzardData data) {
		
		data.setCrypted(btnCrypt.getSelection());
		data.setMetadata(btnMetadata.getSelection());
		data.setPath(txtPath.getText());
		data.setPattern(txtPattern.getText());
		
		return data;
	}

	
	
    @Override
   public boolean isPageComplete() {
    	
   	return txtPath.getText() != null && txtPath.getText().length() > 0 
   			&& txtPattern.getText() != null && txtPattern.getText().length() > 0;
   }
    
    
	
}
