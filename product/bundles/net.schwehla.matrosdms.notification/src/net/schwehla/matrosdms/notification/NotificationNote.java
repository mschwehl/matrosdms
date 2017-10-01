package net.schwehla.matrosdms.notification;

import java.util.logging.Level;

public class NotificationNote {

	String heading;
	Level  severity;
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public Level getSeverity() {
		return severity;
	}
	public void setSeverity(Level severity) {
		this.severity = severity;
	}
	
}
