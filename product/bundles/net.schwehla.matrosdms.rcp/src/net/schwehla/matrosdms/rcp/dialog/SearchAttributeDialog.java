package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.parts.helper.InfoAttributeListWrapper;

@Creatable
public class SearchAttributeDialog extends Dialog {
	
	@Inject
	InfoAttributeListWrapper art;
	


	@Override
	protected void setShellStyle(int newShellStyle) {           
	    super.setShellStyle(SWT.RESIZE | SWT.MODELESS| SWT.BORDER | SWT.TITLE);
	    setBlockOnOpen(false);
	}
	
	@Override
	protected void createButtonsForButtonBar(final Composite parent)
	{ 
	  GridLayout layout = (GridLayout)parent.getLayout();
	  layout.marginHeight = 0;
	}
	
	// without inject not createable
    @Inject
	public SearchAttributeDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
    	

		super(shell);
	
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		
		Composite control  = (Composite) super.createDialogArea(parent);
			art.init(control);
		
		return control;
		
		
					
	}
	
	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return super.close();
	}
		 

}
