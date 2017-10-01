package net.schwehla.matrosdms.rcp.parts;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import net.schwehla.matrosdms.domain.core.InfoContext;

public class VisibleFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof InfoContext) {
			return ((InfoContext) element).isVisible();
		}
		
		return false;
	}

}
