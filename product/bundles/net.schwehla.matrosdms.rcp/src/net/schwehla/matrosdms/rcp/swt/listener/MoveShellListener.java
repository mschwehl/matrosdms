package net.schwehla.matrosdms.rcp.swt.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

// http://stackoverflow.com/questions/18949073/how-to-create-a-moveable-swt-shell-without-title-bar-close-button
	
/**
* Class to allow user to move a shell without a title.
* 
* @author Laurent Muller
* @version 1.0
*/
public class MoveShellListener implements Listener {

   /*
    * the parent shell
    */
   private final Shell parent;

   /*
    * the mouse down location
    */
   private Point ptMouseDown;

   /**
    * Creates a new instance of this class.
    * 
    * @param parent
    *            the shell to handle.
    */
   public MoveShellListener(final Shell parent) {
       if (parent == null) {
           SWT.error(SWT.ERROR_NULL_ARGUMENT);
       }
       if (parent.isDisposed()) {
           SWT.error(SWT.ERROR_WIDGET_DISPOSED);
       }

       // copy and add listener
       this.parent = parent;
       addControl(parent);
   }

   /**
    * Adds the given control to the list of listened controls. If the given
    * control is an instance of {@link Composite}, the children controls are
    * also added.
    * 
    * @param control
    *            the control to add.
    */
   public void addControl(final Control control) {
       // check control
       if (isDisposed(control) || control.getShell() != parent) {
           return;
       }

       // add listeners
       control.addListener(SWT.MouseDown, this);
       control.addListener(SWT.MouseUp, this);
       control.addListener(SWT.MouseMove, this);

       // children
       if (control instanceof Composite) {
           final Control[] children = ((Composite) control).getChildren();
           for (final Control child : children) {
               addControl(child);
           }
       }
   }

   /**
    * Adds the given controls to the list of listened controls. If one of the
    * given controls is an instance of {@link Composite}, the children controls
    * are also added.
    * 
    * @param controls
    *            the controls to add.
    */
   public void addControls(final Control... controls) {
       if (controls != null) {
           for (final Control control : controls) {
               addControl(control);
           }
       }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void handleEvent(final Event e) {
       switch (e.type) {
       case SWT.MouseDown:
           onMouseDown(e);
           break;
       case SWT.MouseUp:
           onMouseUp(e);
           break;
       case SWT.MouseMove:
           onMouseMove(e);
           break;
       }
   }

   /**
    * Removes the given control to the list of listened controls. If the given
    * control is an instance of {@link Composite}, the children controls are
    * also removed.
    * 
    * @param control
    *            the control to remove.
    */
   public void removeControl(final Control control) {
       // check control
       if (control == parent || isDisposed(control)
               || control.getShell() != parent) {
           return;
       }

       // remove listeners
       control.removeListener(SWT.MouseDown, this);
       control.removeListener(SWT.MouseUp, this);
       control.removeListener(SWT.MouseMove, this);

       // children
       if (control instanceof Composite) {
           final Control[] children = ((Composite) control).getChildren();
           for (final Control child : children) {
               removeControl(child);
           }
       }
   }

   /**
    * Removes the given controls to the list of listened controls. If one of
    * the given controls is an instance of {@link Composite}, the children
    * controls are also removed.
    * 
    * @param controls
    *            the controls to remove.
    */
   public void removeControls(final Control... controls) {
       if (controls != null) {
           for (final Control control : controls) {
               removeControl(control);
           }
       }
   }

   /**
    * Checks if the given control is null or disposed.
    * 
    * @param control
    *            the control to verify.
    * @return true if the control is null or
    *         disposed.
    */
   private boolean isDisposed(final Control control) {
       return control == null || control.isDisposed();
   }

   /**
    * Handles the mouse down event.
    * 
    * @param e
    *            the event data.
    */
   private void onMouseDown(final Event e) {
       if (e.button == 1) {
           ptMouseDown = new Point(e.x, e.y);
       }
   }

   /**
    * Handles the mouse move event.
    * 
    * @param e
    *            the event data.
    */
   private void onMouseMove(final Event e) {
       if (ptMouseDown != null) {
           final Point location = parent.getLocation();
           location.x += e.x - ptMouseDown.x;
           location.y += e.y - ptMouseDown.y;
           parent.setLocation(location);
       }
   }

   /**
    * Handles the mouse up event.
    * 
    * @param e
    *            the event data.
    */
   private void onMouseUp(final Event e) {
       ptMouseDown = null;
   }
}