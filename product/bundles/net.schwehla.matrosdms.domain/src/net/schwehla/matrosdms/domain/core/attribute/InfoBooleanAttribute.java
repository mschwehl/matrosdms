package net.schwehla.matrosdms.domain.core.attribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoBooleanAttribute extends AbstractInfoAttribute {

	private static final long serialVersionUID = 1L;
	private Boolean boolValue;
	
	public Boolean getBooleanValue() {
		return boolValue;
	}

	public void setBooleanValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}
	
	public InfoBooleanAttribute(Identifier uuid, String Name) {
		super(uuid, Name);
	}

	@Override
	public String getTextDescription() {
		return "" + Boolean.TRUE.equals(boolValue);
	}

	@Override
	public void setValue(Object value) {
		setBooleanValue((Boolean) value);
		
	}
	
	@Override
	public Object getValue() {
		return boolValue;
	}

	@Override
	public void init() {
		boolValue = true;
		
	}

	
	
}
