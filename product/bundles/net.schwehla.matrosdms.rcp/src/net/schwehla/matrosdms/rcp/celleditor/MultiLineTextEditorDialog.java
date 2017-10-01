package net.schwehla.matrosdms.rcp.celleditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.schwehla.matrosdms.domain.core.attribute.InfoTextAttribute;

// http://forum.cloveretl.com/viewtopic.php?t=3740

public class MultiLineTextEditorDialog extends Dialog {
	   
	   /**
	    * The input value; the empty string by default.
	    */
	   private InfoTextAttribute value = null;

	   private ICellEditorValidator validator;
	   
	   /**
	    * Input text widget.
	    */
	   private Text text;

	   /**
	    * Error message text widget.
	    */
	   private Text errorMessageText;

	   /**
	    * Constructor.
	    */
	   public MultiLineTextEditorDialog(Shell parentShell, InfoTextAttribute initialValue, ICellEditorValidator validator) {
	      super(parentShell);
	      this.validator = validator;
	      setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX );
	      value = initialValue ;
	   }
	   
	   /**
	    * Before closing dialog we only store the result value for later usage.
	    * @see #getValue()
	    */
	   @Override
	protected void buttonPressed(int buttonId) {
//	      value = buttonId == IDialogConstants.OK_ID ? text.getText() : null;
	      super.buttonPressed(buttonId);
	   }
	   
	   /**
	    * Here you can preset shell attributes like window title.
	    */
	   @Override
	protected void configureShell(Shell shell) {
	      super.configureShell(shell);
	      shell.setText("My multi-line editor");
	   }

	   /**
	    * After all window widgets are ready to use, you can preset their values. 
	    */
	   @Override
	protected Control createContents(Composite parent) {
	      Control control = super.createContents(parent);

	      text.setFocus();
	      if (value != null) {
	         text.setText("init");
	         text.selectAll();
	      }

	      return control;
	   }
	   
	   /**
	    * In this method you have to create all widgets in root composite of dialog.
	    */
	   @Override
	protected Control createDialogArea(Composite parent) {
	      // create root composite
	      Composite composite = (Composite) super.createDialogArea(parent);
	      // create main multi-line text editor
	      text = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	      text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_BOTH));
	      text.addModifyListener(new ModifyListener() {
	         @Override
			public void modifyText(ModifyEvent e) {
	            validateInput();
	         }
	      });
	      // create error message board
	      errorMessageText = new Text(composite, SWT.READ_ONLY);
	      errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
	      errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

	      return composite;
	   }

	   private void validateInput() {
	      setErrorMessage(validator.isValid(text.getText()));
	   }

	   public void setErrorMessage(String errorMessage) {
	      errorMessageText.setText(errorMessage == null ? "" : errorMessage); //$NON-NLS-1$
	      getButton(IDialogConstants.OK_ID).setEnabled(errorMessage == null);
	      errorMessageText.getParent().update();
	   }

	   /**
	    * Returns the string value typed into this dialog.
	    */
	   public String getValue() {
		   
//	      return value;
	      
	      return "oha";
	      
	   }
	   
 }	   
