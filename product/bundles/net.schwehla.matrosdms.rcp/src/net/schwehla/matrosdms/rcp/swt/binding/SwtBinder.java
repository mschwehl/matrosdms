package net.schwehla.matrosdms.rcp.swt.binding;

import java.util.function.Consumer;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.sideeffect.ISideEffectFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetSideEffects;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class SwtBinder  {
	
	ISideEffectFactory sideEffectFactory;
	
	public SwtBinder(Widget widget) {
		sideEffectFactory = WidgetSideEffects.createFactory(widget);
	}

	public From from(Text get_swtTxtItemname) {
		
		From from = new From(get_swtTxtItemname);
		return from;
	}
	
    IObservableValue <String> swtItemName ;

	 IObservableValue<String> myModelUser;
    Consumer<String> f ;
    Object infoItem;
	 
	Text fromWidget;
	
	class From {


		
		public From(Text fromWidget) {
			SwtBinder.this.fromWidget = fromWidget;
		}

		
		
		public void create() {
			

			 sideEffectFactory.create(myModelUser::getValue, swtItemName::setValue);
			
	         sideEffectFactory.create(swtItemName::getValue, f);
         
			
		}

		public From toway(String name, Object infoItem, Consumer<String> f) {
			SwtBinder.this.myModelUser = PojoProperties.value(name).observe(infoItem);  
			SwtBinder.this.infoItem = infoItem;
			SwtBinder.this.f = f;
			return this;
		}
		
	}
	
	

}
