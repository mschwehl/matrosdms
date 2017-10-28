package net.schwehla.matrosdms.rcp.wizzard.page.dialog;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.idm.MatrosUser;
import net.schwehla.matrosdms.domain.util.ObjectCloner;
import net.schwehla.matrosdms.rcp.binding.MatrosBinder;


@Creatable
public class UserDialog extends TitleAreaDialog  {
	
	
    MatrosUser local_clone_user;
    MatrosUser givenUser;
    
	private Text text_firstname;
	private Text text_secondname;
	private Text text_password1;
	private Text text_login_name;
	private Text text_email;
	private Text text_Icon;

	
    public UserDialog(Shell parentShell, MatrosUser user) throws Exception {
        super(parentShell);
		this.local_clone_user =  new ObjectCloner<MatrosUser>().cloneThroughSerialize(user);
		this.givenUser = user;
    }
    
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
            
         
  
            container.pack();
       
    
            return container;
    }
    
    // buttons are created late
    // https://www.eclipse.org/forums/index.php/t/97014/
    
    
    @Override
    protected Control createButtonBar(Composite parent) {
      Control c = super.createButtonBar(parent);
      

      
  	  Button ok = getButton(IDialogConstants.OK_ID);
  	
	  if (ok != null) {
	  	ok.setEnabled(local_clone_user.getName() != null && local_clone_user.getName().length() > 1);
	  }
	  
      createBinding();
      
      return c;
      
    }
    
    
    private void createBinding() {
    	

    	
    	Button ok = getButton(IDialogConstants.OK_ID);
    	if (ok != null) {
    		
    		
    		MatrosBinder bindingNewStype = new MatrosBinder(ok);
    		
    		MatrosBinder.Twoway firstName =  bindingNewStype.bindComposite(text_firstname, true)
    			.toModelTwoWay(local_clone_user, "firstname", (x) -> { local_clone_user.setFirstname( (String) x); } ).build();
    		
    		MatrosBinder.Twoway secondName =  bindingNewStype.bindComposite(text_secondname, true)
			.toModelTwoWay(local_clone_user, "secondname", (x) -> { local_clone_user.setSecondname( (String) x); } ).build();

    		MatrosBinder.Twoway email =  bindingNewStype.bindComposite(text_email, true)
			.toModelTwoWay(local_clone_user, "email", (x) -> { local_clone_user.setEmail( (String) x); } ).build();
    		
    		
    		MatrosBinder.Twoway passwd =  bindingNewStype.bindComposite(text_password1, true)
        			.toModelTwoWay(local_clone_user, "password", (x) -> { local_clone_user.setPassword( (String) x); } ).build();

    		MatrosBinder.Twoway login =  bindingNewStype.bindComposite(text_login_name, true)
        			.toModelTwoWay(local_clone_user, "name", (x) -> { local_clone_user.setName( (String) x); } ).build();
    		
    		bindingNewStype.bindComposite(text_Icon, false)
        			.toModelTwoWay(local_clone_user, "icon", (x) -> { local_clone_user.setIcon( (String) x); } ).build();
    		
    		
    		bindingNewStype.setButtonVisible(ok,firstName,secondName,email,passwd,login);
    		
    	}

    	

	}

	

	@Override
    protected boolean isResizable() {
            return true;
    }

    
    @Override
    protected void okPressed() {
  
    	// copy all properties to other object
    	givenUser.applyValues(local_clone_user);
    	
    	super.okPressed();
    }

    
    
    

}
