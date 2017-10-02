package net.schwehla.matrosdms.rcp.swt.labelprovider;

import java.util.function.Function;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;

public class MultilineLabelProvider <T> extends OwnerDrawLabelProvider {
	
	Table table;
	Function <T,StringBuffer> suplier;
	 
	public MultilineLabelProvider(Table table, Function <T,StringBuffer> suplier) {
		this.table = table;
		this.suplier = suplier;
	}
	

	// http://stackoverflow.com/questions/26463520/eclipse-ownerdrawlabelprovider-makes-selection-background-dark-blue
	@Override
	protected void erase(Event event, Object element){
	}
	

	@Override

	protected void measure(Event event, Object element) {

		// Element is filtered
		if (element == null) {
			return;
		}

		event.width = table.getColumn(event.index).getWidth();

		if (event.width == 0) {
			return;
		}

		T entry = (T) element;

		StringBuffer sb = suplier.apply(entry);
		
		Point size = event.gc.textExtent(sb.toString().trim());

		event.height = size.y;

	}

	@Override

	protected void paint(Event event, Object element) {

		// Element is filtered

		if (element == null) {

			return;

		}

		T entry = (T) element;

		StringBuffer sb = suplier.apply(entry);
		
		event.gc.drawText(sb.toString().trim(), event.x, event.y, true);

	}

}
	



