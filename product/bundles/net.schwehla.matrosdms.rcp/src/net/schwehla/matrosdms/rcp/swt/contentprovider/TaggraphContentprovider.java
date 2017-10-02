package net.schwehla.matrosdms.rcp.swt.contentprovider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;

public class TaggraphContentprovider implements ITreeContentProvider {

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof InfoKategory) {
			InfoKategory[] r = new InfoKategory[1];
			return ((InfoKategory) inputElement).getChildren().toArray();

		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		InfoKategory file = (InfoKategory) parentElement;
		return file.getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		InfoKategory file = (InfoKategory) element;

		if (file.getParents() != null && file.getParents().size() > 0) {
			return file.getParents().get(0);
		}

		return null;

	}

	@Override
	public boolean hasChildren(Object element) {
		InfoKategory file = (InfoKategory) element;
		if (!file.getChildren().isEmpty()) {
			return true;
		}
		return false;
	}

}