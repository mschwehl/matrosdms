package net.schwehla.matrosdms.rcp.parts.include;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.swt.composite.MatrosTagGroup;

@Creatable
public class ItemPartMetadataGroup    {
	
	
	@Inject ESelectionService selectionService;

	@Inject
	private MDirtyable _dirty;

	@Inject
	private IEventBroker eventBroker;

	 
	
	Map<Identifier, TableViewer> _tagGraphMap;


	@Inject
	MApplication application;


	@Inject IMatrosServiceService matrosService;
	
	@Inject
	IEclipseContext context;
	
	MatrosTagGroup g;
	

	private Text _swtTextContext;
	private Text _swtTxtItemname;
	private Text _swtFileName;
	

	public Text get_swtFileName() {
		return _swtFileName;
	}

	public void set_swtFileName(Text _swtFileName) {
		this._swtFileName = _swtFileName;
	}

	public Text get_swtTextContext() {
		return _swtTextContext;
	}

	public void set_swtTextContext(Text _swtTextContext) {
		this._swtTextContext = _swtTextContext;
	}

	public Text get_swtTxtItemname() {
		return _swtTxtItemname;
	}

	public void set_swtTxtItemname(Text _swtTxtItemname) {
		this._swtTxtItemname = _swtTxtItemname;
	}


	
	public ItemPartMetadataGroup() {
		
	}
	
	InfoItem  _localInfoItem;

	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void appendElementsToParentComposit(InfoItem localInfoItem, Composite parent ) {

		this._localInfoItem = localInfoItem;
		
		
		Composite compositePane = new Composite(parent, SWT.FILL);
		
		GridData groupDocumentsLayoutPane = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		groupDocumentsLayoutPane.minimumHeight  = 50;
		
		compositePane.setLayout(new GridLayout(2, false));
		compositePane.setLayoutData(groupDocumentsLayoutPane);
		
		
		
		
		Group composite_metadata_form = new Group(compositePane, SWT.NONE);
		composite_metadata_form.setText("Metadata");
		composite_metadata_form.setLayout(new GridLayout(2, false));
		composite_metadata_form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblContext = new Label(composite_metadata_form, SWT.NONE);
		lblContext.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblContext.setText("Context");
		
		_swtTextContext = new Text(composite_metadata_form, SWT.BORDER | SWT.MULTI);
		_swtTextContext.setEnabled(false);
		_swtTextContext.setEditable(false);
		_swtTextContext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFilename = new Label(composite_metadata_form, SWT.NONE);
		lblFilename.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilename.setText("Filename");
		
		_swtFileName = new Text(composite_metadata_form, SWT.BORDER | SWT.MULTI);
		_swtFileName.setEnabled(false);
		_swtFileName.setEditable(false);
		_swtFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label swt_lblName = new Label(composite_metadata_form, SWT.NONE);
		swt_lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		swt_lblName.setText("Name");
		
		_swtTxtItemname = new Text(composite_metadata_form, SWT.BORDER);
		_swtTxtItemname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		 g = MatrosTagGroup.buildTagGroupViaEclipseContext(context, compositePane,_localInfoItem.getTypList() );

	}
	
	

	public void refresh(boolean update) {
		
		if (g != null && !g.isDisposed()) {
			g.refresh();
		}

	}

	
}
