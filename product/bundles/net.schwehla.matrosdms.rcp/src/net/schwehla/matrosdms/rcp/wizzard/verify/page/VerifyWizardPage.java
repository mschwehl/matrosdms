package net.schwehla.matrosdms.rcp.wizzard.verify.page;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import net.schwehla.matrosdms.domain.util.VerifyMessage;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.wizzard.VerifyWizzard;


@Creatable
public class VerifyWizardPage extends WizardPage {

	@Inject
	@Translation
	MatrosMessage messages;
	

	
	@Override
	public boolean isPageComplete() {
		return true;
	}
	
	
	Group grpFilter ;
	private Button btnFilesize;
	private Button btnContent;
	
	Button btnArchived ;

	/**
	 * Create the wizard.
	 */
	@Inject
	public VerifyWizardPage(@Translation MatrosMessage messages) {
		super("wizardPage");
		setTitle(messages.wizzard_verify_title);
		setDescription(messages.wizzard_verify_description);
	}


	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		
		Composite compositeInboxPage = new Composite(parent, SWT.NULL);

		setControl(compositeInboxPage);
		compositeInboxPage.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(compositeInboxPage, SWT.NONE);
		
		TabItem tbtmInbox = new TabItem(tabFolder, SWT.FILL);
		tbtmInbox.setText(messages.wizzard_verify_ablage);
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText(messages.wizzard_verify_control_description);
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(1, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
	    grpFilter = new Group(compositeTreeAndButtons, SWT.NONE);
		grpFilter.setLayout(new GridLayout(1, false));
		grpFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpFilter.setText(messages.VerifyWizardPage_filter);
		
		btnFilesize = new Button(grpFilter, SWT.RADIO);
		btnFilesize.setText(messages.VerifyWizardPage_Filesize_only);
		
		btnContent = new Button(grpFilter, SWT.RADIO);
		btnContent.setText(messages.VerifyWizardPage_content);
		
		btnArchived = new Button(compositeTreeAndButtons, SWT.CHECK);
		btnArchived.setText(messages.VerifyWizardPage_include_archived);
		 
	
	}


	  public String getUsecase() {
	    if (btnContent.getSelection())
		      return VerifyWizzard.CONTENT;
	    if (btnFilesize.getSelection())
		      return VerifyWizzard.FILESIZE_ONLY;
	    return "";
	  }

	public VerifyMessage applyDataTo(VerifyMessage data) {
		data.setUsecase(getUsecase());
		data.setIncludeArchived(btnArchived.getSelection());
		return data;
		
	}

	  

	  
	  
}
