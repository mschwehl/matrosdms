package net.schwehla.matrosdms.rcp.preferences;

import java.io.File;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;

@Creatable
public class NotProcessedFolderPreferencePage extends PreferencePage {
	
    public NotProcessedFolderPreferencePage()
    {
        super();
        setTitle("Not Processed Folder");
    }
    
	@Inject
	@Translation
	MatrosMessage messages;
	
	
	private Text txtPath;


	
	@Override
	public boolean performOk() {

		performApply() ;
		
		
    	 String path = txtPath.getText();

    	 File f = new File ("" + path);
    	 
    	 return f.exists();
    	
	}
	
	
	
	@Override
	protected void performApply() {
		
        
     	 String path = txtPath.getText();
     	 getPreferenceStore().setValue(MyGlobalConstants.Preferences.NOT_PROCESSED_PATH, path);

		
	}
	
	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		
		
		
		
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		
		Composite compositeTreeAndButtons = new Composite(container, SWT.NONE);
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeTreeAndButtons.setLayout(new GridLayout(2, false));
		
		Label lblRoot = new Label(compositeTreeAndButtons, SWT.NONE);
		lblRoot.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblRoot.setText("Path");
		
		txtPath = new Text(compositeTreeAndButtons, SWT.BORDER);
		txtPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
	
		init();


		
		return container;
	}

	private void init() {
	
		 String path = getPreferenceStore().getString(MyGlobalConstants.Preferences.NOT_PROCESSED_PATH);
		    
		 txtPath.setText(path != null ? path : "");
	
		
		
	}

	   @Override
	    public String toString() {
	    	return "4_not_processedFolder";
	    }
		
	

}
