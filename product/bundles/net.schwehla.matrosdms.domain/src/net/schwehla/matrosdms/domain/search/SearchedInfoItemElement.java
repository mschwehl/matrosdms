package net.schwehla.matrosdms.domain.search;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.util.Identifier;

public class SearchedInfoItemElement extends InfoItem {
	
	
	public SearchedInfoItemElement(InfoContext context, Identifier key , String name) {
		super(context, key, name);
	}

	String  contextName;
	String  searchFilterString;
	
	public String getSearchFilterString() {
		return searchFilterString;
	}

	public void setSearchFilterString(String searchFilterString) {
		this.searchFilterString = searchFilterString;
	}

	boolean effectiveArchived;
	

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public boolean isEffectiveArchived() {
		return effectiveArchived;
	}

	public void setEffectiveArchived(boolean effectiveArchived) {
		this.effectiveArchived = effectiveArchived;
	}
}
