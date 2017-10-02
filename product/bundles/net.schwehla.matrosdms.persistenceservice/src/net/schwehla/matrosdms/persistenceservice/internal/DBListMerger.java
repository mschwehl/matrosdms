package net.schwehla.matrosdms.persistenceservice.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import net.schwehla.matrosdms.domain.api.IIdentifiable;
import net.schwehla.matrosdms.persistenceservice.entity.internal.AbstractDBInfoBaseEntity;

public class DBListMerger <X extends AbstractDBInfoBaseEntity>  {
	
	
	Set <IIdentifiable> 		   doubleSet = new HashSet<>();
    Set <AbstractDBInfoBaseEntity> deleteSet = new HashSet<>();

    

	public void merge (EntityManager em, List <IIdentifiable> beListClone , List <AbstractDBInfoBaseEntity> dbListClone ,
			Function<IIdentifiable,  X> newElementfunction,
			Consumer <IIdentifiable > updateFunction , Consumer <AbstractDBInfoBaseEntity > deleteFunction ) {
		
		List <IIdentifiable> beList =new ArrayList(beListClone);
		
		// not on copy
		List <AbstractDBInfoBaseEntity> dbList = dbListClone;

        // Merge :: XXX recode to streams

        for (AbstractDBInfoBaseEntity d: dbList) {

        			IIdentifiable exists = null;
        			AbstractDBInfoBaseEntity  deleteme = d;

                   for (IIdentifiable k : beList) {
                               if (d.getPK().longValue() == k.getIdentifier().getPk()) {
	                               deleteme = null;
	                               exists = (IIdentifiable) k;
	                               continue;
                               }
                   }

                   if (exists != null) {
                           doubleSet.add(exists); 
                   }

                   if (deleteme != null) {
                           deleteSet.add(deleteme);         
                   }
        }


		// update Existing
	    for (IIdentifiable art : doubleSet) {
	    	updateFunction.accept(art);
	    }
	    

        Set <IIdentifiable> jobSet = beList.stream().collect(Collectors.toSet());
        jobSet.removeAll(doubleSet);
		
	    for (AbstractDBInfoBaseEntity art : deleteSet) {
	    	deleteFunction.accept(art);
	    }
	    
	    
        
		// map missing elements
	    for (IIdentifiable art : jobSet) {
	    	X o = newElementfunction.apply(art);
		    dbList.add(o);
	    }
	    
	
	    
	}
	
		

	
}

