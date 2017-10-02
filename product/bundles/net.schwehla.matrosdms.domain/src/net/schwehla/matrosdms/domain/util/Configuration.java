package net.schwehla.matrosdms.domain.util;

public class Configuration {
	
	LocationList locationList = new LocationList();
	
	public Configuration() {
		
		Location localStorage = new Location();
		
		localStorage.setCrypted(false);
		localStorage.setLocal(true);
		localStorage.setPrio(0);
		localStorage.setMaster(true);
		
		locationList.add(localStorage);
	
	}


}
