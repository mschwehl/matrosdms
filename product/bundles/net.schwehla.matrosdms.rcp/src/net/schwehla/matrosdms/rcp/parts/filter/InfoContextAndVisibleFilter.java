package net.schwehla.matrosdms.rcp.parts.filter;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.Viewer;

import net.schwehla.matrosdms.domain.core.InfoContext;

@Creatable
public class InfoContextAndVisibleFilter extends ItemFilter {
	



    @Override
    public boolean select(Viewer viewer, 
        Object parentElement, 
        Object element) {
      
    	try {
    		
    		InfoContext c = (InfoContext) element;
    		
    		if (!c.isVisible()) {
    			return false;
    		}
    		
    	
    	      if (searchString == null || searchString.length() == 0) {
    	          return true;
    	        }
    	        
    	        
    	        
    	        if (c.getName().toLowerCase().contains(this.searchString.toLowerCase())) {
    	           return true;
    	        }
    	        
    	        return false;

    	} catch(RuntimeException e) {
    		logger.warn("Nullpointer :" + element , e); //$NON-NLS-1$
    	}
    	
    	return false;

      
    }
  } 
