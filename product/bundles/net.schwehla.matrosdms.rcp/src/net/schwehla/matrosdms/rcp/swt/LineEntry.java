package net.schwehla.matrosdms.rcp.swt;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Event;

public class LineEntry {

    String line;

    int columnWidth;

    /**
    * Create a new instance of the receiver with name text constrained to a
    * column of width.
    */

    LineEntry(String text, int width) {
           line = text;
           columnWidth = width;
    }

    public int getHeight(Event event) {
           event.gc.setLineWidth(columnWidth);
           return event.gc.textExtent(line).y;
    }

    public int getWidth(Event event) {
           return columnWidth;
    }  

    protected Font getFont() {
           return JFaceResources.getFont(JFaceResources.HEADER_FONT);
    }

    public void draw(Event event) {
           event.gc.drawText(line, event.x, event.y);

    }
}
