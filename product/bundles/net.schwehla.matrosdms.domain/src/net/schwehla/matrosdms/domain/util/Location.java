package net.schwehla.matrosdms.domain.util;

import java.net.URL;

public class Location {
	
	int 	prio;
	boolean crypted;
	boolean local;
	
	boolean master;
	
	
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
	URL 	rootUrl;
	

	
	public int getPrio() {
		return prio;
	}
	public void setPrio(int prio) {
		this.prio = prio;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	public boolean isCrypted() {
		return crypted;
	}
	public void setCrypted(boolean crypted) {
		this.crypted = crypted;
	}
	public URL getRootUrl() {
		return rootUrl;
	}
	public void setRootUrl(URL rootUrl) {
		this.rootUrl = rootUrl;
	}

	
}
