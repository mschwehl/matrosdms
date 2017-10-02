package net.schwehla.matrosdms.rcp.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;

public class KategoryLabelProvider extends LabelProvider implements IStyledLabelProvider {

	

	
	
	private ImageDescriptor directoryImage;
	private ResourceManager resourceManager;

	public  KategoryLabelProvider(ImageDescriptor directoryImage) {
		this.directoryImage = directoryImage;
	}

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof InfoKategory) {
			InfoKategory file = (InfoKategory) element;
			StyledString styledString = new StyledString(file.getName());

			return styledString;
		}
		return null;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof InfoKategory) {
			if (((InfoKategory) element).isObject()) {
				return getResourceManager().createImage(directoryImage);
			}
		}

		return super.getImage(element);
	}

	@Override
	public void dispose() {
		// garbage collect system resources
		if (resourceManager != null) {
			resourceManager.dispose();
			resourceManager = null;
		}
	}

	protected ResourceManager getResourceManager() {
		if (resourceManager == null) {
			resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return resourceManager;
	}

}