package net.schwehla.matrosdms.rcp.binding;

import java.util.function.Consumer;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class MatrosBinder {
	
	Control parent;
	
	Consumer invokeConsumer ;
	
    ISideEffectFactory sideEffectFactory ;
	
	Display display = Display.getCurrent();
	Color defaultColor;
	
	public MatrosBinder(Control parent) {
		this.parent = parent;
		sideEffectFactory = WidgetSideEffects.createFactory(parent);
	}

	public BindModel bindComposite(Text composite, boolean required) {
		
		
		ISWTObservableValue swtElement = WidgetProperties.text(SWT.Modify).observe(composite);
		
		if (required) {

			WritableValue<IStatus> firstNameValidation = new WritableValue<>();
			
			sideEffectFactory.create(() -> {
				String firstName = (String) swtElement.getValue();
				if (firstName != null && firstName.isEmpty()) {
					firstNameValidation.setValue(ValidationStatus.warning("Element is required"));
					composite.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
					
					return;
				}
				
				composite.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
				firstNameValidation.setValue(Status.OK_STATUS);
			});
			
			ControlDecorationSupport.create(firstNameValidation, SWT.TOP | SWT.LEFT, swtElement);
			
		}
				
		parent.addDisposeListener(e-> swtElement.dispose());

		
		BindModel bm = new BindModel(swtElement);
		
		return bm;
		 
	}
	
	public class BindModel {
		
		ISWTObservableValue swtElement;
		IObservableValue    myDBPaht;
		Consumer consumer;
		
		public BindModel(ISWTObservableValue swtElement) {
			this.swtElement = swtElement;
		}
		
		 
		public Twoway toModelTwoWay(Object model, String path, Consumer consumer) {
			
			myDBPaht = PojoProperties.value(path).observe(model);
			this.consumer =consumer;
			parent.addDisposeListener( e->myDBPaht.dispose());
			
			Twoway t = new Twoway(BindModel.this);
			return t;
			
		}
		

		
	}; 
	
	public class Twoway {

		public BindModel bindModel;
		public Twoway(BindModel bindModel) {
			this.bindModel = bindModel;
		}
		
		
		public Twoway build() {
	        sideEffectFactory.create(bindModel.myDBPaht::getValue, bindModel.swtElement::setValue);
	        
	        sideEffectFactory.create(bindModel.swtElement::getValue , (x) -> {
	        	
	        	if (x != null && ("" + x.toString()).length() > 0 ) {
	        		 bindModel.consumer.accept(x);
	        	} else {
	        		 bindModel.consumer.accept(null);
	        	}
	        	
	        	if (invokeConsumer != null) {
	        		invokeConsumer.accept(x);
	        	}
	        	
	        });
	        
	        return Twoway.this;
	        
		}
		
	}

			
	public void setButtonVisible(Button ok, Twoway ... conditions) {
	
		
		// track whether the selection list is empty or not
		// (viewerSelectionObservable.isEmpty() is a tracked getter!)
		IObservableValue<Boolean> hasSelectionObservable = ComputedValue
				.create(() -> { for (Twoway xy : conditions) {
					
					if (xy.bindModel.swtElement.getValue() == null 
							|| "".equals(xy.bindModel.swtElement.getValue().toString().trim() ) ) {
						return false;
					}
					
				} return true;
			}
		);

		sideEffectFactory.create(hasSelectionObservable::getValue,ok::setEnabled);
		
	}

	public void invokeAfterChange(Consumer invokeConsumer ) {
		
		this.invokeConsumer = invokeConsumer;
		
	}

}

