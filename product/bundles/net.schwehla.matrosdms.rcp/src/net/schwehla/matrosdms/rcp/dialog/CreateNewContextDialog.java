package net.schwehla.matrosdms.rcp.dialog;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.observable.sideeffect.ISideEffect;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;

@Creatable
public class CreateNewContextDialog extends  AbstractMatrosTitleAreaDialog  {

	InfoBaseElement baseElement = new InfoBaseElement();

	
	private Text txtName;
	private Text txtDescription;
	private Text txtIcon;

	@Inject
	public CreateNewContextDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
	        super(shell);
			setShellStyle(getShellStyle() | SWT.SHEET);
		}
	    

	 public InfoBaseElement getBaseElement() {
		return baseElement;
	}


	public void setBaseElement(InfoBaseElement baseElement) {
		this.baseElement = baseElement;
	}


	@Override
	  protected Control createDialogArea(Composite parent) {
			setTitle(messages.createNewContextDialog_title);
			
			
			Composite area = (Composite) super.createDialogArea(parent);
			Composite container = new Composite(area, SWT.NONE);
			container.setLayout(new GridLayout(2, false));
			container.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			Label lblName = new Label(container, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText("name");
			
			txtName = new Text(container, SWT.BORDER);
			txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label lblIcon = new Label(container, SWT.NONE);
			lblIcon.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblIcon.setText("icon");
			
			txtIcon = new Text(container, SWT.BORDER);
			txtIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			Label lblDescription = new Label(container, SWT.NONE);
			lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
			lblDescription.setText("description");
			
			txtDescription = new Text(container, SWT.BORDER | SWT.MULTI);
			txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
			return area;
		}




	@Override
	protected void buildBinding() {

		
		// binding must not be emtpy
        IObservableValue <String> swtNameBinding = WidgetProperties.text(SWT.Modify).observe(txtName);
        
        ISideEffectFactory sideEffectFactory = WidgetSideEffects.createFactory(txtName);
        
    	
		// track whether the selection list is empty or not
		// (viewerSelectionObservable.isEmpty() is a tracked getter!)
		IObservableValue<Boolean> hasNameObservable = ComputedValue
				.create(() -> swtNameBinding.getValue() != null && swtNameBinding.getValue().length() > 0 );

		
		ISideEffect enableOkButtonSideEffect = ISideEffect.create(hasNameObservable::getValue,buttonOk::setEnabled);
	
		
	}
	
	
	
	  @Override
	  protected void okPressed() {
	    saveInput();
	    super.okPressed();
	  }

	// save content of the Text fields because they get disposed
	  // as soon as the Dialog closes
	  private void saveInput() {
		  
		  baseElement.setName(txtName.getText());
		  baseElement.setIcon(txtIcon.getText());
		  baseElement.setDescription(txtDescription.getText());

	  }
	  

	
	  
	} 