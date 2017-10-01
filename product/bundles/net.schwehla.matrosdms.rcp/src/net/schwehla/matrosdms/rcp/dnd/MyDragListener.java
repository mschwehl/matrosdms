package net.schwehla.matrosdms.rcp.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;




public class MyDragListener <T> extends DragSourceAdapter {

	
	private final ISelectionProvider selectionProvider;
	Class clazz;
	
	public MyDragListener(ISelectionProvider selectionProvider, Class clazz) {
		this.selectionProvider = selectionProvider;
		this.clazz = clazz;
	}
	
	@Override
	public void dragStart(DragSourceEvent event) {

		event.doit = false;
		if ( !selectionProvider.getSelection().isEmpty() ) { 
			Object obj = ((StructuredSelection)selectionProvider.getSelection()).getFirstElement(); 
			if ( obj != null && clazz.isInstance(obj) ) { 
				event.doit = true; 
			} 
		} 

		super.dragStart(event);
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) selectionProvider
				.getSelection();
		
		Iterator it = selection.iterator();
	
		List l = new ArrayList();
		it.forEachRemaining(l::add);
		

		event.data = l;

	
	}

}