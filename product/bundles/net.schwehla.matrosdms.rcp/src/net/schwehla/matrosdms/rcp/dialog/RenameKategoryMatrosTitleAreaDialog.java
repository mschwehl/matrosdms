package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.util.ObjectCloner;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.binding.MatrosBinder;

/**
 * updates a Kategory
 */
@Creatable
public class RenameKategoryMatrosTitleAreaDialog extends AbstractMatrosTitleAreaUpdateDialog  {
	
	@Inject IMatrosServiceService matrosService;
	
	// the current tree
	InfoKategory localKategory ;
	
	private Button btnObject;
	private Text   txtkategoryName;
	private Text   txtparentName;
	private Text   txtkategoryIcon;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	
    @Inject
	public RenameKategoryMatrosTitleAreaDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
        super(shell);
		setShellStyle(getShellStyle() | SWT.SHEET);
	}



	

	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(messages.createNewKategoryDialog_title);
		
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(area, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		

		
		Group group_2 = new Group(composite, SWT.NONE);
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		group_2.setText("Kategorie");
		group_2.setLayout(new GridLayout(2, false));
		
		
		Label labelParent = new Label(group_2, SWT.NONE);
		labelParent.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelParent.setText(messages.createNewKategoryDialog_label_parent);
		
		txtparentName = new Text(group_2, SWT.BORDER | SWT.NO_FOCUS);
		txtparentName.setEditable(false);
		txtparentName.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		
		Label label = new Label(group_2, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText(messages.createNewKategoryDialog_label_name);
		
		txtkategoryName = new Text(group_2, SWT.BORDER);
		txtkategoryName.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		
		Label label_1 = new Label(group_2, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Icon");
		
		txtkategoryIcon = new Text(group_2, SWT.BORDER);
		txtkategoryIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label label_2 = new Label(group_2, SWT.NONE);
		label_2.setText(messages.createNewKategoryDialog_label_object);
		
		btnObject = new Button(group_2, SWT.CHECK);

		txtkategoryName.setFocus();

		// Swt tree have a bug the root needs a pseudoroot to display the first element
		


		return area;
	}


	@Override
	protected void buildBinding() {

	
	
		MatrosBinder bindingNewStype = new MatrosBinder(buttonOk);
		
		MatrosBinder.Twoway firstName =  bindingNewStype.bindComposite(txtkategoryName, true)
			.toModelTwoWay(localKategory, "name", (x) -> { localKategory.setName( (String) x); } ).build();
		
		MatrosBinder.Twoway secondName =  bindingNewStype.bindComposite(txtkategoryIcon, false)
		.toModelTwoWay(localKategory, "icon", (x) -> { localKategory.setIcon( (String) x); } ).build();

	
		bindingNewStype.bindComposite(txtparentName, false)
    			.toModelTwoWay(localKategory.getParents().get(0), "name", (x) -> { localKategory.getParents().get(0).setName( (String) x); } ).build();
		
		
		bindingNewStype.setButtonVisible(buttonOk,firstName);
		

		
	}
	

		public InfoKategory getRoot() {
			return localKategory;
		}

		public void setClonedRoot(InfoKategory root) {
			
			try {
				this.localKategory = new ObjectCloner<InfoKategory>().cloneThroughSerialize(root);
			} catch (Exception e2) {
				logger.error(e2);
				
				close();
				return;
			}

			
		}



		



		private void clearGuiFields() {
			
			btnObject.setSelection(false);
			txtkategoryName.setText("");
			txtparentName.setText("");
			txtkategoryIcon.setText("");
			
			
		}






		@Override
		protected void updateElement() throws MatrosServiceException {
		
			matrosService.updateInfoKategory(localKategory,null);
			lastRecentSavedKategory = localKategory;
			
		}


		
}
