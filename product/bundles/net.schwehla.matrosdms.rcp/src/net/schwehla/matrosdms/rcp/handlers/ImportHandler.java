 
package net.schwehla.matrosdms.rcp.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.rcp.wizzard.OpenExistingWizzard;

public class ImportHandler {
	@Execute
	public void execute( IEclipseContext context, Shell activeShell) {
		
	   	WizardDialog dialog;
	   	
	   	OpenExistingWizzard setupNewWizzard = ContextInjectionFactory.make(OpenExistingWizzard.class, context);
    	dialog = new WizardDialog(activeShell, setupNewWizzard);
    	dialog.create();
		dialog.open();

		
	}
		
}