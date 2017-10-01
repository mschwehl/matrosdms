package net.schwehla.matrosdms.domain.core;

import net.schwehla.matrosdms.domain.api.IStorableInfoItemContainerInterface;

public class InfoItemListProxy implements IStorableInfoItemContainerInterface {

	public InfoContext context;

	public InfoItemListProxy(InfoContext context) {
		this.context = context;
	}

	private int _count;

	@Override
	public int getCount() {
		return _count;
	}

	public void setCount(int _count) {
		this._count = _count;
	}

	@Override
	public InfoContext getContext() {
		return context;
	}

	public void setContext(InfoContext context) {
		this.context = context;
	}

}
