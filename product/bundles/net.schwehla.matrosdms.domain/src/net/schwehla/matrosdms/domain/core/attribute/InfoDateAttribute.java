package net.schwehla.matrosdms.domain.core.attribute;

import java.text.DateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoDateAttribute extends AbstractInfoAttribute {

	private static final long serialVersionUID = 1L;
	
	Date dateValue;
	
	public InfoDateAttribute(Identifier uuid, String Name) {
		super(uuid, Name);
	}
	
	DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

	
	@Override
	public String getTextDescription() {
		
		if (dateValue != null) {
			return df.format(dateValue);
		}
		
		return "";
	}

	@Override
	public void setValue(Object value) {
		dateValue = (Date) value;
	}

	@Override
	public Object getValue() {
		return dateValue;
		
	}

	@Override
	public void init() {
		dateValue = new Date();
	}

	
	
}
