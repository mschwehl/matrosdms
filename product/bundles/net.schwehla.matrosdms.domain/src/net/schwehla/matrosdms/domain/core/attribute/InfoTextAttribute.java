package net.schwehla.matrosdms.domain.core.attribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class InfoTextAttribute extends AbstractInfoAttribute {

	private static final long serialVersionUID = 1L;
	
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
