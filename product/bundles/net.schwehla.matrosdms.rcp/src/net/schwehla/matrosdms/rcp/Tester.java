package net.schwehla.matrosdms.rcp;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Tester {

  public static void main(String[] a) {
    
    final Display d = new Display();
    final Shell shell = new Shell(d);

    shell.setSize(250, 200);
    
    
    shell.setLayout(new FillLayout());
    final Tree tree = new Tree(shell, SWT.SINGLE);

    for (int i = 0; i < 3; i++) {
      TreeItem iItem = new TreeItem(tree, SWT.NONE);
      iItem.setText("Edit me by pressing F2 " + (i + 1));
      iItem.setExpanded(true);
    }
    
    


    final TreeEditor editor = new TreeEditor(tree);
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;
    editor.minimumWidth = 50;


    tree.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                    // Clean up any previous editor control
                    Control oldEditor = editor.getEditor();
                    if (oldEditor != null) oldEditor.dispose();
    
                    // Identify the selected row
                    TreeItem item = (TreeItem)e.item;
                    if (item == null) return;
    
                    // The control that will be the editor must be a child of the Tree
                    Text newEditor = new Text(tree, SWT.NONE);
                    newEditor.setText(item.getText());
                    newEditor.addModifyListener(new ModifyListener() {
                            public void modifyText(ModifyEvent e) {
                                    Text text = (Text)editor.getEditor();
                                    editor.getItem().setText(text.getText());
                            }
                    });
                    newEditor.selectAll();
                    newEditor.setFocus();
                    editor.setEditor(newEditor, item);
            }
    });
    
    tree.addKeyListener(new KeyAdapter() {
    	

    	
      public void keyPressed(KeyEvent event) {
    	  
    	   final Text text = new Text(tree, SWT.NONE);
    
    	   
        if (event.keyCode == SWT.F2 && tree.getSelectionCount() == 1) {
          final TreeItem item = tree.getSelection()[0];

       
          text.setText(item.getText());
          text.setFocus();

          
          
          text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent event) {
            	
              item.setText(text.getText());
              text.dispose();
            }
          });

          editor.setEditor(text, item);
        }
        

    
      }
    });
    
    shell.open();
    while (!shell.isDisposed()) {
      if (!d.readAndDispatch())
        d.sleep();
    }
    d.dispose();

  }
}