package net.schwehla.matrosdms.rcp.celleditor;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import net.schwehla.matrosdms.domain.core.attribute.InfoTextAttribute;

public class MultilineTextCellEditor extends DialogCellEditor {

	   private ICellEditorValidator validator;
	   
	   /**
	    * Constructor.
	    */
	   public MultilineTextCellEditor(Composite parent, ICellEditorValidator validator) {
	      super(parent);
	      this.validator = validator;
	   }

	   /**
	    * This is selection handler of dialog button in cell editor.
	    * We only create and open multiline editor with appropriate validator and preseted value. 
	    */
	   @Override
	protected Object openDialogBox(Control cellEditorWindow) {
	    
		   
	      MultiLineTextEditorDialog dialog = new MultiLineTextEditorDialog(getControl().getShell(), (InfoTextAttribute) getValue(), validator);
	      dialog.create();
	      return dialog.open() == Window.OK ? dialog.getValue() : dialog.getValue();
	   }
	   
}