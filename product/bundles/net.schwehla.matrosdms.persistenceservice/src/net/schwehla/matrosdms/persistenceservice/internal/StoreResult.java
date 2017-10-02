package net.schwehla.matrosdms.persistenceservice.internal;

public class StoreResult {
	
	String SHA256;
	String cryptSettings;

	public String getSHA256() {
		return SHA256;
	}

	public void setSHA256(String sHA256) {
		SHA256 = sHA256;
	}

	public String getCryptSettings() {
		return cryptSettings;
	}

	public void setCryptSettings(String cryptSettings) {
		this.cryptSettings = cryptSettings;
	}



}
