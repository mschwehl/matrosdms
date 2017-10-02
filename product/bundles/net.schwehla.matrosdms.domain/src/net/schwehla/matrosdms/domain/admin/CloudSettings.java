package net.schwehla.matrosdms.domain.admin;

import net.schwehla.matrosdms.domain.api.IComplete;

public class CloudSettings implements IComplete {
			

	String path;

	CryptSettings cryptSettings = new CryptSettings();;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean isComplete() {
		return path != null;
	}

	public CryptSettings getCryptSettings() {
		return cryptSettings;
	}

	public void setCryptSettings(CryptSettings cryptSettings) {
		this.cryptSettings = cryptSettings;
	}

	
	


}