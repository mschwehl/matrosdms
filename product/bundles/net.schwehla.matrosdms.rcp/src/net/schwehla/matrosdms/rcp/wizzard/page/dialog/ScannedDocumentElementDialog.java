package net.schwehla.matrosdms.rcp.wizzard.page.dialog;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.admin.MatrosUser;


@Creatable
public class ScannedDocumentElementDialog extends TitleAreaDialog  {
	
	private Text text_firstname;
	private Text text_secondname;
	private Text text_password1;
	private Text text_login_name;
	private Text text_email;
	private Text text_Icon;

	
    public ScannedDocumentElementDialog(Shell parentShell, MatrosUser user) {
        super(parentShell);
        
        this.user = user;
        
    }

    MatrosUser user;
    

    
	@Override
	public void create() {
	        super.create();
	        setTitle("Matros-User");
	        setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION);
	}


    @Override
    protected Control createDialogArea(Composite parent) {
            Composite container = (Composite) super.createDialogArea(parent);
            GridLayout gridLayout = (GridLayout) container.getLayout();
            
            Composite composite = new Composite(container, SWT.NONE);
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            composite.setLayout(new GridLayout(2, false));
            
            Label lblNewLabel = new Label(composite, SWT.NONE);
            lblNewLabel.setText("Vorname");
            
            text_firstname = new Text(composite, SWT.BORDER);
            text_firstname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            
            Label lblNewLabel_1 = new Label(composite, SWT.NONE);
            lblNewLabel_1.setText("Nachname");
            
            text_secondname = new Text(composite, SWT.BORDER);
            text_secondname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            
            Label lblNewLabel_2 = new Label(composite, SWT.NONE);
            lblNewLabel_2.setText("Email");
            
            text_email = new Text(composite, SWT.BORDER);
            text_email.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            
            Label lblNewLabel_3 = new Label(composite, SWT.NONE);
            lblNewLabel_3.setText("Login-Name");
            
            text_login_name = new Text(composite, SWT.BORDER);
            text_login_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            
            Label lblIcon = new Label(composite, SWT.NONE);
            lblIcon.setText("Icon");
            
            text_Icon = new Text(composite, SWT.BORDER);
            text_Icon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

            
            Label lblNewLabel_4 = new Label(composite, SWT.NONE);
            lblNewLabel_4.setText("Passwort");
            
            text_password1 = new Text(composite, SWT.BORDER | SWT.PASSWORD);
            text_password1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            
            init();
            
            return container;
    }
    
    private void init() {
    	
    	if (user != null) {
    		
    	 	text_email.setText(user.getEmail() != null ? user.getEmail() : "");
    	 	text_login_name.setText(user.getName() != null ? user.getName() : "");
        	// xxx
    		
    	}
		
	}


	@Override
    protected boolean isResizable() {
            return true;
    }

    
    @Override
    protected void okPressed() {
    	saveInput();
    	super.okPressed();
    }
    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private void saveInput() {
    	
    	user.setFirstname(text_firstname.getText());
    	user.setSecondname(text_secondname.getText());
    	
    	// login-name
    	user.setName(text_login_name.getText());
    	user.setIcon(text_Icon.getText());
    	
    	user.setEmail(text_email.getText());
    	user.setPassword(text_password1.getText());

    }
    

}
