package net.schwehla.matrosdms.domain.util;

import java.util.List;


/**
 * Entspricht einem Zip-Container mit allen Dokumenten
 * 
 * @author S850
 */
public class TaggableContainerStore {

	private String id;
	private String contextId;
	private String typ;
	private List<String> itemIdList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public List<String> getItemIdList() {
		return itemIdList;
	}

	public void setItemIdList(List<String> itemIdList) {
		this.itemIdList = itemIdList;
	}

}
