package net.schwehla.matrosdms.rcp.celleditor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.i18n.MatrosMessage;
import net.schwehla.matrosdms.rcp.dialog.AttributeDateTimespanDialog;

public class AttributeValidToDialogEditor extends DialogCellEditor {
	

	// https://github.com/vogella/eclipse.platform.ui/blob/master/examples/org.eclipse.jface.snippets/Eclipse%20JFace%20Snippets/org/eclipse/jface/snippets/viewers/TextAndDialogCellEditor.java
	
	
	MatrosMessage messages;

		private Composite parent;
		
		public AttributeValidToDialogEditor(Composite parent, int style) {
			super(parent, style);
			
			this.parent = parent;
		}

		private CLabel label;

		/**
		 * @wbp.parser.entryPoint
		 */
		@Override
		protected Control createContents(Composite cell) {
			label = new CLabel(cell, SWT.CENTER);
			label.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					if (getErrorMessage() != null) {
						MessageDialog.openError(parent.getShell(),
								"Invalid date", getErrorMessage());
					}
					super.focusLost(e);
				}
			});
			return label;
		}
		
		@Override
		protected void updateContents(Object value) {
			
			if (value instanceof AbstractInfoAttribute) {
				AbstractInfoAttribute casted = (AbstractInfoAttribute) value;
				label.setText(value != null ? casted.getTimeInfomation() : "");
			}

		}
		
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			AttributeDateTimespanDialog dialog = new AttributeDateTimespanDialog(parent.getShell());
			dialog.setMessages(messages);
			
			dialog.setInfoAttribute( (AbstractInfoAttribute) getValue());
			int retVal = dialog.open();
			
			return retVal == Window.OK ? dialog.getInfoAttribute() : null;
		}

		public void setMessages(MatrosMessage messages) {
			this.messages = messages;
			
		}
		
		

	}