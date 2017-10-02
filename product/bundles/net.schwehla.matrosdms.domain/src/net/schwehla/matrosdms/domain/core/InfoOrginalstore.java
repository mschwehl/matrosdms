package net.schwehla.matrosdms.domain.core;

import net.schwehla.matrosdms.domain.util.Identifier;

public class InfoOrginalstore extends InfoBaseElement {
	
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
