package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
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

import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.binding.MatrosBinder;


@Creatable
public class ItemMetadataDialog extends TitleAreaDialog  {
	
	MatrosMetadata metadata;
    
	@Inject
	@Translation
	MatrosMessage messages;
	private Text textSHA;
	private Text textMinetype;
	
	
	// needs injection for context.create
	@Inject
    public ItemMetadataDialog(Shell parentShell, MatrosMetadata metadata) {
        super(parentShell);
        this.metadata = metadata;
    }
    
	@Override
	public void create() {
	        super.create();
	        setTitle("Metadata");
	        setMessage("Metadata", IMessageProvider.INFORMATION);
	}


    @Override
    protected Control createDialogArea(Composite parent) {
            Composite container = (Composite) super.createDialogArea(parent);
            GridLayout gridLayout = (GridLayout) container.getLayout();
            
            Composite composite = new Composite(container, SWT.NONE);
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            composite.setLayout(new GridLayout(2, false));
            
            Label lblHash = new Label(composite, SWT.NONE);
            lblHash.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            lblHash.setText("SHA256");
            
            textSHA = new Text(composite, SWT.BORDER);
            textSHA.setEditable(false);
            textSHA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            
            Label lblMimetype = new Label(composite, SWT.NONE);
            lblMimetype.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            lblMimetype.setText("Mimetype");
            
            textMinetype = new Text(composite, SWT.BORDER);
            textMinetype.setEditable(false);
            textMinetype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            
            
            
            init();
         
  
            container.pack();
       
    
            return container;
    }
    
    // buttons are created late
    // https://www.eclipse.org/forums/index.php/t/97014/
    
//    
//    @Override
    protected Control createButtonBar(Composite parent) {
      Control c = super.createButtonBar(parent);
  	  Button ok = getButton(IDialogConstants.OK_ID);
  	
	  
      createBinding();
      
      return c;
      
    }
    
    
    private void createBinding() {
    	

    	Button ok = getButton(IDialogConstants.OK_ID);
    	if (ok != null) {
    		
    		/*
    		  	String mimetype;
				String filename;
				String sha256;
				Long   filesize;
    		 */
    		
    		MatrosBinder bindingNewStype = new MatrosBinder(ok);
    		
    		MatrosBinder.Twoway sha =  bindingNewStype.bindComposite(textSHA, false)
    			.toModelTwoWay(metadata, "sha256", (x) -> { metadata.setSha256( (String) x); } ).build();
    		
    		MatrosBinder.Twoway mime =  bindingNewStype.bindComposite(textMinetype, false)
        			.toModelTwoWay(metadata, "mimetype", (x) -> { metadata.setMimetype( (String) x); } ).build();

    		
    	}
		
    
    	
		// TODO Auto-generated method stub
		
	}

	private void init() {
		
	}


	@Override
    protected boolean isResizable() {
            return true;
    }


    

}
