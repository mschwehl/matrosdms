package net.schwehla.matrosdms.rcp.parts.filter;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.rcp.controller.InfoContextListController;

@Creatable
public class TagSmartTreeFilter extends ViewerFilter  {

	
    @Inject
    InfoContextListController listController;
    
    
	boolean localContextLookup;
	
	public boolean isLocalContextLookup() {
		return localContextLookup;
	}


	public void setLocalContextLookup(boolean localContextLookup) {
		this.localContextLookup = localContextLookup;
	}


	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		InfoKategory p = (InfoKategory) element;

		if (localContextLookup == true && listController != null) {
			
			return listController.getInfoKategorySet().contains(p);
			
		}
		return true;
		
	}



}