package net.schwehla.matrosdms.domain.util;

import java.io.Serializable;

public class InboxFile implements Serializable {

	transient String absolutePath;
	
   
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	    String filename = null;
      
		public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}

