package net.schwehla.matrosdms.domain.core.infoitem;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;

/**
 *  An Info-Container with attachements
 * 
 * @author S850
 */

// TODO: Think about byte[] content -> Performance 

public class FileAttachement  extends InfoBaseElement {
	
	private String filename;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
