 
package net.schwehla.matrosdms.rcp.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.rcp.wizzard.VerifyWizzard;

public class VerifyHandler {
	@Execute
	public void execute(IEclipseContext context, Shell activeShell) {
		
		
	   	WizardDialog dialog;
	   	
    	VerifyWizzard setupNewWizzard = ContextInjectionFactory.make(VerifyWizzard.class, context);
    	dialog = new WizardDialog(activeShell, setupNewWizzard);
    	dialog.create();
		dialog.open();

	}
		
}

