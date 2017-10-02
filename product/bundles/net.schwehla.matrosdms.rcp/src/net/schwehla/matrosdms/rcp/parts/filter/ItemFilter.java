package net.schwehla.matrosdms.rcp.parts.filter;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;

@Creatable
public class ItemFilter extends ViewerFilter {
	
	@Inject Logger logger;

    protected String searchString = ""; //$NON-NLS-1$

    public String getSearchString() {
         return searchString;
   }

   public void setSearchString(String searchString) {
         this.searchString = searchString;
   }



    @Override
    public boolean select(Viewer viewer, 
        Object parentElement, 
        Object element) {
      
    	try {
    	
    	      if (searchString == null || searchString.length() == 0) {
    	          return true;
    	        }
    	        
    	        InfoBaseElement p = (InfoBaseElement) element;
    	        
    	        
    	        if (p.getName().toLowerCase().contains(this.searchString.toLowerCase())) {
    	           return true;
    	        }
    	        
    	        return false;

    	} catch(RuntimeException e) {
    		logger.warn("Nullpointer :" + element , e); //$NON-NLS-1$
    	}
    	
    	return false;

      
    }
  } 
