package net.schwehla.matrosdms.rcp.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.i18n.MatrosMessage;

public class AboutHandler {
	
	
	@Inject
	EModelService modelService;
	
	@Inject
	IEventBroker eventBroker;

	
	@Inject
	EPartService partService;

	@Inject MApplication app;
		
	@Inject
	@Translation
	MatrosMessage messages;

	@Execute
	public void execute(Shell shell) {
		MessageDialog.openInformation(shell, messages.abouthandler_heading, messages.abouthandler_detail);
	}

	
}
