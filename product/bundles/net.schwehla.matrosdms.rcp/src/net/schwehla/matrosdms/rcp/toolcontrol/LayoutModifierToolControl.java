package net.schwehla.matrosdms.rcp.toolcontrol;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * This is a specialized tool control used by the TrimBarLayout to modify the layout mechanisms.
 */
public class LayoutModifierToolControl {
	@PostConstruct
	void createWidget(Composite parent, MToolControl tc) {
		Composite comp = new Composite(parent, SWT.NO_BACKGROUND) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.widgets.Control#computeSize(int, int)
			 */
			@Override
			public Point computeSize(int wHint, int hHint, boolean flushCache) {
				return new Point(0, 0);
			}
		};
		comp.setSize(0, 0);
	}
}