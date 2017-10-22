package net.schwehla.matrosdms.notification.internal;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.notification.INotificationService;
import net.schwehla.matrosdms.notification.NotificationNote;

public class NotificationServiceImpl implements INotificationService {

	@Inject
	@Active Shell shell;
	
	@Override
	public void openPopup(NotificationNote note) {
	
		
		SuccessNotificationPopup popup = new SuccessNotificationPopup(shell.getDisplay());
		popup.setDelayClose(3000L);
		popup.setFadingEnabled(false);
		popup.setNote(note);
		
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				popup.close();
			}
		});

		popup.open();

		
		
	}


	

}
