package net.schwehla.matrosdms.domain.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoOrginalstore extends InfoBaseElementWithOrdinal {
	
	private static final long serialVersionUID = 1L;

	private InfoOrginalstore() {
	}
	
	String shortname;
	
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public InfoOrginalstore(Identifier key, String name) {
		super(key, name);
	}
	
}
