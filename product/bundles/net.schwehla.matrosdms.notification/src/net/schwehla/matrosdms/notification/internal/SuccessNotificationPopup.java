package net.schwehla.matrosdms.notification.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import net.schwehla.matrosdms.notification.NotificationNote;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class SuccessNotificationPopup extends AbstractNotificationPopup {

	
	NotificationNote note;
	private Text textContent;
	
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
	             
	             if (note.getBody() != null) {
		             textContent = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		             textContent.setEditable(false);
		             
		             textContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		             
		             textContent.setText(note.getBody());
	             }
	             

	          
	    }
	  
	  
}