package net.schwehla.matrosdms.rcp.parts.filter;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.Viewer;

import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;

@Creatable
public class SearchItemFilter extends ItemFilter {
	
    

    @Override
    public boolean select(Viewer viewer, 
        Object parentElement, 
        Object element) {
      
    	try {
    	
    	      if (searchString == null || searchString.length() == 0) {
    	          return true;
    	        }
    	        
    	      	SearchedInfoItemElement p = (SearchedInfoItemElement) element;
    	        
    	        
    	        if ((p.getSearchFilterString())
    	        		.contains(this.searchString.toLowerCase())) {
    	           return true;
    	        }
    	        
    	        return false;

    	} catch(RuntimeException e) {
    		logger.warn("Nullpointer :" + element , e); //$NON-NLS-1$
    	}
    	
    	return false;

      
    }
    
} 
