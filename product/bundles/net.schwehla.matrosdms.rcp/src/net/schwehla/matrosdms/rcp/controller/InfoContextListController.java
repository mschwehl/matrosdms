package net.schwehla.matrosdms.rcp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;

import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategoryList;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;

@Creatable
@Singleton
public class InfoContextListController   {

    List <InfoContext> _allInfoContextElements = new ArrayList<>();
    
    Set <InfoKategory> infoKategorySet = new HashSet<>();
    
    @Inject
    private IMatrosServiceService service;
    
	@Inject 
	Logger logger;
	
	@Inject
	private IEventBroker eventBroker;
	
    InfoContext _infoContext;
	
	@Inject
	@Optional
	private void subscribeTopic_TOPIC_CONTEXT_CREATED(@UIEventTopic(MyEventConstants.TOPIC_CONTEXT_CREATED) InfoContext type) {
		 
		if (type != null ) {
			
			try {
				_allInfoContextElements.add(service.loadInfoContextByIdentifier(type.getIdentifier() )) ;
				
				eventBroker.post(MyEventConstants.TOPIC_REFRESH_CONTEXTLIST , type);
				
				
			} catch (MatrosServiceException e) {
				logger.error(e);
			}

		}

		
	}
	

	
    @Inject
	@Optional
	private void subscribeTOPIC__REFRESH_CONTEXT_ARCHIV(@UIEventTopic(MyEventConstants.TOPIC__REFRESH_CONTEXT_STAR) InfoContext element) {


    	reload();
		
		eventBroker.post(MyEventConstants.TOPIC_REFRESH_CONTEXTLIST , element);
		
		
	}
    

    
	
	@Inject
	@Optional
	private void subscribeTOPIC_DROPFIELD_KATEGORY_CHANGED_SEND_TO_MAPLIST(@UIEventTopic(MyEventConstants.TOPIC_DROPFIELD_KATEGORY_CHANGED_SEND_TO_MAPLIST) InfoContext type) {
		
		_infoContext = type;
		
		infoKategorySet.clear();
		
		// speedup with parallel stream
		
		
		// TODO: better tow threads with half array working ? 
		
		// 2 sek for a half million array is not that bad
		
		// cool new syntax
			
			_allInfoContextElements.parallelStream().forEach( con -> {
				

				
				con.setVisible(select(con));
				
				if (con.isVisible()) {
					
					// All reachable elements
					
					// Optimization: why clear three of them ? 
					
					infoKategorySet.addAll(con.getDictionary().get(MyGlobalConstants.ROOT_WER).getSelfAndTransitiveParents());
					infoKategorySet.addAll(con.getDictionary().get(MyGlobalConstants.ROOT_WO).getSelfAndTransitiveParents() );
					infoKategorySet.addAll(con.getDictionary().get(MyGlobalConstants.ROOT_WAS).getSelfAndTransitiveParents() );
				}
				
			
				
				
			} );
			
			
/*
			
			
			for(InfoContext con : _allInfoContextElements) {
				
				con.setVisible(select(con));
				
				if (con.isVisible()) {
					
					// All reachable elements
					
					infoKategorySet.addAll(con.getDictionary().get(MyGlobalConstants.ROOT_WER).getSelfAndTransitiveParents());
					infoKategorySet.addAll(con.getDictionary().get(MyGlobalConstants.ROOT_WO).getSelfAndTransitiveParents() );
					infoKategorySet.addAll(con.getDictionary().get(MyGlobalConstants.ROOT_WAS).getSelfAndTransitiveParents() );
				}
				
			}
		
			
		}
		
		*/

	
		
		eventBroker.post(MyEventConstants.TOPIC_REFRESH_CONTEXTLIST , type);
	    

	}
	



	public void reload()  {
    	
    	try {
    		
        	_allInfoContextElements.clear();
        	_allInfoContextElements.addAll( service.loadInfoContextList(false) );
        	
        	infoKategorySet.clear();
        	
        	InfoKategory rootWo = service.getInfoKategoryByIdentifier(MyGlobalConstants.ROOT_WER);
        	infoKategorySet.addAll( rootWo.getSelfAndAllTransitiveChildren());

        	InfoKategory rootWer = service.getInfoKategoryByIdentifier(MyGlobalConstants.ROOT_WO);
        	infoKategorySet.addAll( rootWer.getSelfAndAllTransitiveChildren());
        	
        	InfoKategory rootWas = service.getInfoKategoryByIdentifier(MyGlobalConstants.ROOT_WAS);
        	infoKategorySet.addAll( rootWas.getSelfAndAllTransitiveChildren());
        	
    	} catch (MatrosServiceException e1) {
			  logger.error(e1);
	    } 

    	
    }
    
    public List getList() {
    	return _allInfoContextElements;
    }

    
    public Set<InfoKategory> getInfoKategorySet() {
		return infoKategorySet;
	}

    





        public boolean select(InfoContext element) {
               
 
          
          if (_infoContext != null)
          {

  			
  			
              // Meist sollte WAS am meisten Filtern
  	           
              if (!checkItemFilter(element, MyGlobalConstants.ROOT_WER))
                {
                    return false;
                }
   
              if (!checkItemFilter(element,MyGlobalConstants.ROOT_WO))
              {
                  return false;
              }

              if (!checkItemFilter(element,MyGlobalConstants.ROOT_WAS))
              {
                  return false;
              }
              

          
                return true;

         }

          // Default: Alle Elemente zeigen
          return true;

      }

  
        
    	public boolean checkItemFilter(InfoContext currentListElement, Identifier art) {

    		
    	       if (_infoContext.getTagList(art).size() > 0)
               {
    	    	   
    	    	   InfoKategoryList currentList = currentListElement.getTagList(art);
    	    	   
    	    	   if (currentList.size() == 0) {
    	    		   return false;
    	    	   }

    	    	   return  _infoContext.getTagList(art).checkRekursivKategoriePasstRekursiv(currentList);
    	    	   
               }

               return true;
               
    	} 
        
        

    	
  
}
