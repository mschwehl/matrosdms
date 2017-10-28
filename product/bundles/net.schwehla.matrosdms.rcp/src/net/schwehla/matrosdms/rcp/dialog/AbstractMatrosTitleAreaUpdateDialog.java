package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public abstract class AbstractMatrosTitleAreaUpdateDialog extends AbstractMatrosTitleAreaDialog {

	@Inject Logger logger;
	protected @Inject IMatrosServiceService matrosService;
	
	
	public AbstractMatrosTitleAreaUpdateDialog(Shell parentShell) {
		super(parentShell);
	}
	

	// the element to be created 
	InfoBaseElement lastRecentSavedKategory;

	

	public Identifier getLastSavedElementIdentifier() {
		return lastRecentSavedKategory != null ? lastRecentSavedKategory.getIdentifier() : null;
	}
	
	


	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		 buttonOk = createButton(parent, IDialogConstants.OK_ID, "Update", false);
		 buttonOk.setEnabled(false);
		 
		 cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	
	
	  @Override
	  protected void okPressed() {
	    try {
	    	updateElement();
		} catch (MatrosServiceException e) {
			logger.error("Cannot update: " , e);
		}
	    
	    // closes the dialog
	    super.okPressed();
	  }
	  

	  
	
     protected abstract void updateElement() throws MatrosServiceException;
		  

}
