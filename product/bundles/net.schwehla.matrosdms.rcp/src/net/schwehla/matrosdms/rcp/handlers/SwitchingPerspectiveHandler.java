 
package net.schwehla.matrosdms.rcp.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SwitchingPerspectiveHandler {
	

	
	@Execute
	public void execute(IEclipseContext context, @Optional @Named("net.schwehla.matrosdms.rcp.commandparameter.id") String perspectiveID) {
	
			openPerspective(context,perspectiveID); //$NON-NLS-1$
	}
	

	/**
	 * Opens the perspective with the given identifier.
	 * 
	 * @param perspectiveId
	 *            The perspective to open; must not be <code>null</code>
	 * @throws ExecutionException
	 *             If the perspective could not be opened.
	 */
	private final void openPerspective(IEclipseContext context, String perspectiveID) {
		MApplication application = context.get(MApplication.class);
		EPartService partService = context.get(EPartService.class);
		EModelService modelService = context.get(EModelService.class);
		
		List<MPerspective> perspectives = modelService.findElements(application, perspectiveID, MPerspective.class, null);
		partService.switchPerspective(perspectives.get(0));
	}
	
	
}