package net.schwehla.matrosdms.rcp.wizzard.setup.page;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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

@Creatable
public class SetupPageApplicationSettings extends MatrosWizzardPage {

	@Inject
	@Translation
	MatrosMessage messages;
	
    private Text txtUsername;
    private Text txtPassword;
    
    private Text txtInstallPath;

	
	/**
	 * Create the wizard.
	 */
	public SetupPageApplicationSettings() {
		super("wizardPage");
		setTitle("Welcome to Matros");
		setDescription("Application Directory");
	}


	@Override
	public void buildControl(Composite parent) {
		
		if (masterData.getAppSettings() == null) {
			masterData.setAppSettings(new AppSettings());
		}
		
		
		Composite compositeInboxPage = new Composite(parent, SWT.NULL);

		setControl(compositeInboxPage);
		compositeInboxPage.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(compositeInboxPage, SWT.NONE);
		
		TabItem tbtmInbox = new TabItem(tabFolder, SWT.FILL);
		tbtmInbox.setText(messages.wizzard_database_database);
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText("Der Index Ihrer Dokumente wird in einer Datenbank gespeichert");
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(1, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		

		
		Composite composite = new Composite(compositeTreeAndButtons, SWT.NONE);
	
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		Label lblAppPath = new Label(composite, SWT.NONE);
		lblAppPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAppPath.setText("App Path");
		
		txtInstallPath = new Text(composite, SWT.BORDER);
		txtInstallPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		Composite compositeDatabase = new Composite(compositeTreeAndButtons, SWT.NONE);
		compositeDatabase.setLayout(new GridLayout(2, false));
		compositeDatabase.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		
		Label lblNewLabel = new Label(compositeDatabase, SWT.NONE);
		lblNewLabel.setText("Datenbank-Benutzer");
		
		txtUsername = new Text(compositeDatabase, SWT.BORDER);

		txtUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		
		Label lblNewLabel_1 = new Label(compositeDatabase, SWT.NONE);
		lblNewLabel_1.setText("Datenbank-Passwort");
		
		txtPassword = new Text(compositeDatabase, SWT.BORDER | SWT.PASSWORD);

		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		

		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				

				
			    DirectoryDialog dialog = new DirectoryDialog( Display.getDefault().getActiveShell());
			    
			    String result = dialog.open();
			    
			    if (result != null) {
			    	
			    	txtInstallPath.setText(result);
			    }
		
			}
		});
		btnNewButton.setText(messages.wizzard_database_dbpath);
		
	
	}

	
		
		public DataBindingContext initDataBindings() {
			

			
			
			ISWTObservableValue  user = WidgetProperties.text(SWT.Modify).observe(txtUsername);
			ISWTObservableValue  pass = WidgetProperties.text(SWT.Modify).observe(txtPassword);
			ISWTObservableValue  path = WidgetProperties.text(SWT.Modify).observe(txtInstallPath);
			
			// dbUser : String
			// dbPasswd : String
			// dbPath : String
			
			IObservableValue<String> myModelUser = PojoProperties.value("dbUser").observe(masterData.getDbConnection());
			IObservableValue<String> myDbUser    = PojoProperties.value("dbPasswd").observe(masterData.getDbConnection());
			IObservableValue<String> myAppRoot    = PojoProperties.value("appPath").observe(masterData.getAppSettings());
			        
			
		
			// track whether the selection list is empty or not
			// (viewerSelectionObservable.isEmpty() is a tracked getter!)
			IObservableValue<Boolean> hasSelectionObservable = ComputedValue
					.create(() -> user.getValue() != null && user.getValue().toString().length() > 0 
							&& pass.getValue() != null && pass.getValue().toString().length() > 0 
							&& path.getValue() != null  && path.getValue().toString().length() > 0 	);

			
			// once the selection state(Boolean) changes the ISideEffect will
			// update the button

			
			ISideEffect enableOkButtonSideEffect = ISideEffect.create(hasSelectionObservable::getValue,this::accept);
			
			txtUsername.addDisposeListener(e -> hasSelectionObservable.dispose());
			txtUsername.addDisposeListener(e -> enableOkButtonSideEffect.dispose());

			
			//
			DataBindingContext bindingContext = new DataBindingContext();
			//
			bindingContext.bindValue(user, myModelUser, null, null);
			bindingContext.bindValue(pass, myDbUser, null, null);
			bindingContext.bindValue(path, myAppRoot, null, null);
			//
	
			return bindingContext;
			
		}


	
	 
}
