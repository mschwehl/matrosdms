package net.schwehla.matrosdms.rcp.swt.search;



import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

public class MatrosPopup extends MatrosTooltip {
	

	public MatrosPopup(Control control) {
		super(control,SWT.NONE, true);
	}

	Composite control ;


	@Override
	protected Composite createMatrosTooltipContentArea(Event event, Composite parent) {
		setHideOnMouseDown(false);

		
		control = new Composite(parent, SWT.NONE);
		control.setSize(100, 200);
					
		return control;
	
	}

	public void open() {
		
		
		this.show(new Point(0, 0));
	}

	public void toggle() {
		CURRENT_SHELL.setVisible(!CURRENT_SHELL.isVisible());
	}




	
	

}
