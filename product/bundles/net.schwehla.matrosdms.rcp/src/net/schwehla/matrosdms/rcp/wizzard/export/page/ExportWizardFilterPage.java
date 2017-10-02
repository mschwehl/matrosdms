package net.schwehla.matrosdms.rcp.wizzard.export.page;

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

import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.wizzard.ExportWizzard;
import net.schwehla.matrosdms.rcp.wizzard.model.ExportWizzardData;


@Creatable
public class ExportWizardFilterPage extends WizardPage {

	@Inject
	@Translation
	MatrosMessage messages;
	

	
	@Override
	public boolean isPageComplete() {
		return true;
	}
	
	
	Group grpFilter ;
	private Button btnAllElements;
	private Button btnRadioButtonContextlist;
	private Button btnSelectedContextelement;
	
	
	/**
	 * Create the wizard.
	 */
	@Inject
	public ExportWizardFilterPage(@Translation MatrosMessage messages) {
		super("wizardPage");
		setTitle(messages.wizzard_export_title);
		setDescription(messages.wizzard_export_description);
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
		tbtmInbox.setText(messages.wizzard_export_summary);
		
		Composite compositeWholeContent = new Composite(tabFolder, SWT.FILL);
		tbtmInbox.setControl(compositeWholeContent);
		compositeWholeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeDescription = new Composite(compositeWholeContent, SWT.NONE);
		compositeDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeDescription.setLayout(new GridLayout(1, false));
		
		Label lblDescription = new Label(compositeDescription, SWT.NONE);
		lblDescription.setText(messages.wizzard_export_summary_description);
		
		Composite compositeTreeAndButtons = new Composite(compositeWholeContent, SWT.NONE);
		compositeTreeAndButtons.setLayout(new GridLayout(1, false));
		compositeTreeAndButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
	    grpFilter = new Group(compositeTreeAndButtons, SWT.NONE);
		grpFilter.setLayout(new GridLayout(1, false));
		grpFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpFilter.setText(messages.ExportWizardFilterPage_filter);
		
		btnAllElements = new Button(grpFilter, SWT.RADIO);
		btnAllElements.setText(messages.ExportWizardFilterPage_groupelement_all_elements_in_database);
		
		btnRadioButtonContextlist = new Button(grpFilter, SWT.RADIO);
		btnRadioButtonContextlist.setText(messages.ExportWizardFilterPage_groupelement_all_elements_in_contextlist);
		
		btnSelectedContextelement = new Button(grpFilter, SWT.RADIO);
		btnSelectedContextelement.setText(messages.ExportWizardFilterPage_groupelement_selected_context);
		 
	
	}
	


	  public String getUsecase() {
	    if (btnAllElements.getSelection())
	      return ExportWizzard.ALL_DATABASE;
	    if (btnRadioButtonContextlist.getSelection())
		      return ExportWizzard.CONTEXTLIST;
	    if (btnSelectedContextelement.getSelection())
		      return ExportWizzard.SELECTION;
	    return "";
	  }




	public ExportWizzardData applyDataTo(ExportWizzardData data) {
		data.setUsecase(getUsecase());
		return data;
	}
	  
	  
}
