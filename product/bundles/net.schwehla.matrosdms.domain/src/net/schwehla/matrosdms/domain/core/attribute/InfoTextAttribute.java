package net.schwehla.matrosdms.domain.core.attribute;

import net.schwehla.matrosdms.domain.util.Identifier;

public class InfoTextAttribute extends AbstractInfoAttribute {

	
	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	private String strValue;
	
	public InfoTextAttribute(Identifier uuid, String Name) {
		super(uuid, Name);
	}

	@Override
	public String getTextDescription() {
		
		if (strValue !=null) {
			return strValue;
		}
		
		return "";
	}

	@Override
	public void setValue(Object value) {
		setStrValue((String) value);
		
	}
	
	@Override
	public Object getValue() {
		return strValue;
	}

	@Override
	public void init() {
		strValue = "";
		
	}
	
	
}
