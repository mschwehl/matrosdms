package net.schwehla.matrosdms.rcp.swt.filteredcomposite;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public abstract class AbstractPatternFilter extends ViewerFilter
{
	public abstract boolean isLeafMatch(Viewer tableViewer, Object data);
	public abstract boolean isElementSelectable(Object data);
	public abstract void setPattern(String text);
}