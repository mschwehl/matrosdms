 
package net.schwehla.matrosdms.rcp.handlers;

import javax.inject.Named;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import net.schwehla.matrosdms.rcp.MyGlobalConstants;

public class ResetViewHander {
	
    private static final String FORCE_CLEAR_PERSISTED_STATE = "model.forceClearPersistedState"; //$NON-NLS-1$
	
	
	@Execute
	public void execute(
			@Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS) IEclipsePreferences preferences,
			Logger logger, IWorkbench workbench, UISynchronize sync,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell) {


		sync.asyncExec(new Runnable() {

			@Override
			public void run() {
				boolean restart = MessageDialog.openQuestion(activeShell, "restart?",
						"In this version you need to restart for a reset");
				if (restart) {
					

					try {
						preferences.put(FORCE_CLEAR_PERSISTED_STATE, Boolean.TRUE.toString());
						preferences.flush();
					} catch (BackingStoreException e1) {
						logger.error(e1);
					}
					
					workbench.restart();
				}
			}
		});

		// MPerspectiveStack perspectiveStack = (MPerspectiveStack)
		// modelService.find(MAIN_PERSPECTIVE_STACK_ID, application);
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
		// perspectiveStack.getSelectedElement().setVisible(true);
		// perspectiveStack.setVisible(true);
		//

	}
		
}