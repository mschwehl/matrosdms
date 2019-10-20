package net.schwehla.matrosdms.resourcepool;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public interface IMatrosResource {
	

	public static enum Images
	{
	  
	    NOTE("note.png"); //$NON-NLS-1$

	    private final String file;

	    private Images(String file)
	    {
	        this.file = file;
	    }
	    
	    public String getFile() {
	    	return file;
	    }
	    
	}
	
	public enum Fonts {
		
		FONT_BIG;
	}
	


	
	public Image getImage(Images image);
	public Font getFont(Fonts font);
    public Color getColor(String name);

}
