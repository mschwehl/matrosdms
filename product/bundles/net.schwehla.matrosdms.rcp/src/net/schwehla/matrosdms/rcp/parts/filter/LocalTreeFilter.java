package net.schwehla.matrosdms.rcp.parts.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;

	/**
	 * Not possible to setSelection() due to stackoverflow -- Need to do a
	 * postfilter
	 * 
	 * @author Martin
	 *
	 */
	public class LocalTreeFilter extends ViewerFilter {

		private String searchString = ""; //$NON-NLS-1$

		public String getSearchString() {
			return searchString;
		}

		public void setSearchString(String searchString) {
			this.searchString = searchString;
			this.firstFocus = false;
		}

		boolean firstFocus;

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {

			if (searchString == null || searchString.length() == 0) {
				return true;
			}

			InfoKategory p = (InfoKategory) element;

			if (p.getChildren().size() > 0) {

				boolean effective = false;

				for (InfoKategory kid : p.getChildren()) {
					effective |= select(viewer, p, kid);
				}

				if (effective) {

					// XXX not so easy because not threadsave :-(
					// Expanding while filtering

					/*
					 * synchronized (treeViewer.getTree() ) { ((TreeViewer)
					 * _treeviewer). expandToLevel( p
					 * ,AbstractTreeViewer.ALL_LEVELS);
					 * 
					 * }
					 */

					return true;

				}

			}

			if (p.getName().toLowerCase().contains(this.searchString.toLowerCase())) {
				return true;
			}

			return false;

		}
	}


