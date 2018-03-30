
package net.schwehla.matrosdms.rcp.swt.search;

import org.eclipse.swt.*;
 import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
 import org.eclipse.swt.layout.*;
 
	  public class Shell1 {

	  public static void main(String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);
    
    shell.setSize(289, 185);
    
    
    
	      Listener tipListener = new Listener() {
        Shell tip = null;
        Label label = null;
	          void createToolTip() {
            tip = new Shell(shell, SWT.ON_TOP);
            tip.setLayout(new FillLayout());
            label = new Label(tip, SWT.NONE);
	              Listener listener = new Listener() {
	                  public void handleEvent(Event event) {
                    tip.dispose();
                }
            };
           label.addListener(SWT.MouseExit, listener);
            Color foreground =
                display.getSystemColor(
                    SWT.COLOR_INFO_FOREGROUND);
            label.setForeground(foreground);
            Color background =
                display.getSystemColor(
                    SWT.COLOR_INFO_BACKGROUND);
            label.setBackground(background);
            label.setText("ToolTip");
            tip.pack();
       }
          public void handleEvent(Event e) {
	              switch (e.type) {
                case SWT.KeyDown:
                case SWT.MouseMove:
                    if (tip != null) tip.dispose();
                   tip = null;
                  break;
                  case SWT.MouseHover: {
                    if (tip != null) break;
                   createToolTip();
                   Rectangle rect = tip.getBounds();
                   rect.x = e.x;
                 rect.y = e.y + 22;
                 tip.setBounds(
                       display.map(shell, null, rect));
                   tip.setVisible(true);
               }
            }
        }
    };
    
    
    
    shell.addListener(SWT.KeyDown, tipListener);
   shell.addListener(SWT.MouseHover, tipListener);
    shell.addListener(SWT.MouseMove, tipListener);
    shell.pack();
    shell.setLayout(null);
    shell.open();
      while (!shell.isDisposed()) {
        if (!display.readAndDispatch()) display.sleep();
    }
    display.dispose();
}
}