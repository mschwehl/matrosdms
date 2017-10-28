package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public abstract class AbstractMatrosTitleAreaCreateAndNewDialog extends AbstractMatrosTitleAreaDialog {

	@Inject Logger logger;
	protected @Inject IMatrosServiceService matrosService;
	
	
	public AbstractMatrosTitleAreaCreateAndNewDialog(Shell parentShell) {
		super(parentShell);
	}
	
	
	
	protected Button buttonCreateAndNew;

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
		
		 buttonOk = createButton(parent, IDialogConstants.OK_ID, "New", false);
		 buttonOk.setEnabled(false);

		 buttonCreateAndNew = createButton(parent, IDialogConstants.NEXT_ID, "Create \\& New", true);
		 buttonCreateAndNew.setEnabled(false);
		 
		 buttonCreateAndNew.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    try {
					createAndNew();
				} catch (MatrosServiceException e1) {
					logger.error(e1);
					close();
				}
			}
		 });
		 
		 cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	
	
	  @Override
	  protected void okPressed() {
	    try {
			createNewElementInDatabase();
		} catch (MatrosServiceException e) {
			logger.error("Cannot create category: " , e);
		}
	    
	    // closes the dialog
	    super.okPressed();
	  }
	  

	  
	
     protected void createAndNew() throws MatrosServiceException {
		    createNewElementInDatabase();
	 }
		  
	 protected abstract void createNewElementInDatabase() throws MatrosServiceException;
	 

}
