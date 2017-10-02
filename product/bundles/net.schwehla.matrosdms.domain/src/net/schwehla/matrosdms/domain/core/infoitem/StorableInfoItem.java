package net.schwehla.matrosdms.domain.core.infoitem;

import java.util.ArrayList;
import java.util.List;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.util.Identifier;

/**
 *  An Info-Container with attachements
 * 
 * @author S850
 */

// TODO: Think about byte[] content -> Performance 

public class StorableInfoItem  extends InfoBaseElement {

	
	private InfoItem parentContainer;

	private List <FileAttachement> attachementList = new ArrayList();


	public List<FileAttachement> getAttachementList() {
		return attachementList;
	}

	public void setAttachementList(List<FileAttachement> attachementList) {
		this.attachementList = attachementList;
	}

	public InfoItem getParentContainer() {
		return parentContainer;
	}

	public void setParentContainer(InfoItem parentContainer) {
		this.parentContainer = parentContainer;
	}

	public StorableInfoItem(Identifier key,   String name) {
		super(key,  name);
	}




}
