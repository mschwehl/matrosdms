 
package net.schwehla.matrosdms.rcp.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.rcp.wizzard.ExportWizzard;

public class ExportHandler {
	
	@Execute
	public void execute( IEclipseContext context, Shell activeShell) {
	

	   	WizardDialog dialog;
	   	
    	ExportWizzard setupNewWizzard = ContextInjectionFactory.make(ExportWizzard.class, context);
    	dialog = new WizardDialog(activeShell, setupNewWizzard);
    	dialog.create();
		dialog.open();

		
	}
		
}