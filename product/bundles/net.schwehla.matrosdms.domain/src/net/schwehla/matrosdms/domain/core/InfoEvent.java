package net.schwehla.matrosdms.domain.core;

import java.util.Date;

import net.schwehla.matrosdms.domain.core.Identifier;

public class InfoEvent extends InfoBaseElement {
	
	private InfoEvent() {}
	
	public InfoEvent( Identifier key , String name) {
		super(key, name);

	}
	
	
	private Identifier userIdentifier ;
	private InfoItem  item;
	
	private Date dateScheduled;
	private Date dateCompleted;
	
	public Identifier getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(Identifier userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public InfoItem getItem() {
		return item;
	}

	public void setItem(InfoItem item) {
		this.item = item;
	}

	public Date getDateScheduled() {
		return dateScheduled;
	}

	public void setDateScheduled(Date dateScheduled) {
		this.dateScheduled = dateScheduled;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public String getActionscript() {
		return actionscript;
	}

	public void setActionscript(String actionscript) {
		this.actionscript = actionscript;
	}

	private String actionscript;
	
	
}
