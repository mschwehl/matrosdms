package net.schwehla.matrosdms.domain.api;

import net.schwehla.matrosdms.domain.core.attribute.InfoBooleanAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoDateAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoNumberAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoTextAttribute;

public enum E_ATTRIBUTETYPE {

	TEXT(InfoTextAttribute.class.getName()), 
	BOOLEAN(InfoBooleanAttribute.class.getName()),
	DATE(InfoDateAttribute.class.getName()),
	NUMBER(InfoNumberAttribute.class.getName());

	
	private String type;
	
	private E_ATTRIBUTETYPE(String type) {
		this.type = type;
	}
	public String getJavaModelType() {
		return type;
	}
	

}
