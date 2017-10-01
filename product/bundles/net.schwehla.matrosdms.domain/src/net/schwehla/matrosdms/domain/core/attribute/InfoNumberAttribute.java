package net.schwehla.matrosdms.domain.core.attribute;

import java.text.DecimalFormat;

import net.schwehla.matrosdms.domain.util.Identifier;

public class InfoNumberAttribute extends AbstractInfoAttribute {

	Double value;

	DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
	
	
	
	public InfoNumberAttribute(Identifier uuid, String Name) {
		super(uuid, Name);
	}
	
	@Override
	public String getTextDescription() {

		if (value != null) {
			
			String t =  df2.format(value);
			
			if (getType().getUnit() != null) {
				t += " " + getType().getUnit(); 
			}
			
			return t;
		}
		
		return "";
	}
	
	@Override
	public void setValue(Object value) {
		
		if (value != null) {
			this.value = (Double) value;
		}
		

	}
	@Override
	public Object getValue() {
		
		if (value != null ) {
			return new Double(value);
		}
		
		return null;

	}
	
	
	@Override
	public void init() {
		value = 0.0d;
	}
	

}
