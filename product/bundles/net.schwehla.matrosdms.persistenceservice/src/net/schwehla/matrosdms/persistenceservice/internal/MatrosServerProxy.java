package net.schwehla.matrosdms.persistenceservice.internal;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.internal.databaseaccess.DatabaseAccessor;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ConnectionPool;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.flywaydb.core.Flyway;

import net.schwehla.matrosdms.domain.admin.AppSettings;
import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.api.IIdentifiable;
import net.schwehla.matrosdms.domain.core.idm.MatrosUser;
import net.schwehla.matrosdms.persistenceservice.CacheConstants;
import net.schwehla.matrosdms.persistenceservice.entity.internal.management.DBConfig;
import net.schwehla.matrosdms.persistenceservice.internal.cache.Matroscache;
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

	
	 
	 MyCaches myCaches;
	 

	
		public MatrosServerProxy() {
			
			CacheManager cacheManager;
			
			 cacheManager = CacheManagerBuilder.newCacheManagerBuilder() 
//				    .withCache("preConfigured",
//				        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100))) 
				    .withClassLoader(this.getClass().getClassLoader())
				    .build(); 
				cacheManager.init(); 
				
				
				myCaches = new MyCaches();
				 
				 myCaches.addCache(CacheConstants.ORIGINALSTORE,	 cacheManager.createCache(CacheConstants.ORIGINALSTORE, 
						    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class, ResourcePoolsBuilder.heap(10))));
				 
				 
				 myCaches.addCache(CacheConstants.INFOKATEGORY,	 cacheManager.createCache(CacheConstants.INFOKATEGORY, 
						    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class, ResourcePoolsBuilder.heap(10))));
				 

				 // Default: 500 elements
				 myCaches.addCache(CacheConstants.INFOITEM,	 cacheManager.createCache(CacheConstants.INFOITEM, 
						    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class, ResourcePoolsBuilder.heap(500))));

				 
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

		
		
		private String toCacheIdentifer(String methodName, Object[] array) {
			
			if (array == null || array.length == 0) {
				return methodName;
			}
			
			if (array.length == 1) {
				return methodName + "_" + array[0].toString();
			}
			
			throw new IllegalStateException("too much parameter!");
			
			
		}
		  
	
		
		

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
			
			Matroscache cached = method.getAnnotation(Matroscache.class);
			
			String identifier = null;

			
			if (cached != null) {
				 
				if ("NONE".equals(cached.name()) && cached.evictAll() == false) {
					 myCaches.clear();
					throw new IllegalStateException("No Cache specified: " + method.getName());
				}
				
				if (cached.evictAll()) {
					 myCaches.clear();
				} else {
					
					
					identifier = toCacheIdentifer(cached.name(),args);
					if (myCaches.get(cached.name()).containsKey(identifier)) {
//						System.out.println("cache hit " + identifier);
						return myCaches.get(cached.name()).get(identifier);
					}
					
				}
				
				
			}
			
			
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
				
				Object result = method.invoke(serviceImpl, args);
				
				if (identifier != null) {
					myCaches.get(cached.name()).put(identifier, result);
				}

				return result;
				
				
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
			
			boolean created = f.mkdirs();
			
			if (created == false ) {
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
			
			
			// Eclipselink-Specific
			Session session = getEntityManager(false).unwrap(Session.class);
			  ConnectionPool defaultPool = ((ServerSession)session).getConnectionPool("default");
			  DatabaseAccessor da = (DatabaseAccessor) defaultPool.acquireConnection();
			  
			java.sql.Connection connection = da.getConnection();
			
			Flyway flyway = new Flyway();
			flyway.setDataSource( new DataSource() {
				
				@Override
				public <T> T unwrap(Class<T> iface) throws SQLException {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public boolean isWrapperFor(Class<?> iface) throws SQLException {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void setLoginTimeout(int seconds) throws SQLException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setLogWriter(PrintWriter out) throws SQLException {
//					PrintWriter wrapped = new 
				}
				
				@Override
				public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
					return java.util.logging.Logger.getLogger(MatrosServerProxy.class.getName());
				}
				
				@Override
				public int getLoginTimeout() throws SQLException {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public PrintWriter getLogWriter() throws SQLException {
				    return new PrintWriter(System.out);
				}
				
				@Override
				public Connection getConnection(String username, String password) throws SQLException {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Connection getConnection() throws SQLException {
					return connection;
				}
			});
			
			
			flyway.setClassLoader(this.getClass().getClassLoader());
			
			// TODO: figure out which is relevant
			flyway.setLocations("classpath:setup/database");
			flyway.setLocations("classpath:/");
			
			flyway.migrate();
			/*
			
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
			*/
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
			


		


		
		
		class MyCaches {
			private Map <String, Cache> myCaches = new HashMap();
			
			public void addCache(String key, Cache c) {
			 myCaches.put(key , c);
			}
			
			public void clear() {
				myCaches.values().stream().forEach( e -> e.clear());
			}
			
			public Cache get(String key) {
				return myCaches.get(key);
			}
		}
		


		
		
}
