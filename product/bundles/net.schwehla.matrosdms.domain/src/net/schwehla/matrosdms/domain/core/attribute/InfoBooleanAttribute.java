package net.schwehla.matrosdms.domain.core.attribute;

import net.schwehla.matrosdms.domain.util.Identifier;

public class InfoBooleanAttribute extends AbstractInfoAttribute {

	
	public Boolean getBooleanValue() {
		return boolValue;
	}

	public void setBooleanValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}

	private Boolean boolValue;
	
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
		boolValue = false;
		
	}

	
	
}
