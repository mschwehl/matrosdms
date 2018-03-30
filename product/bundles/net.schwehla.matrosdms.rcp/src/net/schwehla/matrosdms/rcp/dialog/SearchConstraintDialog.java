package net.schwehla.matrosdms.rcp.dialog;

import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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

import net.schwehla.matrosdms.domain.core.attribute.AttributeType;


@Creatable
public class SearchConstraintDialog extends TitleAreaDialog  {
	
	AttributeType constraint;
    

	String constraitStr;
	
	
	public String getConstraitStr() {
		return constraitStr;
	}

	public void setConstraitStr(String constraitStr) {
		this.constraitStr = constraitStr;
	}
	private Text text_name;
	private Text text_constraint;

	
    public Text getText_constraint() {
		return text_constraint;
	}

	public void setText_constraint(Text text_constraint) {
		this.text_constraint = text_constraint;
	}

	public SearchConstraintDialog(Shell parentShell, AttributeType user) {
        super(parentShell);
        this.constraint = user;
    }
    
	@Override
	public void create() {
	        super.create();
	        setTitle("Constraint");
	        setMessage("Constraint", IMessageProvider.INFORMATION);
	}


    @Override
    protected Control createDialogArea(Composite parent) {
            Composite container = (Composite) super.createDialogArea(parent);
            GridLayout gridLayout = (GridLayout) container.getLayout();
            
            Composite composite = new Composite(container, SWT.NONE);
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            composite.setLayout(new GridLayout(2, false));
            
            Label lblNewLabel = new Label(composite, SWT.NONE);
            lblNewLabel.setText("Name");
            
            text_name = new Text(composite, SWT.BORDER);
            text_name.setEditable(false);
            text_name.setEnabled(false);
            text_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            
                        
                        Label lblNewLabel_4 = new Label(composite, SWT.NONE);
                        lblNewLabel_4.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
                        lblNewLabel_4.setText("constraint");
            
            text_constraint = new Text(composite, SWT.BORDER | SWT.MULTI);
            text_constraint.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            
            init();
  
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
	  	ok.setEnabled(constraint.getName() != null && constraint.getName().length() > 1);
	  }
	  
      createBinding();
      
      return c;
      
    }
    
    
    private void createBinding() {
    	
    	
    	Button ok = getButton(IDialogConstants.OK_ID);
    	if (ok != null) {
    		
    		
    		ISWTObservableValue text_nameObservable = WidgetProperties.text(SWT.Modify)
    			    .observe(text_name);
    		
    		
    		ISWTObservableValue text_noteObservable = WidgetProperties.text(SWT.Modify)
    			    .observe(text_constraint);
    		
    		
    		// track whether the selection list is empty or not
    		// (viewerSelectionObservable.isEmpty() is a tracked getter!)
    		IObservableValue<Boolean> hasSelectionObservable = ComputedValue
    				.create(() -> text_nameObservable.getValue() != null );

    		// once the selection state(Boolean) changes the ISideEffect will
    		// update the button

    		
    		ISideEffect enableOkButtonSideEffect = ISideEffect.create(hasSelectionObservable::getValue,ok::setEnabled);

    		ok.addDisposeListener(e -> enableOkButtonSideEffect.dispose());
     		ok.addDisposeListener(e -> text_nameObservable.dispose());
     	   	ok.addDisposeListener(e -> text_noteObservable.dispose()); 		
   			
     		        
    		
    	}

    	
		// TODO Auto-generated method stub
		
	}

	private void init() {
    	
    	if (constraint != null) {
    		
    		text_name.setText( constraint.getName() != null ? constraint.getName() : "" ); //$NON-NLS-1$
    		text_constraint.setText( constraint.getDescription() != null ? constraint.getDescription() : "" ); //$NON-NLS-1$
    		
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

//    	constraint.setName( text_name.getText());
//    	constraint.setIcon(text_icon.getText());
//    	constraint.setDescription(text_note.getText());
    	
    	constraitStr = text_constraint.getText();

    }
    
    
    

}
