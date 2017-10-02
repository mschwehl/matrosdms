package net.schwehla.matrosdms.rcp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;

import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;

@Creatable
@Singleton
public class SerachResultListController   {

    List <SearchedInfoItemElement> _allElements = new ArrayList<>();
    
    
    @Inject
    private IMatrosServiceService service;
    
	@Inject 
	Logger logger;
	
	@Inject
	private IEventBroker eventBroker;
	



	public void reload(SearchItemInput input)  {
    	
    	try {
    		
        	_allElements.clear();
        	_allElements.addAll( service.searchInfoContextItems(input) );
        	

        	
    	} catch (MatrosServiceException e1) {
			  logger.error(e1);
    		_allElements.clear();

    	} finally {
	    	eventBroker.post(MyEventConstants.TOPIC_REFRESH_SEARCHRESULT , input);
	    }

    	
    }
    
	
    public List getList() {
    	return _allElements;
    }

    	
  
}
