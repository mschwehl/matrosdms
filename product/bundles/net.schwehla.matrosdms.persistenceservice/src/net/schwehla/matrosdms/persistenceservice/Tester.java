package net.schwehla.matrosdms.persistenceservice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;

public class Tester {
public static void main(String[] args) {
	
	
	
    Map map = new HashMap(); 
    map.put( PersistenceUnitProperties.CLASSLOADER, Tester.class.getClassLoader() ); 
    
	    map.put("javax.persistence.jdbc.url", "jdbc:h2:" + "c:\\temp" + File.separator 
	    		+ "matrosdms;MODE=Oracle;TRACE_LEVEL_SYSTEM_OUT=2;"   );

	    
	    map.put("eclipselink.create-ddl-jdbc-file-name","matroscreateDDL_ddlGeneration.jdbc" );

			
		    map.put("eclipselink.ddl-generation.output-mode","sql-script" );
		    
			
		    map.put("eclipselink.ddl-generation","drop-and-create-tables" );
  
		
			
    
    try {
		PersistenceProvider persistenceProvider = new  PersistenceProvider(); 
		 persistenceProvider.createEntityManagerFactory( "H2JPA", map ).createEntityManager();
	} catch (Exception e) {
		e.printStackTrace();
	} 
    
    
    
}
}
