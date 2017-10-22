package net.schwehla.matrosdms.domain.admin;

import java.io.File;

import net.schwehla.matrosdms.domain.api.IComplete;

public class AppSettings implements IComplete {
	
	String appPath;
	String processedFolder;
	String notprocessedFolder;
	
	public String getNotprocessedFolder() {
		return notprocessedFolder;
	}

	public void setNotprocessedFolder(String notprocessedFolder) {
		this.notprocessedFolder = notprocessedFolder;
	}

	public String getProcessedFolder() {
		return processedFolder;
	}

	public void setProcessedFolder(String processedFolder) {
		this.processedFolder = processedFolder;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	@Override
	public boolean isComplete() {
		return appPath != null;
	}
	
	
	public String getDbPath() {
		
		return appPath + File.separator + "db";
	}
	

}
