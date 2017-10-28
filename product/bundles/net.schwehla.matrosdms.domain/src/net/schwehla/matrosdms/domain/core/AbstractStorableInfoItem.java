package net.schwehla.matrosdms.domain.core;

import net.schwehla.matrosdms.domain.core.Identifier;

public abstract class AbstractStorableInfoItem extends InfoBaseElement {

	private static final long serialVersionUID = 1L;
	
	private InfoItem parentContainer;

	public AbstractStorableInfoItem(Identifier key, String name) {
		super(key,  name);
	}

	public InfoItem getParentContainer() {
		return parentContainer;
	}

	public void setParentContainer(InfoItem parentContainer) {
		this.parentContainer = parentContainer;
	}



}
