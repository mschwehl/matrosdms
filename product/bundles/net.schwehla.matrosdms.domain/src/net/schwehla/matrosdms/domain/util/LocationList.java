package net.schwehla.matrosdms.domain.util;

import java.util.ArrayList;

public class LocationList  extends ArrayList <Location> {

	Location master;
	
	@Override
	public boolean add(Location e) {
	
		if (e.isMaster()) {
			master = e;
		}
		
		return super.add(e);
	}
	
	public Location getMaster() {
		
		if (master == null) {
			throw new NullPointerException("Master not defined!");
		}
		
		return master;
		
	}
	
}
