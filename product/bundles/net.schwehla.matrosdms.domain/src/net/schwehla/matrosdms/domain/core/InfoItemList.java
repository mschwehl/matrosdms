package net.schwehla.matrosdms.domain.core;

import java.util.ArrayList;

import net.schwehla.matrosdms.domain.api.IStorableInfoItemContainerInterface;


public class InfoItemList extends ArrayList<InfoItem>
		implements IStorableInfoItemContainerInterface {

	private static final long serialVersionUID = 1L;

	public InfoContext context;

	@Override
	public InfoContext getContext() {
		return context;
	}

	public void setContext(InfoContext context) {
		this.context = context;
	}

	public InfoItemList(InfoContext Context) {
		this.context = Context;
	}

	@Override
	public int getCount() {
		return size();
	}

}
