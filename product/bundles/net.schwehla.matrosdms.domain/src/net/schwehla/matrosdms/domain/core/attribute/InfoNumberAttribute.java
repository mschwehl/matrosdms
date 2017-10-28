package net.schwehla.matrosdms.domain.core.attribute;

import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoNumberAttribute extends AbstractInfoAttribute {

	private static final long serialVersionUID = 1L;
	
	Double value;

	@XmlTransient
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
