package net.schwehla.matrosdms.resourcepool;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class MatrosResourceProvider  implements IMatrosResource {
	
    private static ImageRegistry imageRegistry = new ImageRegistry();
	    
    private static FontRegistry  fontRegistry = new FontRegistry();
	@Override
	public Image getImage(Images imageenum) {
		
        Image image = imageRegistry.get(imageenum.getFile());
        if (image == null)
        {
            descriptor(imageenum); // lazy loading
            image = imageRegistry.get(imageenum.getFile());
        }
        return image;
		
	}

	@Override
	public Font getFont(Fonts font) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColor(Color color) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
   

    public ImageDescriptor descriptor(Images imageenum)
    {
        ImageDescriptor descriptor = imageRegistry.getDescriptor(imageenum.getFile());
        if (descriptor == null)
        {
            Bundle bundle = FrameworkUtil.getBundle(Images.class);
            IPath path = new Path("icons/" + imageenum.getFile()); //$NON-NLS-1$
            URL url = FileLocator.find(bundle, path, null);
            descriptor = ImageDescriptor.createFromURL(url);
            imageRegistry.put(imageenum.getFile(), descriptor);
        }
        return descriptor;
    }

       
}
