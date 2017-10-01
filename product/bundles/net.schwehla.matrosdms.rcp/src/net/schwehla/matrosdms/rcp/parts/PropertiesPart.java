
package net.schwehla.matrosdms.rcp.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;

public class PropertiesPart {
	
	
	@Inject IMatrosServiceService matrosService;
	
	private DataBindingContext m_bindingContext;

	Composite switchTo = null;
	
	@Inject
	public void elementChanged(final @Optional @Named(IServiceConstants.ACTIVE_SELECTION) InfoBaseElement elementParameter, UISynchronize sync,
			@Optional @Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Optional INotificationService notificationService) {

		if (elementParameter != null) {
			this.baseelement = elementParameter;
		} else {
			this.baseelement = new InfoBaseElement();
		}
		
		
		if (lblNewLabel != null && !lblNewLabel.isDisposed()) {
			
			if (elementParameter != null) {
				lblNewLabel.setText(elementParameter.getClass().getName());
			} else {
				lblNewLabel.setText("Property");
			}
		}

	    switchTo = compositeBlank;

		if (compositeRoot != null && !compositeRoot.isDisposed()) {

			
		



			
			if (elementParameter instanceof InfoContext) {
				
				InfoContext con = (InfoContext)  elementParameter;
				
				_localDropfieldContext = new InfoContext(con.getIdentifier(), con);
				
				switchTo = compositeInfoContext;
			}
			
//			compositeAdvanced.dispose();
//			
//			// todo: Read advanced properties
//			compositeAdvanced = new Composite(compositeContent, SWT.NONE);
//			compositeAdvanced.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//			compositeAdvanced.setLayout(new GridLayout(1, false));
//			
//			if (baseelement !=  null ) {
//				PropertyUtils.getPropertyDescriptors(baseelement);
//				
//			}
				
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = initDataBindings();
			}
			
			

			
			

	       	 sync.syncExec(new Runnable() {
					
					@Override
					public void run() {
						
		
						if (compositeRoot != null && !compositeRoot.isDisposed()) {
		
							sl_compositeAdvancedArea.topControl = switchTo;
							compositeInfoContext.layout(true,true);
							
							
							switchTo.layout(true,true);
							compositeRoot.getParent().layout(true,true);
							compositeRoot.getParent().setRedraw(true);
							compositeRoot.getParent().update();
						   
							
						}

					};
					
			});
		
       	 

		}
		
		



	}

	InfoBaseElement baseelement;

	@Inject
	ESelectionService selectionService;
	private Text textId;
	private Text textUUID;

	@Inject
	public PropertiesPart() {

	}

	ToolItem btnCommons;
	Composite compositeAdvanced;
	Composite compositeContent ;
	
	StackLayout sl_compositeAdvancedArea = new StackLayout();
	StackLayout sl_compositeContentArea = new StackLayout();
	
	List<ToolItem> toolItemList = new ArrayList<>();


	Composite compositeRoot;

	Color bgColor;
	private Text txtName;
	private Text txtIcon;
	private Text txtDescription;
	private DateTime dateTimeCreated;
	private DateTime dateTimeArchived;
	private Composite compositeBlank;
	private Composite compositeInfoContext;
	
	CLabel lblNewLabel ;
	
	
	InfoContext _localDropfieldContext;
	private Table table;

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		
		_localDropfieldContext = new InfoContext(Identifier.createNEW(), "dummy");
		
		parent.setLayout(new GridLayout(1, false));
		

		compositeRoot = new Composite(parent, SWT.NONE);
		compositeRoot.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.horizontalSpacing = 0;
		compositeRoot.setLayout(gl_composite);
		
		Composite composite = new Composite(compositeRoot, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		composite.setLayout(new GridLayout(2, false));
		
		lblNewLabel = new CLabel(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblNewLabel.setText("Properties");
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmLock = new ToolItem(toolBar, SWT.CHECK);
		tltmLock.setText("Unlock");
		
		ToolItem tltmUpdateElement = new ToolItem(toolBar, SWT.NONE);
		tltmUpdateElement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				
			}
		});
		tltmUpdateElement.setText("Update");
		
		 // Set the background gradient
		lblNewLabel.setBackground(new Color[] { Display.getDefault().getSystemColor(SWT.COLOR_WHITE),
				 Display.getDefault().getSystemColor(SWT.COLOR_GRAY),
				 Display.getDefault().getSystemColor(SWT.COLOR_WHITE),
				 Display.getDefault().getSystemColor(SWT.COLOR_WHITE) }, new int[] { 33, 67, 100 });


		ToolBar compositeButtons = new ToolBar(compositeRoot, SWT.VERTICAL);

		GridLayout gl_compositeButtons = new GridLayout(1, false);
		gl_compositeButtons.marginHeight = 0;
		gl_compositeButtons.marginWidth = 0;
		gl_compositeButtons.horizontalSpacing = 0;
		compositeButtons.setLayout(gl_compositeButtons);
		compositeButtons.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		ToolItem btnIdentifier = new ToolItem(compositeButtons, SWT.RADIO);

		btnIdentifier.setText("Identifer");

		btnCommons = new ToolItem(compositeButtons, SWT.RADIO);

		btnCommons.setText("Commons");
		btnCommons.setSelection(true);
		ToolItem btnAdvanced = new ToolItem(compositeButtons, SWT.RADIO);

		btnAdvanced.setText("Advanced");


		toolItemList.add(btnIdentifier);
		toolItemList.add(btnCommons);
		toolItemList.add(btnAdvanced);



	    compositeContent = new Composite(compositeRoot, SWT.NONE);
	    compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContent.setLayout(sl_compositeContentArea);
		
		ScrolledComposite scrolledCompositeIdentifier = new ScrolledComposite(compositeContent, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeIdentifier.setExpandHorizontal(true);
		scrolledCompositeIdentifier.setExpandVertical(true);
		
		ScrolledComposite scrolledCompositeCommons = new ScrolledComposite(compositeContent, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeCommons.setExpandHorizontal(true);
		scrolledCompositeCommons.setExpandVertical(true);
		
		ScrolledComposite scrolledCompositeAdvanced = new ScrolledComposite(compositeContent, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeAdvanced.setExpandHorizontal(true);
		scrolledCompositeAdvanced.setExpandVertical(true);

		Composite compositeIdentifierContent = new Composite(scrolledCompositeIdentifier, SWT.NONE);
		scrolledCompositeIdentifier.setContent(compositeIdentifierContent);
		
		compositeIdentifierContent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeIdentifierContent.setLayout(new GridLayout(2, false));

		Label lblID = new Label(compositeIdentifierContent, SWT.NONE);
		lblID.setSize(55, 15);
		lblID.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblID.setText("Id");

		textId = new Text(compositeIdentifierContent, SWT.BORDER);
		textId.setEditable(false);

		textId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblUuid = new Label(compositeIdentifierContent, SWT.NONE);
		lblUuid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUuid.setText("uuid");

		textUUID = new Text(compositeIdentifierContent, SWT.BORDER);
		textUUID.setEditable(false);
		textUUID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDatecreated = new Label(compositeIdentifierContent, SWT.NONE);
		lblDatecreated.setText("Date_Created");

		dateTimeCreated = new DateTime(compositeIdentifierContent, SWT.BORDER);
		dateTimeCreated.setEnabled(false);

		Label lblDatearchived = new Label(compositeIdentifierContent, SWT.NONE);
		lblDatearchived.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatearchived.setText("Date_Archived");

		dateTimeArchived = new DateTime(compositeIdentifierContent, SWT.BORDER);
		dateTimeArchived.setEnabled(false);

		Composite compositeCommons = new Composite(scrolledCompositeCommons, SWT.NONE);
		scrolledCompositeCommons.setContent(compositeCommons);
		
		compositeCommons.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeCommons.setLayout(new GridLayout(2, false));

		compositeAdvanced = new Composite(scrolledCompositeAdvanced, SWT.NONE);
		scrolledCompositeAdvanced.setContent(compositeAdvanced);
		compositeAdvanced.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeAdvanced.setLayout(sl_compositeAdvancedArea);
		
		compositeBlank = new Composite(compositeAdvanced, SWT.NONE);
		compositeBlank.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		compositeInfoContext = new Composite(compositeAdvanced, SWT.NONE);
		compositeInfoContext.setLayout(new GridLayout(2, false));
		
		Label lblContext = new Label(compositeInfoContext, SWT.NONE);
		lblContext.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblContext.setText("Context");
		
		Composite grpContext = new Composite(compositeInfoContext, SWT.NONE);
		grpContext.setLayout(new GridLayout(1, false));
		grpContext.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		
		//XXX		
//		
//		
//		  Supplier<List<InfoKategory>> i  = ()-> new ArrayList<>();
//		
//		MatrosTagGroup group = new MatrosTagGroup(grpContext,"wer", 
//				_localDropfieldContext.getDictionary().get(MyGlobalConstants.ROOT_WER) );
//		
//		MatrosTagGroup group2 = new MatrosTagGroup(grpContext, "was", 
//				_localDropfieldContext.getDictionary().get(MyGlobalConstants.ROOT_WAS) );
//		
//		MatrosTagGroup group3 = new MatrosTagGroup(grpContext, "wo", 
//				_localDropfieldContext.getDictionary().get(MyGlobalConstants.ROOT_WO) );
//		
//		
//		Label lblNewLabel_1 = new Label(compositeInfoContext, SWT.NONE);
//		lblNewLabel_1.setText("New Label");
//		new Label(compositeInfoContext, SWT.NONE);
//		
//				
		
		
		
		
		Composite compositeInfoItem = new Composite(compositeAdvanced, SWT.NONE);
		compositeInfoItem.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		

		
		sl_compositeContentArea.topControl = scrolledCompositeCommons;
		sl_compositeAdvancedArea.topControl = compositeBlank;
		
		Label lblBlank = new Label(compositeBlank, SWT.NONE);
		lblBlank.setText("Blank");

		Label lblName = new Label(compositeCommons, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("name");

		txtName = new Text(compositeCommons, SWT.BORDER);

		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblIcon = new Label(compositeCommons, SWT.NONE);
		lblIcon.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIcon.setText("icon");

		txtIcon = new Text(compositeCommons, SWT.BORDER);
		txtIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDescription = new Label(compositeCommons, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description");

		txtDescription = new Text(compositeCommons, SWT.BORDER);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	
		btnAdvanced.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				sl_compositeContentArea.topControl = scrolledCompositeAdvanced;

				compositeContent.layout();

				compositeRoot.redraw();

			}
		});

		btnCommons.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				sl_compositeContentArea.topControl = scrolledCompositeCommons;

				compositeContent.layout();

				compositeRoot.redraw();

			}
		});

		btnIdentifier.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				sl_compositeContentArea.topControl = scrolledCompositeIdentifier;

				compositeContent.layout();

				compositeRoot.redraw();

			}
		});
		
		
		
		m_bindingContext = initDataBindings();

	}

	@Focus
	public void setFocus() {

		if (btnCommons != null && !btnCommons.isDisposed()) {

			boolean focused = false;
			for (ToolItem t : toolItemList) {
				if (t.getSelection()) {
					focused = true;
				}
			}
			
			if (!focused) {
				btnCommons.setSelection(true);
			}
			
		
		}

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextIdObserveWidget = WidgetProperties.text(SWT.Modify).observe(textId);
		IObservableValue identifierpkBaseelementObserveValue = PojoProperties.value("identifier.pk")
				.observe(baseelement);
		bindingContext.bindValue(observeTextTextIdObserveWidget, identifierpkBaseelementObserveValue, null, null);
		//
		IObservableValue observeTextTextUUIDObserveWidget = WidgetProperties.text(SWT.Modify).observe(textUUID);
		IObservableValue identifieruuidBaseelementObserveValue = PojoProperties.value("identifier.uuid")
				.observe(baseelement);
		bindingContext.bindValue(observeTextTextUUIDObserveWidget, identifieruuidBaseelementObserveValue, null, null);
		//
		IObservableValue observeSelectionDateTimeCreatedObserveWidget = WidgetProperties.selection()
				.observe(dateTimeCreated);
		IObservableValue dateCreatedBaseelementObserveValue = PojoProperties.value("dateCreated").observe(baseelement);
		bindingContext.bindValue(observeSelectionDateTimeCreatedObserveWidget, dateCreatedBaseelementObserveValue, null,
				null);
		//
		IObservableValue observeSelectionDateTimeArchivedObserveWidget = WidgetProperties.selection()
				.observe(dateTimeArchived);
		IObservableValue dateArchivedBaseelementObserveValue = PojoProperties.value("dateArchived")
				.observe(baseelement);
		bindingContext.bindValue(observeSelectionDateTimeArchivedObserveWidget, dateArchivedBaseelementObserveValue,
				null, null);
		//
		IObservableValue observeTextTxtNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtName);
		IObservableValue nameBaseelementObserveValue = PojoProperties.value("name").observe(baseelement);
		bindingContext.bindValue(observeTextTxtNameObserveWidget, nameBaseelementObserveValue, null, null);
		//
		IObservableValue observeTextTxtIconObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtIcon);
		IObservableValue iconBaseelementObserveValue = PojoProperties.value("icon").observe(baseelement);
		bindingContext.bindValue(observeTextTxtIconObserveWidget, iconBaseelementObserveValue, null, null);
		//
		IObservableValue observeTextTxtDescriptionObserveWidget = WidgetProperties.text(SWT.Modify)
				.observe(txtDescription);
		IObservableValue descriptionBaseelementObserveValue = PojoProperties.value("description").observe(baseelement);
		bindingContext.bindValue(observeTextTxtDescriptionObserveWidget, descriptionBaseelementObserveValue, null,
				null);
		//
		return bindingContext;
	}
}