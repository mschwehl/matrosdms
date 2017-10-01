package net.schwehla.matrosdms.notification.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import net.schwehla.matrosdms.notification.NotificationNote;

public class SuccessNotificationPopup extends AbstractNotificationPopup {

	
	NotificationNote note;
	
	public NotificationNote getNote() {
		return note;
	}


	public void setNote(NotificationNote note) {
		this.note = note;
	}


	public SuccessNotificationPopup(Display display) {
		super(display);
		// TODO Auto-generated constructor stub
	}
	

	  @Override
	    protected void createContentArea(Composite parent) {
	             Label l = new Label(parent, SWT.None);
	             l.setText(note.getHeading());
	    }
	  
	  
}