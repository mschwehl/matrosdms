package net.schwehla.matrosdms.rcp.parts.include;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoItem;

@Creatable
public class ItemPartFixedAttributesGroup{
	private DataBindingContext m_bindingContext;
	
	
	@Inject
	IEclipseContext context;
	

	

	@Inject
	public ItemPartFixedAttributesGroup() {
		
	}
	
	InfoItem  _localInfoItem;
	private Text issueDate;

	
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
		
		Group composite_fixedAttributes_form = new Group(compositePane, SWT.NONE);
		composite_fixedAttributes_form.setText("FixedAttributes");
		composite_fixedAttributes_form.setLayout(new GridLayout(2, false));
		composite_fixedAttributes_form.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label swt_lblIssuedate = new Label(composite_fixedAttributes_form, SWT.NONE);
		swt_lblIssuedate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		swt_lblIssuedate.setText("Issuedate");
		
		issueDate = new Text(composite_fixedAttributes_form, SWT.BORDER);
		new Label(compositePane, SWT.NONE);

		m_bindingContext = initDataBindings();
		
	    parent.addDisposeListener(new DisposeListener() {
	        public void widgetDisposed(DisposeEvent e) {
	        	m_bindingContext.dispose();
	        }
	    });
		
	
	}
	
	




	public void refresh(boolean update) {
		
		if (issueDate != null && !issueDate.isDisposed()) {
			issueDate.redraw();
		}

	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue textIssueDateObserveValue = WidgetProperties.text(new int[]{SWT.Modify, SWT.FocusOut}).observe(issueDate);
		IObservableValue issueDate_localInfoItemObserveValue = PojoProperties.value("issueDate").observe(_localInfoItem);
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setConverter(new StringToDateConverter());
		strategy.setAfterGetValidator(new DateValidator());
		UpdateValueStrategy strategy_1 = new UpdateValueStrategy();
		strategy_1.setConverter(new DateToStringConverter());
		bindingContext.bindValue(textIssueDateObserveValue, issueDate_localInfoItemObserveValue, strategy, strategy_1);
		//
		

		
		
		return bindingContext;
	}
}
