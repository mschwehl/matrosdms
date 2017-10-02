package net.schwehla.matrosdms.persistenceservice.internal;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;

import net.schwehla.matrosdms.domain.admin.AppSettings;
import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.admin.MatrosUser;
import net.schwehla.matrosdms.domain.api.IIdentifiable;
import net.schwehla.matrosdms.persistenceservice.entity.internal.management.DBConfig;
import net.schwehla.matrosdms.rcp.MatrosNoServerconfigServiceException;
import net.schwehla.matrosdms.rcp.MatrosServiceException;


public class MatrosServerProxy 	implements InvocationHandler,  Serializable  {

	    
    Logger logger;

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	MatrosUser user;

	IEventBroker eventBroker;
	public void setEventBroker(IEventBroker eventBroker) {
		this.eventBroker = eventBroker;
		
	}

	Map<String,String> cachedServerConfig = null;

	
	private MatrosServiceImpl serviceImpl;

	private EntityManagerFactory emf;

	
		public MatrosServerProxy() {
		
		}
		
	


		public MatrosUser getUser() {
			return user;
		}



		public void setUser(MatrosUser user) {
			this.user = user;
		}



		public MatrosServiceImpl getServiceImpl() {
			return serviceImpl;
		}



		public void setServiceImpl(MatrosServiceImpl serviceImpl) {
			this.serviceImpl = serviceImpl;
		}



		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
			

			
			
			EntityManager em = getEntityManager(false);
			
			Exception localException = null;
			
			try {
				
				em.getTransaction().begin();
				serviceImpl.setEntityManager(em);
				serviceImpl.setUser(user);
				serviceImpl.setEventBroker(eventBroker);
				serviceImpl.setConfiguration(readConfigTable());
	
				// maybe loadConfig() implementation here
				// serviceImpl.setConfig(load config table here)
				// the serviceImpl is not the rigtht place because of chicken-egg-problem
				
				return method.invoke(serviceImpl, args);
				
			} catch (Exception e) {
				
				if (e instanceof InvocationTargetException ) {
					((InvocationTargetException) e).getTargetException().printStackTrace(System.err);
				}
				
				StringBuffer sb = new StringBuffer("Parameter -> ");
				
				if (args != null) {
					for (Object obj : args) {
						if (obj instanceof IIdentifiable) {
							sb.append( ((IIdentifiable) obj).getName() + "|" +  ((IIdentifiable) obj).getIdentifier().toDebug() );
						}
					}
					
				}
				
				
				serviceImpl.log(e,  sb.toString() );
				localException = e;
				
				
				if (e instanceof MatrosServiceException) {
					throw (MatrosServiceException) e;
				}
				
				throw e;
				
			} finally {
				
				if (em != null && em.isOpen() ) {
					
					if (localException == null) {
						
						if (em.getTransaction().isActive()) {
							em.getTransaction().commit();
						}
						
					} else {
						
						if (em.getTransaction().isActive()) {
							em.getTransaction().rollback();
							
							logger.debug("Transaction rollback");
						}
						
					}
					
					em.close();
				}
	 			
				em = null;
				
			}
			
		}
		


	    /**
	     * only open if database exists
	     */
		public EntityManager getEntityManager(boolean createNew) throws MatrosServiceException {
			
			if (emf == null) {
				
				
		       	MatrosConfigReader cr = new MatrosConfigReader();

				
				String dbPath = null;
				try {
					dbPath = cr.getDBPath();
				} catch (Exception e) {
					 throw new MatrosServiceException("Kann Path nicht ermitteln");
				}

				if (!createNew && !isDBFileexistent()) {
					 throw new IllegalStateException("DB-Path-Property not set");
				}
				
				// check if trace exists, then backup
				Path p = Paths.get(dbPath + File.separator + "matrosdms.trace.db");

				if (Files.exists(p)) {
					
				    Path newOne = Paths.get(p.getParent() + File.separator + "_backup_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_matrosdms.trace.db");
				    try {
						Files.move(p,newOne);
					} catch (IOException e) {
						logger.error("cannot move dbtracefile");
					}
					
				}
				
				
			    Map map = new HashMap(); 
			    map.put( PersistenceUnitProperties.CLASSLOADER, getClass().getClassLoader() ); 
			    
		 	    map.put("javax.persistence.jdbc.url", "jdbc:h2:" + dbPath + File.separator + "matrosdms;MODE=Oracle;TRACE_LEVEL_SYSTEM_OUT=1;" + (createNew == false ? "IFEXISTS=TRUE":"") );

			    
			    PersistenceProvider persistenceProvider = new  PersistenceProvider(); 
			    emf = persistenceProvider.createEntityManagerFactory( "H2JPA", map ); 
			    
			    
			    
				
			}
	
			
			
			if (emf != null) {
				
				try {
						return  emf.createEntityManager();
				} catch(Exception e) {
					
					String error = null;
					try {

				       	MatrosConfigReader cr = new MatrosConfigReader();
						String dbPath;

						
						dbPath = cr.getDBPath();
						Path p = Paths.get(dbPath + File.separator + "matrosdms.trace.db");
						
						 error = new String(Files.readAllBytes( p));
						
					} catch (Exception e1) {
						logger.error(e1, "cannot attach trace :" + (error != null ? error : "no trace available"));
					}
					
					MatrosServiceException mse = new MatrosServiceException(e, "cannot create entity manager: " + (error != null ? error : "no trace available"));
					throw mse;
				}
				
			}
			
			throw new MatrosServiceException("cannot create entitymanager (end of method): ");
			
		}
		
		
	    /**
	     * creates database if not 	exists
	     */
		public void createDatabaseFile(MatrosConnectionCredential credentials) throws MatrosServiceException {
			
			
		    try {

		    	emf = null;
		    	getEntityManager(true);
		    	
		    	
		    
		    } catch(Exception e) {
		    	e.printStackTrace();
		    	logger.error(e);
		    	throw new MatrosServiceException("Cannot create Database-File " );
		    }
			
		}
		
		

		public void updateConfigIniFileProperties(String approot,  MatrosConnectionCredential dbCredentials) throws Exception {


	       	MatrosConfigReader cr = new MatrosConfigReader();
	    	  
	     	 try {
	     		 cr.setConfigIniFileProperties(approot, dbCredentials.getDbUser(), dbCredentials.getDbPasswd());
	     	 } catch(Exception e) {
	     		 logger.error("cannot find home + " + cr.getInstallHomeMessage()); 
	     		 throw new RuntimeException(e);
	        } 
		     
			
		}
		
		
		
		public boolean isDBFileexistent() throws MatrosNoServerconfigServiceException{
			
			
	       	MatrosConfigReader cr = new MatrosConfigReader();
			
			String dbPath = null;
			try {
				
				dbPath = cr.getDBPath();
				File f = new File(dbPath + File.separator + "matrosdms.mv.db");
				return f.exists();
				
			} catch (Exception e) {
				throw new MatrosNoServerconfigServiceException("DB-Path points to no database-file");
			}

			
		}
		
		
		public void createCloudDirectoryAndSubdirs(CloudSettings settings) throws MatrosServiceException {
		
			File f = new File(settings.getPath()) ;
			
			if (!f.exists() && f.isDirectory()) {
				throw new MatrosServiceException("not a directory");
			}
			
			boolean created = f.mkdirs();
			
			if (created = false ) {
				throw new MatrosServiceException(f.toString() + " not created");
			}
			
			createSubdir(f,"documents");
			createSubdir(f,"backup");
					
		}
		
		
		
				
		public void createAppDirectoryAndSubdirs(AppSettings settings) throws MatrosServiceException {
		
			File f = new File(settings.getAppPath()) ;
			
			if (!f.exists() && f.isDirectory()) {
				throw new MatrosServiceException("not a directory");
			}
			
			boolean crated = f.mkdirs();
			
			if (crated == false ) {
				throw new MatrosServiceException(f.toString() + " not created");
			}
			
			createSubdir(f,"logs");
			createSubdir(f,"index");
			createSubdir(f,"db");
			createSubdir(f,"cache");
					
		}
		
		
		
		private void createSubdir(File parent, String name) throws MatrosServiceException {
			
			try {
				
				File subDir = new File(parent.getCanonicalPath() + File.separator + name );
				
			    boolean created = subDir.mkdirs();
				
				if (created == false ) {
					throw new MatrosServiceException(subDir.toString() + " not created");
				}
				
			} catch (IOException e) {
				throw new MatrosServiceException(e.getMessage());
			}
	
			

			
		}

		

		public void createEmptyDatabase() throws MatrosServiceException  {
			
			Function<EntityManager,java.util.Optional<Object>> consumer = (em -> {
				
				String executeResult;
				
				  for (String statement : statements) {
					  
					  executeResult = statement;
					  try {
						  
						  em.createNativeQuery(statement ).executeUpdate();
						  
						  logger.info(executeResult + " ok");
						  
					  } catch(RuntimeException e) {
						  
						  logger.info(executeResult + " error");
						  
						  throw e;
					  }
					  

				  }		 

				  
				  return java.util.Optional.of(Boolean.TRUE);
				
			});
			
			
			execute(consumer);
			
		}


		


		private Map<String,String> readConfigTable() throws MatrosServiceException {
			
			
			if (cachedServerConfig == null) {
				
				Function<EntityManager,java.util.Optional<Object>> consumer = (em -> {

					Map<String,String> result =  new HashMap<>();
					 List <DBConfig> dbResult = em.createNamedQuery("DBConfig.findAll", DBConfig.class).getResultList(); //$NON-NLS-1$

					 for (DBConfig element : dbResult) {
						 result.put(element.getKey(), element.getValue());
					 }

					 return java.util.Optional.of(result);
					

					
				});
				
				
				
				cachedServerConfig = (Map<String,String>) execute(consumer).get();
				
				
			}
			
			return cachedServerConfig;

		

			
		}
		
		
		public java.util.Optional execute(Function <EntityManager,java.util.Optional<Object>> consumer ) throws MatrosServiceException {
			
				
				EntityManager em = getEntityManager(false);
				Object localException = null;
				
				try {
					
					em.getTransaction().begin();
					
					return consumer.apply(em);
					
				} catch (Exception e) {
					
					localException = e;
					logger.error(e, "cannot create tables");
					
					
				} finally {
					
					if (em != null && em.isOpen() ) {
						
						
						if (localException == null) {
							
							if (em.getTransaction().isActive()) {
								em.getTransaction().commit();
							}
							
						} else {
							
							if (em.getTransaction().isActive()) {
								em.getTransaction().rollback();
								
								logger.debug("Transaction rollback");
							}
							
						}
						
						em.close();
					}
						
					em = null;
							
							
				}
				
				return java.util.Optional.empty();
			
		}
			


		


		String[] statements = new String[] {
		
				"CREATE TABLE Context (CONTEXT_ID BIGINT NOT NULL UNIQUE, DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, DESCRIPTION VARCHAR, ICON VARCHAR, NAME VARCHAR NOT NULL, UUID VARCHAR NOT NULL UNIQUE, PRIMARY KEY (CONTEXT_ID))",
				"CREATE TABLE FileMetadata (FILE_ID BIGINT NOT NULL UNIQUE, CRYPTSETTINGS VARCHAR, FILENAME VARCHAR NOT NULL, FILESIZE BIGINT, MIMETYPE VARCHAR, SHA256CRYPTED VARCHAR NOT NULL UNIQUE, SHA256ORIGINAL VARCHAR NOT NULL UNIQUE, PRIMARY KEY (FILE_ID))",
				"CREATE TABLE Item (ITEM_ID BIGINT NOT NULL UNIQUE, DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, DESCRIPTION VARCHAR, ICON VARCHAR, LASTINDEXRUN TIMESTAMP, INDEXSTATE INTEGER, NAME VARCHAR NOT NULL, STAGE INTEGER, STORAGEITEMIDENTIFIER VARCHAR, UUID VARCHAR NOT NULL UNIQUE, CONTEXT_ID BIGINT NOT NULL, FILE_ID BIGINT UNIQUE, STORE_STORE_ID BIGINT, USER_ID BIGINT NOT NULL, PRIMARY KEY (ITEM_ID))",
				"CREATE TABLE Kategory (KATEGORY_ID BIGINT NOT NULL UNIQUE, DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, DESCRIPTION VARCHAR, ICON VARCHAR, NAME VARCHAR NOT NULL, OBJECT BOOLEAN, UUID VARCHAR NOT NULL UNIQUE, PARENT_KATEGORY_ID BIGINT, PRIMARY KEY (KATEGORY_ID))",
				"CREATE TABLE Store (STORE_ID BIGINT NOT NULL UNIQUE, DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, DESCRIPTION VARCHAR, ICON VARCHAR, NAME VARCHAR NOT NULL, SHORTNAME VARCHAR, UUID VARCHAR NOT NULL UNIQUE, PRIMARY KEY (STORE_ID))",
				"CREATE TABLE Event (EVENT_ID BIGINT NOT NULL UNIQUE, ACTIONSCRIPT VARCHAR, DATEARCHIVED TIMESTAMP, DATECOMPLETED TIMESTAMP, DATECREATED TIMESTAMP, DATESCHEDULED TIMESTAMP, DATEUPDATED TIMESTAMP, DESCRIPTION VARCHAR, ICON VARCHAR, NAME VARCHAR NOT NULL, UUID VARCHAR NOT NULL UNIQUE, ITEM_ID BIGINT NOT NULL, PRIMARY KEY (EVENT_ID))",
				"CREATE TABLE Attribute (ATTRIBUTE_ID BIGINT NOT NULL UNIQUE, ATTR_SUBTPYE VARCHAR(31), DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, DESCRIPTION VARCHAR, ICON VARCHAR, NAME VARCHAR NOT NULL, RELEVANCEFROM TIMESTAMP, RELEVANCETO TIMESTAMP, UUID VARCHAR NOT NULL UNIQUE, ITEM_ID BIGINT NOT NULL, ATTRIBUTETYPE_ATTRIBUTETYPE_ID BIGINT, BOOLEANVALUE BOOLEAN, DATEVALUE TIMESTAMP, INTERNALURL BOOLEAN, URL VARCHAR, NUMBERVALUE DOUBLE, TEXTVALUE VARCHAR, PRIMARY KEY (ATTRIBUTE_ID))",
				"CREATE TABLE Attributetype (ATTRIBUTETYPE_ID BIGINT NOT NULL UNIQUE, DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, DEFAULTVALUESCRIPT VARCHAR, DESCRIPTION VARCHAR, ICON VARCHAR, KEY VARCHAR NOT NULL, NAME VARCHAR NOT NULL, PATTERN VARCHAR, TYPE VARCHAR NOT NULL, UNIT VARCHAR, UUID VARCHAR NOT NULL UNIQUE, VALIDATESCRIPT VARCHAR, PRIMARY KEY (ATTRIBUTETYPE_ID))",
				"CREATE TABLE User (USER_ID BIGINT NOT NULL UNIQUE, DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, DESCRIPTION VARCHAR, EMAIL VARCHAR, ICON VARCHAR, NAME VARCHAR NOT NULL, PASSWORDHASH VARCHAR, UUID VARCHAR NOT NULL UNIQUE, PRIMARY KEY (USER_ID))",
				"CREATE TABLE Permission (PERMISSION_ID BIGINT NOT NULL UNIQUE, KEY VARCHAR, NAME VARCHAR, USER_ID BIGINT NOT NULL, PRIMARY KEY (PERMISSION_ID))",
				"CREATE TABLE CONFIG (CONFIG_ID BIGINT NOT NULL UNIQUE, KEY VARCHAR, VALUE VARCHAR, PRIMARY KEY (CONFIG_ID))",
				"CREATE TABLE Context_Kategorie (CONTEXT_ID BIGINT NOT NULL, KATEGORY_ID BIGINT NOT NULL, PRIMARY KEY (CONTEXT_ID, KATEGORY_ID))",
				"CREATE TABLE Item_Kategorie (ITEM_ID BIGINT NOT NULL, KATEGORY_ID BIGINT NOT NULL, PRIMARY KEY (ITEM_ID, KATEGORY_ID))",
				"ALTER TABLE Item ADD CONSTRAINT FK_Item_CONTEXT_ID FOREIGN KEY (CONTEXT_ID) REFERENCES Context (CONTEXT_ID)",
				"ALTER TABLE Item ADD CONSTRAINT FK_Item_USER_ID FOREIGN KEY (USER_ID) REFERENCES User (USER_ID)",
				"ALTER TABLE Item ADD CONSTRAINT FK_Item_FILE_ID FOREIGN KEY (FILE_ID) REFERENCES FileMetadata (FILE_ID)",
				"ALTER TABLE Item ADD CONSTRAINT FK_Item_STORE_STORE_ID FOREIGN KEY (STORE_STORE_ID) REFERENCES Store (STORE_ID)",
				"ALTER TABLE Kategory ADD CONSTRAINT FK_Kategory_PARENT_KATEGORY_ID FOREIGN KEY (PARENT_KATEGORY_ID) REFERENCES Kategory (KATEGORY_ID)",
				"ALTER TABLE Event ADD CONSTRAINT FK_Event_ITEM_ID FOREIGN KEY (ITEM_ID) REFERENCES Item (ITEM_ID)",
				"ALTER TABLE Attribute ADD CONSTRAINT FK_Attribute_ITEM_ID FOREIGN KEY (ITEM_ID) REFERENCES Item (ITEM_ID)",
				"ALTER TABLE Attribute ADD CONSTRAINT FK_Attribute_ATTRIBUTETYPE_ATTRIBUTETYPE_ID FOREIGN KEY (ATTRIBUTETYPE_ATTRIBUTETYPE_ID) REFERENCES Attributetype (ATTRIBUTETYPE_ID)",
				"ALTER TABLE Permission ADD CONSTRAINT FK_Permission_USER_ID FOREIGN KEY (USER_ID) REFERENCES User (USER_ID)",
				"ALTER TABLE Context_Kategorie ADD CONSTRAINT FK_Context_Kategorie_KATEGORY_ID FOREIGN KEY (KATEGORY_ID) REFERENCES Kategory (KATEGORY_ID)",
				"ALTER TABLE Context_Kategorie ADD CONSTRAINT FK_Context_Kategorie_CONTEXT_ID FOREIGN KEY (CONTEXT_ID) REFERENCES Context (CONTEXT_ID)",
				"ALTER TABLE Item_Kategorie ADD CONSTRAINT FK_Item_Kategorie_ITEM_ID FOREIGN KEY (ITEM_ID) REFERENCES Item (ITEM_ID)",
				"ALTER TABLE Item_Kategorie ADD CONSTRAINT FK_Item_Kategorie_KATEGORY_ID FOREIGN KEY (KATEGORY_ID) REFERENCES Kategory (KATEGORY_ID)",
				"CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMERIC(38), PRIMARY KEY (SEQ_NAME))",
				"INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)",

						
			 
			 // View is not autogenerated from jpa
				 "create or replace view VW_CONTEXT as select c.* , count (i.context_id) as sum from context c left join item i on i.context_id = c.CONTEXT_ID where c.datearchived is null and i.datearchived is null group by c.CONTEXT_ID order by c.CONTEXT_ID"

		};

	
			  

		  
	
		
		
		


		
		
}
