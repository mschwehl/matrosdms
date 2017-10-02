package net.schwehla.matrosdms.rcp.dnd;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;



public class MyDropListener <T> extends ViewerDropAdapter {

	
	Consumer consumer;
	
	public void addChangelistener(Consumer consumer) {
		this.consumer = consumer;
	}
    
	
	
	Transfer[] transferTypes = null;
	 
	public MyDropListener(Viewer viewer, Transfer[] transferTypes) {
		super(viewer);
		this.transferTypes = transferTypes;
	}

	
	// XXX für die Liste:: muss auf ein Element gezogen weden!
	public void dragOver(DropTargetEvent event) {
		
		// XXX
		if (true) {
			return;
		}
		
		int currentLocation = determineLocation(event);
		
		switch (currentLocation) {
			case LOCATION_ON :
				event.feedback = DND.FEEDBACK_SELECT;
				event.detail = DND.DROP_DEFAULT;
				break;
			default :
				event.feedback = DND.FEEDBACK_NONE;
				event.detail = DND.DROP_NONE;
				break;
		}

		event.feedback |= DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
	}
	
	
	@Override
	public void drop(DropTargetEvent event) {
		
		List given = (List) event.data;
		
		List l =(List) getViewer().getInput();
		ArrayList<Integer> intersection = new ArrayList<Integer>(given);
	    intersection.removeAll(l);
		l.addAll(intersection);
		
		if (consumer != null) {
			consumer.accept(null);
		}
		
		getViewer().refresh();
	}
	

	
	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		
		for (Transfer tmp: transferTypes) {
			if (tmp.isSupportedType(transferType)) {
				return true;
			}
		}
		
		return false;
	}

	public boolean performDrop(Object data) {
		return false;
	}
	
	


}