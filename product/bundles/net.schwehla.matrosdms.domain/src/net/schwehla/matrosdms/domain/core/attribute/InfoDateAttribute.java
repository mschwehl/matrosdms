package net.schwehla.matrosdms.domain.core.attribute;

import java.text.DateFormat;
import java.util.Date;

import net.schwehla.matrosdms.domain.util.Identifier;

public class InfoDateAttribute extends AbstractInfoAttribute {

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
