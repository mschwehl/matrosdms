package net.schwehla.matrosdms.rcp.swt.labelprovider;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;

public class FileModifiedLabelProvider extends LabelProvider implements IStyledLabelProvider {

	private DateFormat dateLabelFormat;

	public FileModifiedLabelProvider(DateFormat dateFormat) {
		dateLabelFormat = dateFormat;
	}

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof File) {
			File file = (File) element;
			long lastModified = file.lastModified();
			return new StyledString(dateLabelFormat.format(new Date(lastModified)));
		}
		return null;
	}
}