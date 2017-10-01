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

import net.schwehla.matrosdms.domain.core.InfoOrginalstore;


@Creatable
public class OriginalstoreDialog extends TitleAreaDialog  {
	
    InfoOrginalstore originalstore;
    
    
	private Text text_name;
	private Text text_note;
	private Text text_icon;
	private Text text_shortname;

	
    public OriginalstoreDialog(Shell parentShell, InfoOrginalstore user) {
        super(parentShell);
        this.originalstore = user;
    }
    
	@Override
	public void create() {
	        super.create();
	        setTitle("Dokument-Ablage");
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
            lblNewLabel.setText("Name");
            
            text_name = new Text(composite, SWT.BORDER);
            text_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            
            Label lblIcon = new Label(composite, SWT.NONE);
            lblIcon.setText("Icon");
            
            text_icon = new Text(composite, SWT.BORDER);
            text_icon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                        
                        Label lblKrzel = new Label(composite, SWT.NONE);
                        lblKrzel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                        lblKrzel.setText("K\u00FCrzel");
                        
                        text_shortname = new Text(composite, SWT.BORDER);
                        text_shortname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            
                        
                        Label lblNewLabel_4 = new Label(composite, SWT.NONE);
                        lblNewLabel_4.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
                        lblNewLabel_4.setText("Notiz");
            
            text_note = new Text(composite, SWT.BORDER | SWT.MULTI);
            text_note.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
            
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
	  	ok.setEnabled(originalstore.getName() != null && originalstore.getName().length() > 1);
	  }
	  
      createBinding();
      
      return c;
      
    }
    
    
    private void createBinding() {
    	
    	
    	Button ok = getButton(IDialogConstants.OK_ID);
    	if (ok != null) {
    		
    		
    		ISWTObservableValue text_nameObservable = WidgetProperties.text(SWT.Modify)
    			    .observe(text_name);
    		
    		ISWTObservableValue text_iconObservable = WidgetProperties.text(SWT.Modify)
    			    .observe(text_icon);
    		
    		ISWTObservableValue text_noteObservable = WidgetProperties.text(SWT.Modify)
    			    .observe(text_note);
    		
    		
    		// track whether the selection list is empty or not
    		// (viewerSelectionObservable.isEmpty() is a tracked getter!)
    		IObservableValue<Boolean> hasSelectionObservable = ComputedValue
    				.create(() -> text_nameObservable.getValue() != null );

    		// once the selection state(Boolean) changes the ISideEffect will
    		// update the button

    		
    		ISideEffect enableOkButtonSideEffect = ISideEffect.create(hasSelectionObservable::getValue,ok::setEnabled);

    		ok.addDisposeListener(e -> enableOkButtonSideEffect.dispose());
     		ok.addDisposeListener(e -> text_nameObservable.dispose());
     		ok.addDisposeListener(e -> text_iconObservable.dispose());
     	   	ok.addDisposeListener(e -> text_noteObservable.dispose()); 		
   			
     		        
    		
    	}

    	
		// TODO Auto-generated method stub
		
	}

	private void init() {
    	
    	if (originalstore != null) {
    		
    		text_name.setText( originalstore.getName() != null ? originalstore.getName() : "" ); //$NON-NLS-1$
    		text_icon.setText( originalstore.getIcon() != null ? originalstore.getIcon() : "" ); //$NON-NLS-1$
    		text_note.setText( originalstore.getDescription() != null ? originalstore.getDescription() : "" ); //$NON-NLS-1$
    		
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

    	originalstore.setName( text_name.getText());
    	originalstore.setIcon(text_icon.getText());
    	originalstore.setDescription(text_note.getText());

    }
    
    
    

}
