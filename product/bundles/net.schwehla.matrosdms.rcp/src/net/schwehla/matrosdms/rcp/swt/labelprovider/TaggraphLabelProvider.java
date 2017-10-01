package net.schwehla.matrosdms.rcp.swt.labelprovider;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;

public class TaggraphLabelProvider extends LabelProvider implements IStyledLabelProvider {

	// Inject ressourcemanager

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
//				return getResourceManager().createImage(directoryImage);
			}
		}

		return super.getImage(element);
	}



}
