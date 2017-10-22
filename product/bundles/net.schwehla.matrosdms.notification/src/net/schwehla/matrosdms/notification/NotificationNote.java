package net.schwehla.matrosdms.notification;

import java.util.logging.Level;

public class NotificationNote {

	String heading;
	String body;
	Level  severity;
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Level getSeverity() {
		return severity;
	}
	public void setSeverity(Level severity) {
		this.severity = severity;
	}
	
}
