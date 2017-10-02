package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.i18n.MatrosMessage;

public abstract class AbstractMatrosTitleAreaDialog extends TitleAreaDialog{

	protected Button buttonOk;
	protected Button cancelButton;
	protected Button updateButton;
	
	@Inject
	@Translation
	MatrosMessage messages;
	
	public AbstractMatrosTitleAreaDialog(Shell parentShell) {
		super(parentShell);
	}
	
	
	@Override
	protected Control createContents(Composite parent) {

		Control dialog = super.createContents(parent);
		
		buildBinding();
		
		return dialog;
		
	}

	
	// not abstract due to windowBuilder
	protected void buildBinding() {
		
	}


	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		 buttonOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		 buttonOk.setEnabled(false);
	
		 cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	

	
	
	  @Override
	  protected boolean isResizable() {
	    return true;
	  }
}
