package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

@Creatable
public class MoveItemToNewContextDialog extends  AbstractMatrosTitleAreaDialog  {

	
	@Inject
	public MoveItemToNewContextDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
	        super(shell);
			setShellStyle(getShellStyle() | SWT.SHEET);
		}
	    


	@Override
	  protected Control createDialogArea(Composite parent) {
			setTitle(messages.moveItemToNewContextDialog_title);
			
			
			Composite area = (Composite) super.createDialogArea(parent);
			
			
					
			return area;
		}




	@Override
	protected void buildBinding() {

		
	}
	
	
	  @Override
	  protected void okPressed() {
	    saveInput();
	    super.okPressed();
	  }

	// save content of the Text fields because they get disposed
	  // as soon as the Dialog closes
	  private void saveInput() {
	
	  }
	  
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			
			buttonOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
			cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);

		}
		

	
	  
	} 