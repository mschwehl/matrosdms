package net.schwehla.matrosdms.persistenceservice.internal;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.osgi.framework.FrameworkUtil;

import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.domain.admin.E_CLOUDCRYPTION;
import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.api.E_ATTRIBUTETYPE;
import net.schwehla.matrosdms.domain.api.IIdentifiable;
import net.schwehla.matrosdms.domain.api.ITagInterface;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoEvent;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.InfoItemList;
import net.schwehla.matrosdms.domain.core.InfoItemListProxy;
import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.attribute.InfoBooleanAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoDateAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoNumberAttribute;
import net.schwehla.matrosdms.domain.core.attribute.InfoTextAttribute;
import net.schwehla.matrosdms.domain.core.idm.MatrosUser;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;
import net.schwehla.matrosdms.domain.util.VerifyItem;
import net.schwehla.matrosdms.domain.util.VerifyMessage;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.persistenceservice.entity.internal.AbstractDBInfoBaseEntity;
import net.schwehla.matrosdms.persistenceservice.entity.internal.DBContext;
import net.schwehla.matrosdms.persistenceservice.entity.internal.DBItem;
import net.schwehla.matrosdms.persistenceservice.entity.internal.DBItemMetadata;
import net.schwehla.matrosdms.persistenceservice.entity.internal.DBKategorie;
import net.schwehla.matrosdms.persistenceservice.entity.internal.DBOriginalstore;
import net.schwehla.matrosdms.persistenceservice.entity.internal.VW_CONTEXT;
import net.schwehla.matrosdms.persistenceservice.entity.internal.VW_MASTERDATA_UUID;
import net.schwehla.matrosdms.persistenceservice.entity.internal.VW_SEARCH;
import net.schwehla.matrosdms.persistenceservice.entity.internal.VW_TRANSACTIONDATA_UUID;
import net.schwehla.matrosdms.persistenceservice.entity.internal.attribute.AbstractDBInfoAttribute;
import net.schwehla.matrosdms.persistenceservice.entity.internal.attribute.DBAttributeType;
import net.schwehla.matrosdms.persistenceservice.entity.internal.attribute.DBBooleanAttribute;
import net.schwehla.matrosdms.persistenceservice.entity.internal.attribute.DBDateAttribute;
import net.schwehla.matrosdms.persistenceservice.entity.internal.attribute.DBNumberAttribute;
import net.schwehla.matrosdms.persistenceservice.entity.internal.attribute.DBTextAttribute;
import net.schwehla.matrosdms.persistenceservice.entity.internal.management.DBConfig;
import net.schwehla.matrosdms.persistenceservice.entity.internal.management.DBUser;
import net.schwehla.matrosdms.persistenceservice.internal.cryptprovider.MatrosExternalCryptor;
import net.schwehla.matrosdms.persistenceservice.internal.cryptprovider.MatrosNoCryptor;
import net.schwehla.matrosdms.persistenceservice.internal.export.MatrosMetadataGenerator;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyEventConstants;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;

public class MatrosServiceImpl implements IMatrosServiceService {
	


	IEventBroker eventBroker;
	public void setEventBroker(IEventBroker eventBroker) {
		this.eventBroker = eventBroker;
		
	}

	MatrosObjectStore os;
	
	Map<String,String> readConfigTable;


	Map <Identifier,InfoKategory> fastAccess;

	EntityManager em;
	Logger logger = Logger.getLogger(MatrosServiceImpl.class.getName());
	
	MatrosUser user;
	


	public void setUser(MatrosUser user) {
		this.user = user;	
	}

	
	public void setEntityManager(EntityManager em) {
    	this.em = em;
    }
    	
	
//-------------------------------------------- Check database ---------------------------------------
	

	// Check tables and root-Tags are present
	public boolean isDatabaseSetupComplete() throws MatrosServiceException {
		
	
		 em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_WER.getUuid()).getSingleResult();
		 em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_WAS.getUuid()).getSingleResult();
		 em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_WO.getUuid()) .getSingleResult();
		 em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_ART.getUuid()).getSingleResult();
		 
		 return true;
			
	}
	
	/**
	 * Create Category
	 */
	public void setupInitialCategories() throws MatrosServiceException {
		
		
		DBKategorie wer = new DBKategorie();
		wer.setUuid(MyGlobalConstants.ROOT_WER.getUuid());
		wer.setName("Wer");
		wer.setOrdinal(0);
	
		DBKategorie was = new DBKategorie();
		was.setUuid(MyGlobalConstants.ROOT_WAS.getUuid());
		was.setName("Was");
		was.setOrdinal(0);
		
		DBKategorie wo = new DBKategorie();
		wo.setUuid(MyGlobalConstants.ROOT_WO.getUuid());
		wo.setName("Wo");
		wo.setOrdinal(0);
		DBKategorie art = new DBKategorie();
		art.setUuid(MyGlobalConstants.ROOT_ART.getUuid());
		art.setName("Art");
		art.setOrdinal(0);
		
		em.persist(wer);
		em.persist(was);
		em.persist(wo);
		em.persist(art);
		
	}
	
	
	@Override
	public void updateCloudSettings(CloudSettings directory) throws MatrosServiceException {
	
		updateConfiguration(MyGlobalConstants.Configuration.MATROSCOUD_ROOTATH, directory.getPath());
		
		if (directory.getCryptSettings().isCrypted()) {
			
			updateConfiguration(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_PASSWORD, directory.getCryptSettings().getPassword());
			updateConfiguration(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_TYPE, directory.getCryptSettings().getCryption().name());
			
			updateConfiguration(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_EXEPATH, directory.getCryptSettings().getExePath());
			updateConfiguration(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_ENCRYPTLINE, directory.getCryptSettings().getExeEncryptline());
			updateConfiguration(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_DECRYPTLINE, directory.getCryptSettings().getExeDecryptline());
			
		}
		
		
	}
	

	private void updateConfiguration(String key, String value) throws MatrosServiceException {

		List <DBConfig> resultList = 
		em.createNamedQuery("DBConfig.findByKey", DBConfig.class).setParameter("key", key).getResultList();
		
		// key exists
		if (resultList.size() == 1) {
			
			resultList.get(0).setValue(value);
			
			em.merge(resultList.get(0));
		} else {
			
			DBConfig configElement = new DBConfig();
			configElement.setKey(key);
			configElement.setValue(value);
			
			em.persist(configElement);
		}
		
		
	}


	
	
	@Override
	public InfoKategory getInfoKategoryByIdentifier(Identifier identifier) throws MatrosServiceException  {
		
		if (fastAccess == null || fastAccess.get(identifier)  == null)  {
			DBKategorie rootEntity = em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_WER.getUuid()).getSingleResult();
			mapRecurive(rootEntity);
			
			rootEntity = em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_WAS.getUuid()).getSingleResult();
			mapRecurive(rootEntity);
			
			rootEntity = em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_WO.getUuid()).getSingleResult();
			mapRecurive(rootEntity);
			
			rootEntity = em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", MyGlobalConstants.ROOT_ART.getUuid()).getSingleResult();
			mapRecurive(rootEntity);
		
		} 

		
		InfoKategory i =  (InfoKategory) fastAccess.get(identifier);
		
		if (i == null) {
			throw new MatrosServiceException("Cannot find Element: " + identifier.getUuid());
		}
		
		if ( MyGlobalConstants.ROOT_ART.equals(identifier) ) {
			i.setDropfieldKategory(false);
		} else {
			i.setDropfieldKategory(true);
		}
		
		return (InfoKategory) fastAccess.get(identifier);
	}
	


	@Override
	public List<InfoContext> loadInfoContextList(boolean inclArchived) throws MatrosServiceException {
		
		 List<InfoContext> resultList = new ArrayList();
		 
		 List <VW_CONTEXT> dbResult = null;
		 
		 if (inclArchived) {
			 dbResult = em.createNamedQuery("VW_CONTEXT.findAll", VW_CONTEXT.class).getResultList(); 
		 } else {
			 dbResult = em.createNamedQuery("VW_CONTEXT.findAllNotArchived", VW_CONTEXT.class).getResultList(); 
		 }

			
		 for (VW_CONTEXT element : dbResult) {
			 
//		//	InfoContext c = mapContext(element);
//	             
		//	 for (int i =0 ; i < 100000; i++) {
//				

				InfoContext c = mapContext(element);
				
		//		c.getIdentifier().lastTest();
		//		c.setName(UUID.randomUUID().toString());
			
			
	      
				resultList.add(c);
				
			// }
		 }
			
		

		
		return resultList;
	}



	private InfoContext mapContext(VW_CONTEXT element) throws MatrosServiceException {
		InfoContext c = new InfoContext(Identifier.create(element.getPK(), element.getUuid()), element.getName());
		
		mapBasicPropertiesFromDatabaseToModel(c, element);
		c.init();
		c.setStage(element.getStage());
		
		for (DBKategorie dbKat : element.getKategorieList()) {
			
			InfoKategory mapped = getInfoKategoryByIdentifier(Identifier.create(dbKat.getPK(), dbKat.getUuid()));
			c.getDictionary().get(mapped.getRoot().getIdentifier()).add(mapped);
		}
		 
		c.setStorableInfoItemContainerListProxy(new InfoItemListProxy(c));
		c.getStorableInfoItemContainerListProxy().setCount(element.getSum());
		return c;
	}
	
	




	
	
	@Override
	public InfoContext loadInfoContextByIdentifier(Identifier identifier) throws MatrosServiceException {
	
		
				 
		 VW_CONTEXT element = em.createNamedQuery("VW_CONTEXT.findByUUID", VW_CONTEXT.class).setParameter("uuid",identifier.getUuid()).getSingleResult();

			InfoContext c = mapContext(element);

	        
	        return c;
	
		
		
	}
	
	

	@Override
	public InfoItem loadInfoItemByIdentifier(Identifier identifier) throws MatrosServiceException {
	
		
		
		DBItem dbItem = em.createNamedQuery("DBItem.findByUUID", DBItem.class).setHint(QueryHints.REFRESH, HintValues.TRUE)
				.setParameter("uuid",identifier.getUuid()).getSingleResult();
		
		DBContext  dbContext = dbItem.getInfoContext();
		
		InfoContext c = new InfoContext(Identifier.create(dbContext.getPK(), dbContext.getUuid()), dbContext.getName());
		c.init();
		
		for (DBKategorie dbKat : dbContext.getKategorieList()) {
			
			InfoKategory mapped = getInfoKategoryByIdentifier(Identifier.create(dbKat.getPK(), dbKat.getUuid()));
			c.getDictionary().get(mapped.getRoot().getIdentifier()).add(mapped);
		}
		 
		c.setIcon(dbContext.getIcon());
		 
		c.setStorableInfoItemContainerListProxy(new InfoItemListProxy(c));
		c.getStorableInfoItemContainerListProxy().setCount(-1);
		
		InfoItem result =  mapItem(c, dbItem);
	
		
		return result;
			
	}

	@Override
	public FileInputStream getStreamedContent(Identifier identifier) throws MatrosServiceException {
	
		if (getObjectStore().exists( identifier)) {
			return getObjectStore().getStreamedContent(identifier);
		} else {
			throw new MatrosServiceException("File not exists: " + identifier);
		}
		
		
	}
	
	
	@Override

	public List<InfoOrginalstore> loadOriginalStoreList() throws MatrosServiceException {

		 List<InfoOrginalstore> rv = new ArrayList();


		 
		 List <DBOriginalstore> dbResult = em.createNamedQuery("DBOriginalstore.findAll", DBOriginalstore.class).getResultList();

			
		 for (DBOriginalstore dbOrginalstore : dbResult) {
			 
			 InfoOrginalstore beOrginalstore = new InfoOrginalstore(Identifier.create(dbOrginalstore.getStoreId(), dbOrginalstore.getUuid()), dbOrginalstore.getName());
			 mapBasicPropertiesFromDatabaseToModel(beOrginalstore, dbOrginalstore);
			             
			 // extended Attributes
			 beOrginalstore.setShortname(dbOrginalstore.getShortname());
			 beOrginalstore.setOrdinal(dbOrginalstore.getOrdinal());
			 
			rv.add(beOrginalstore);
		 }
			

		return rv;
	
	}


	@Override
	public int getNextFreeOriginalstoreNumber(Identifier identifier) throws MatrosServiceException {
	
		DBOriginalstore store = em.createNamedQuery("DBOriginalstore.findByUUID", DBOriginalstore.class)
				.setParameter("uuid",identifier.getUuid()).getSingleResult();
		
		String query = "select count(*) from item where item.STORE_STORE_ID =  " + store.getStoreId();
		
		return Integer.parseInt( em.createNativeQuery(query).getSingleResult().toString() ) + 1;

		
	}


	
	@Override
	public InfoOrginalstore getOriginalStoreByIdentifer(Identifier identifier) throws MatrosServiceException {
		
		DBOriginalstore element = em.createNamedQuery("DBOriginalstore.findByUUID", DBOriginalstore.class)
				.setParameter("uuid",identifier.getUuid()).getSingleResult();
		InfoOrginalstore c = new InfoOrginalstore(Identifier.create(element.getStoreId() , element.getUuid()), element.getName());

		
		 // extended Attributes
		 c.setShortname(element.getShortname());
		 
		
		return c;
	 		
	}
	 
	
	
	@Override
	public AttributeType getAttributeTypeByIdentifier(Identifier identifier) throws MatrosServiceException {

		 
		DBAttributeType dbType = em.createNamedQuery("DBAttributeType.findByUUID", DBAttributeType.class)
				.setParameter("uuid",identifier.getUuid()).getSingleResult();

		AttributeType result = createAttributeTypeFromDatabase(dbType);
		
		return result;
		
	}

	// TOOD: Candidate for caching factory
	
	private AttributeType createAttributeTypeFromDatabase(DBAttributeType dbType) {
		AttributeType result = new AttributeType(Identifier.create(dbType.getPK(),dbType.getUuid() ), dbType.getName());
		mapBasicPropertiesFromModelToDatabase(dbType, result);
		
		
		result.setKey(dbType.getKey());
		result.setType(E_ATTRIBUTETYPE.valueOf(dbType.getType()));
		result.setDefaultValueScript(dbType.getDefaultValuescript());
		result.setPattern(dbType.getPattern());
		result.setValidateScript(dbType.getValidateScript());
		result.setUnit(dbType.getUnit());
		return result;
	}

	

	

	@Override

	public void createOriginalStore(InfoOrginalstore store) throws MatrosServiceException {
		
		DBOriginalstore dbStore = new DBOriginalstore();
		mapBasicPropertiesFromModelToDatabase(dbStore, store);
		
		// advanced attributes
		dbStore.setShortname(store.getShortname());
		dbStore.setOrdinal(store.getOrdinal());
		
		em.persist(dbStore);
		
	}
	
	
	@Override
	public void createAttributeType(AttributeType attributeType) throws MatrosServiceException {
	
		DBAttributeType dbType = new DBAttributeType();
		mapBasicPropertiesFromModelToDatabase(dbType, attributeType);
		
		dbType.setKey(attributeType.getKey());
		dbType.setType(attributeType.getType().name());
		dbType.setDefaultValuescript(attributeType.getDefaultValueScript());
		dbType.setPattern(attributeType.getPattern());
		dbType.setValidateScript(attributeType.getValidateScript());
		dbType.setUnit(attributeType.getUnit());
		
		em.persist(dbType);	
		
	}



	

	@Override
	public void createContext(InfoContext beInfoContext) {
		
		DBContext dbContext = new DBContext();
		
		mapBasicPropertiesFromModelToDatabase(dbContext, beInfoContext);
		dbContext.setStage(beInfoContext.getStage());


		for (Identifier rootUUID : beInfoContext.getDictionary().keySet()) {
			
			for (InfoKategory elementKategory : beInfoContext.getDictionary().get(rootUUID) ) {
								
				DBKategorie rootEntity = em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", elementKategory.getIdentifier().getUuid()).getSingleResult();
				dbContext.getKategorieList().add(rootEntity);
			}
		}
		em.persist(dbContext);
	
	}

	
	/**
	 * Append one InfoItem to the Infocontext
	 */
	

	@Override
	public void moveInfoitems(Identifier target, List<InfoItem> intersection) throws MatrosServiceException {


		// Load the context
		DBContext dbContext = em.createNamedQuery("DBContext.findByUUID", DBContext.class)
				.setParameter("uuid", target.getUuid()).getSingleResult();
		
		
		for (InfoItem item : intersection) {
			
			DBItem dbKid = em.createNamedQuery("DBItem.findByID", DBItem.class)
					.setParameter("id", item.getIdentifier().getPk()).getSingleResult();
			
			dbKid.setInfoContext(dbContext);
			em.merge(dbKid);
			
		}
		
		
		em.merge(dbContext);
		
	}
	

	@Override
	public void updateInfoContext(InfoContext infoContext) throws MatrosServiceException {

		DBContext dbContext = em.createNamedQuery("DBContext.findByUUID", DBContext.class)
				.setParameter("uuid",infoContext.getIdentifier().getUuid()).getSingleResult();

		  mapBasicPropertiesFromModelToDatabase(dbContext, infoContext);
		  
		  
			List <IIdentifiable> current = new ArrayList<>();
		  
			for (Identifier rootUUID : infoContext.getDictionary().keySet()) {
				current.addAll(infoContext.getDictionary().get(rootUUID) );
			}
		
			  Function<IIdentifiable, AbstractDBInfoBaseEntity> funcGetNewStammdata= (IIdentifiable e)-> { 

					return em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class)
				    			.setParameter("uuid", e.getIdentifier().getUuid()).getSingleResult(); 
					
				  
				  } ;
				  
			new DBListMerger().merge(em, current, dbContext.getKategorieList(), funcGetNewStammdata , (e) -> {} ,
					(e) -> {  dbContext.getKategorieList().remove(e); }  );
			
	
		  
		 em.merge(dbContext);
		 
		 // changed -> we need to evict the cache
		 em.getEntityManagerFactory().getCache().evictAll();
	}
	
	
	

	@Override
	public void updateInfoKategory(InfoKategory infoKategoryToUpdate, Identifier newRootIdentifier)
			throws MatrosServiceException {
		
		 // changed -> we need to evict the cache
		fastAccess = null;
		em.getEntityManagerFactory().getCache().evictAll();

			
		DBKategorie dbKategory =  em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class)
					.setParameter("uuid", infoKategoryToUpdate.getIdentifier(). getUuid() ).getSingleResult();
			
			
		mapBasicPropertiesFromModelToDatabase(dbKategory, infoKategoryToUpdate);
		dbKategory.setObject(infoKategoryToUpdate.isObject());		
		
		
		if (newRootIdentifier != null) {
			
			DBKategorie parentdb =  em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class)
					.setParameter("uuid", newRootIdentifier. getUuid() ).getSingleResult();
			
			dbKategory.setParent(parentdb);
			parentdb.getChildren().add(dbKategory);
			
			DBKategorie oldParent =  em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class)
					.setParameter("uuid", infoKategoryToUpdate.getParents().get(0).getIdentifier() . getUuid() ).getSingleResult();
			
			oldParent.getChildren().remove(dbKategory);
			
			
			em.merge(parentdb);
			em.merge(oldParent);
			
			
		}
		
		em.merge(dbKategory);
		
		
		
	}

	
	

	@Override
	public void updateInfoElement(InfoItem infoItem) throws MatrosServiceException {
		
		DBItem dbItem = em.createNamedQuery("DBItem.findByUUID", DBItem.class).setParameter("uuid",infoItem.getIdentifier().getUuid()).getSingleResult();

	    dbItem.setStage(infoItem.getStage());
	    dbItem.setIndexState(infoItem.getIndexState());
	    dbItem.setLastIndexRun(infoItem.getLastIndexRun());
	    dbItem.setIssueDate(infoItem.getIssueDate());
	    
	    
	    mapBasicPropertiesFromModelToDatabase(dbItem, infoItem);
	    
		mergeMetadata( infoItem, dbItem, dbItem.getFile());
		
		// no update because no properties
		
		  Function<IIdentifiable, AbstractDBInfoBaseEntity> funcGetNewStammdata= (IIdentifiable e)-> { 

			return em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class)
		    			.setParameter("uuid", e.getIdentifier().getUuid()).getSingleResult(); 
			
		  
		  } ;
		
		
		new DBListMerger().merge(em, infoItem.getTypList(), dbItem.getTypList(), funcGetNewStammdata , (e) -> {} ,
				(e) -> {  dbItem.getTypList().remove(e); }  );
		
		
		
		
// Append new Attribute
		Function<AbstractInfoAttribute, AbstractDBInfoBaseEntity> fNewAttribute = (AbstractInfoAttribute e)-> { 

				AbstractDBInfoAttribute o = buildNewDBAttribute(e);
				em.persist(o);
				o.setItem(dbItem);
				
				em.merge(o);
				return  o; 
			
		} ;
				
		
		// Needs to be registered because not read from query
		Consumer<AbstractInfoAttribute> fUpdate = (AbstractInfoAttribute e)-> { 
		
		AbstractDBInfoAttribute dbToUpdate = 
		em.createNamedQuery("AbstractDBInfoAttribute.findByPK", AbstractDBInfoAttribute.class)
		    			.setParameter("id", e.getIdentifier().getPk()).getSingleResult();
		    			
		
		dbToUpdate.setRelevancefrom( e.getFixcalendar().getRelevancefrom());
		dbToUpdate.setRelevanceto(e.getFixcalendar().getRelevanceto());		

		dbToUpdate.updateAdvancedAttributesByBusinessObject(e);
		
		em.merge(dbToUpdate);
		

			
		} ;
		
		
		    			
		new DBListMerger().merge(em, infoItem.getAttributeList(), dbItem.getAttributeList(), fNewAttribute , fUpdate, 
				(e) -> {  dbItem.getAttributeList().remove(e); 
				
				} );
				
        
		em.merge(dbItem.getFile());
		em.merge(dbItem);
		
		// delete cache because of update
		 em.getEntityManagerFactory().getCache().evictAll();
		
	}


	
	
	@Override
	public void appendContainer(File droppedFile, InfoItem myNewContainer) throws MatrosServiceException {
		
		
		// Load the context
		DBContext dbContext = em.createNamedQuery("DBContext.findByUUID", DBContext.class)
				.setParameter("uuid", myNewContainer.getContext().getIdentifier().getUuid()).getSingleResult();
		
		Assert.isTrue(dbContext != null, "No Context found");

		DBUser dbUser = em.createNamedQuery("DBUser.findByUUID", DBUser.class).setParameter("uuid", user.getIdentifier().getUuid()).getSingleResult();
		Assert.isTrue(dbUser != null, "No User found");
		
		
		// Create the new Item	 
		DBItem dbItem = new DBItem();
		dbItem.setUser(dbUser);
	    dbItem.setInfoContext(dbContext);
	    
	    
		mapInfoItemToDbItem(myNewContainer, dbItem);
		
	    dbItem.setStage(myNewContainer.getStage());
	    dbItem.setIndexState(myNewContainer.getIndexState());
	    dbItem.setLastIndexRun(myNewContainer.getLastIndexRun());
	    dbItem.setIssueDate(myNewContainer.getIssueDate());
	    
	    mapBasicPropertiesFromModelToDatabase(dbItem , myNewContainer);
	    
		em.persist(dbItem);
		dbContext.getItemList().add(dbItem);
			 
		mapInfoItemAttributesToDB(myNewContainer, dbItem);
		
		
		DBItemMetadata dbFile = new DBItemMetadata();
		
		mergeMetadata(myNewContainer, dbItem, dbFile);
		
		
		em.persist(dbFile);
		dbItem.setFile(dbFile);
		
		em.merge(dbItem);
		
	
		StoreResult secureHash = getObjectStore().persist(droppedFile, myNewContainer.getIdentifier());
		
		
	    dbFile.setSha256Crypted(secureHash.getSHA256());
	    dbFile.setCryptSettings(secureHash.getCryptSettings());
	    
	    try {
			em.flush();
	    } catch (RuntimeException e) {
	    	
	    	// compensate
	    	
	    	getObjectStore().deleteFileIfExists(myNewContainer.getIdentifier());
	    	
	    	throw e;
	    }

	    
				
	}



	private void mergeMetadata(InfoItem myNewContainer, DBItem dbItem, DBItemMetadata dbFile) {
		dbFile.setFilename(myNewContainer.getMetadata().getFilename());
		dbFile.setMimetype(myNewContainer.getMetadata().getMimetype());
		dbFile.setFilesize(myNewContainer.getMetadata().getFilesize());
		dbFile.setSha256Original(myNewContainer.getMetadata().getSha256());
		
		if (myNewContainer.getStoreIdentifier() != null) {
			
			 DBOriginalstore dbOriginalstore = em.createNamedQuery("DBOriginalstore.findByUUID", DBOriginalstore.class)
					 .setParameter("uuid", myNewContainer.getStoreIdentifier().getUuid()).getSingleResult();
			 
			 // XXX maybe beanvalidation !?
			 Assert.isNotNull(dbOriginalstore);
			 Assert.isNotNull(myNewContainer.getStoreItemNumber());

			 
			 dbItem.setStore(dbOriginalstore);
			 dbItem.setStorageItemIdentifier(myNewContainer.getStoreItemNumber());

		} else {
			dbItem.setStore(null);
		    dbItem.setStorageItemIdentifier(null);
		}
	}



	private void mapInfoItemAttributesToDB(InfoItem myNewContainer, DBItem dbItem) {
		for (AbstractInfoAttribute attribute :  myNewContainer.getAttributeList())	 {

			AbstractDBInfoAttribute newOne = buildNewDBAttribute(attribute);
			
			if (newOne != null ) {
			
				em.persist(newOne);
				newOne.setItem(dbItem);
				dbItem.getAttributeList().add(newOne);
				
			}
	

				
			
		}
	}



	private AbstractDBInfoAttribute buildNewDBAttribute(AbstractInfoAttribute attribute) {
	
		AbstractDBInfoAttribute newOne = null;
	
		if (attribute instanceof InfoTextAttribute) {
			
			DBTextAttribute dbTextAttribute = new DBTextAttribute();
			mapBasicAttributePropertiesFromModelToDatabase(dbTextAttribute, attribute );
			
			dbTextAttribute.setTextValue((String) attribute.getValue());
			
			newOne = dbTextAttribute;
			

			
		} else
		
		if (attribute instanceof InfoBooleanAttribute) {
			
			DBBooleanAttribute dbBooleanAttribute = new DBBooleanAttribute();
			mapBasicAttributePropertiesFromModelToDatabase(dbBooleanAttribute, attribute );
			
			dbBooleanAttribute.setBooleanValue((Boolean)attribute.getValue());
			
			newOne = dbBooleanAttribute;
			
		} else
		
		if (attribute instanceof InfoDateAttribute) {
			
			DBDateAttribute dbDateAttribute = new DBDateAttribute();
			mapBasicAttributePropertiesFromModelToDatabase(dbDateAttribute, attribute );
			
			dbDateAttribute.setDateValue((Date)attribute.getValue());
			
			newOne = dbDateAttribute;
			
			
		} else 
		
		if (attribute instanceof InfoNumberAttribute) {
			
			DBNumberAttribute dbNumber = new DBNumberAttribute();
			mapBasicAttributePropertiesFromModelToDatabase(dbNumber, attribute );
			
			dbNumber.setNumberValue((Double)attribute.getValue());
			
			newOne = dbNumber;
			

			
		} else {
			throw new IllegalStateException("not mappable: " + attribute);
		}
		return newOne;
	}


	@Override
	public void archivareContext(Identifier identifier) throws MatrosServiceException {

		// Load the context
		DBContext dbContext = em.createNamedQuery("DBContext.findByUUID", DBContext.class)
				.setParameter("uuid",identifier.getUuid()).getSingleResult();

		
		dbContext.setDateArchived( new Date());
		
		em.merge(dbContext);
		
	}

	

	@Override
	public void archivareItem(Identifier identifier) throws MatrosServiceException {

		// Load the context
		DBItem dbContext = em.createNamedQuery("DBItem.findByUUID", DBItem.class)
				.setParameter("uuid",identifier.getUuid()).getSingleResult();

		
		dbContext.setDateArchived( new Date());
		
		em.merge(dbContext);
		
		
		
	}



	
	
	@Override
	public InfoItemList loadInfoItemList(InfoContext context , boolean inclArchive) throws MatrosServiceException {
		
		if (context == null) {
			throw new MatrosServiceException("Empty Context, cannot load Details");
		}
		
		InfoItemList result = new InfoItemList(context);
		
		List <DBItem> itemList = null;
		
		if (inclArchive) {
			
			itemList = em.createNamedQuery("DBItem.findAllByContextid", DBItem.class)
					.setParameter("id", context.getIdentifier().getPk()).getResultList();
			
		} else {
			
			itemList = em.createNamedQuery("DBItem.findAllNotArchivedByContextid", DBItem.class)
			.setParameter("id", context.getIdentifier().getPk()).getResultList();
		}
				
			

	
			// Dokumente laden
			for ( DBItem dbItem : itemList ) {
				InfoItem beInfoItem = mapItem(context, dbItem);
				result.add(beInfoItem);
				beInfoItem.setContext(context);
			
			} 
			
		return result;
		
	}



	private InfoItem mapItem(InfoContext context, DBItem dbItem) throws MatrosServiceException {
		InfoItem beInfoItem = new InfoItem(context, Identifier.create(dbItem.getPK(), dbItem.getUuid()), dbItem.getName());
		
		mapBasicPropertiesFromDatabaseToModel(beInfoItem, dbItem);
		
		beInfoItem.setStage(dbItem.getStage());
		beInfoItem.setIndexState(dbItem.getIndexState());
		beInfoItem.setLastIndexRun(dbItem.getLastIndexRun());
		beInfoItem.setIssueDate(dbItem.getIssueDate());
		
		
		if (dbItem.getStore() != null) {
			beInfoItem.setStoreIdentifier(Identifier.create(dbItem.getStore().getStoreId(), dbItem.getStore().getUuid()));
			beInfoItem.setStoreItemNumber(dbItem.getStorageItemIdentifier() );
		}
		
		for (DBKategorie dbtyp : dbItem.getTypList()) {
			
			InfoKategory ik = getInfoKategoryByIdentifier( Identifier.create(dbtyp.getPK(),  dbtyp.getUuid()));
			
			beInfoItem.getTypList().add(ik);
			
		};
		

		
		for (AbstractDBInfoAttribute dbAttribute : dbItem.getAttributeList()) {
			
			AttributeType type = createAttributeTypeFromDatabase(dbAttribute.getAttributeType());
					
			AbstractInfoAttribute newObject = dbAttribute.buildBusinessEntity(type);
			
			mapBasicPropertiesFromDatabaseToModel(newObject, dbAttribute);
			
			beInfoItem.getAttributeList().add(newObject);
			
		};

	
			
		if (dbItem.getFile() != null) {
			
			MatrosMetadata mm = buildMetadataFromDatabase(dbItem.getFile());
			
			// XXX textlayser
			beInfoItem.setMetadata(mm);
		
		}
		return beInfoItem;
	}


	private MatrosMetadata buildMetadataFromDatabase(DBItemMetadata dbItem) {
		MatrosMetadata mm = new MatrosMetadata();
		mm.setFilename( dbItem.getFilename());
		mm.setMimetype(dbItem.getMimetype());
//		mm.setSha256(dbItem.getChecksum());
		return mm;
	}
	



	@Override
	public void createInfoKategory(InfoKategory child, Identifier parentId) {

		fastAccess = null;
			
		DBKategorie dbKategory = new DBKategorie();
		
		mapBasicPropertiesFromModelToDatabase(dbKategory, child);
		
		dbKategory.setObject(child.isObject());
		
		DBKategorie parent =  em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class).setParameter("uuid", parentId.getUuid() ).getSingleResult();
		
		dbKategory.setParent(parent);
		parent.getChildren().add(dbKategory);

		em.persist(dbKategory);
		em.merge(parent);
  		
	}

	@Override
	public List<AttributeType> loadAttributeTypeList() throws MatrosServiceException {
		
		 
		List<AttributeType> result = new ArrayList <AttributeType> ();
		
		for (DBAttributeType dbType: em.createNamedQuery("DBAttributeType.findAll",DBAttributeType.class).getResultList() ) {
			
			AttributeType element = createAttributeTypeFromDatabase(dbType);
			element.setOrdinal(dbType.getOrdinal());
			result.add(element);
		}
		
		return result;
		
	}
	
	
	/**
	 * Checks if the SHA-256-Sum exists already in the database
	 */
	
	@Override
	public List<InfoItem> checkForDuplicate(MatrosMetadata metadata) throws MatrosServiceException {
		
		List<InfoItem> resultList = new ArrayList<InfoItem>();
		
		 List<DBItemMetadata> duplicates = em.createNamedQuery("DBItemMetadata.findByChecksum", DBItemMetadata.class).setParameter("checksum", metadata.getSha256()).getResultList();
		 
		
		 	
		 for (DBItemMetadata dbFile : duplicates ) {
			
			if (dbFile != null) {
				
				DBItem item = em.createNamedQuery("DBItem.findByMetadataId", DBItem.class).setParameter("id", dbFile.getFileId() ) .getSingleResult();
				
				
				
				 InfoContext c = new InfoContext(Identifier.create(item.getInfoContext().getPK() ,  item.getInfoContext().getUuid()), item.getInfoContext().getName()); //$NON-NLS-1$
				
				
				MatrosMetadata mm = buildMetadataFromDatabase(dbFile);
				InfoItem container = new InfoItem(c ,Identifier.create(item.getPK(), item.getUuid()), item.getName());
			 	container.setMetadata(mm);
			 	resultList.add(container);
			}
		 }
		 
		 return resultList;
		 
	}


	@Override
	public MatrosUser checkLogin(MatrosConnectionCredential dbCredentials) throws MatrosServiceException {
		
		try {
			DBUser dbQueryuser = em.createNamedQuery("DBUser.findByNameAndPasswordhash",DBUser.class)
					.setParameter("name", dbCredentials.getDbUser())
					.setParameter("passwordhash", dbCredentials.getDbPasswd()).getSingleResult();
				
				
				MatrosUser matrosUser = new MatrosUser(Identifier.create(dbQueryuser.getPK(), dbQueryuser.getUuid()) , dbQueryuser.getName());
				mapBasicPropertiesFromDatabaseToModel(matrosUser, dbQueryuser);
				
				return matrosUser;
				
		} catch(Exception e) {
			return null;
		}
		
	}

    

	
	@Override
	public void createUser(MatrosUser userX) throws MatrosServiceException {
		
		DBUser dbUser = new DBUser();
		mapBasicPropertiesFromModelToDatabase(dbUser, userX);
		
		/// XXX hasing in eigene klasse
		dbUser.setPasswordhash(userX.getPassword());
		
		em.persist(dbUser);
		
		
	}


	@Override
	public void grantPermission(String userUUID, String permissionKey) throws MatrosServiceException {
		throw new IllegalStateException("Method not implemented");
		
	}


	@Override
	public void revokePermission(String userUUID, String permissionKey) throws MatrosServiceException {
		throw new IllegalStateException("Method not implemented");
	}
	


	//  "CREATE TABLE VW_CONTEXT (ID BIGINT NOT NULL, DATEARCHIVED TIMESTAMP, DATECREATED TIMESTAMP, DATEUPDATED TIMESTAMP, ICON VARCHAR, NAME VARCHAR, SUM INTEGER, UUID VARCHAR NOT NULL UNIQUE, PRIMARY KEY (ID))",
	// 	must be deleted


	 private void mapBasicAttributePropertiesFromModelToDatabase( AbstractDBInfoAttribute dbAbstractAttribute, AbstractInfoAttribute attribute	) {
		
		
		mapBasicPropertiesFromModelToDatabase(dbAbstractAttribute,attribute);	
		
		
		DBAttributeType dbType = em.createNamedQuery("DBAttributeType.findByUUID", DBAttributeType.class)
				.setParameter("uuid",attribute.getType().getIdentifier().getUuid()).getSingleResult();
		dbAbstractAttribute.setAttributeType(dbType);
		
		dbAbstractAttribute.setRelevancefrom( attribute.getFixcalendar().getRelevancefrom());
		dbAbstractAttribute.setRelevanceto(attribute.getFixcalendar().getRelevanceto());		
	}


	private void mapBasicPropertiesFromModelToDatabase( AbstractDBInfoBaseEntity dbBaseAttribute, InfoBaseElement attribute	) {
		
		dbBaseAttribute.setUuid(attribute.getIdentifier().getUuid());
		dbBaseAttribute.setName(attribute.getName());
		dbBaseAttribute.setIcon(attribute.getIcon());		
		dbBaseAttribute.setDescription(attribute.getDescription());
		dbBaseAttribute.setDateArchived(attribute.getDateArchived());
		// creationdate is autogenerated
	}


	public static void mapBasicPropertiesFromDatabaseToModel(InfoBaseElement baseElement,  AbstractDBInfoBaseEntity dbTextAttribute) {
		
		baseElement.setName(dbTextAttribute.getName());
		baseElement.setIcon(dbTextAttribute.getIcon());		
		baseElement.setDescription(dbTextAttribute.getDescription());		
		baseElement.setDateArchived(dbTextAttribute.getDateArchived());		
		baseElement.setDateCreated(dbTextAttribute.getDateCreated());
	}


	/**
	 * rekursives Laden
	 * @param rootEntity
	 * @return
	 */
	
	private InfoKategory mapRecurive(DBKategorie rootEntity) {
	
		
		if (rootEntity == null) {
			throw new IllegalStateException("call with null");
		}
		
		if (fastAccess == null) {
			fastAccess = new HashMap<>();
		}
		
		if (fastAccess.containsKey(rootEntity.getUuid())) {
			throw new IllegalStateException("Zyklus found " + rootEntity.getUuid());
		}
		
	
		InfoKategory element = mapEntity(rootEntity) ;
	
		fastAccess.put(element.getIdentifier(), element);
		
		if (rootEntity.getChildren() != null && rootEntity.getChildren().size() > 0) {
			
			for (DBKategorie kid : rootEntity.getChildren()) {
				
				InfoKategory beKid  = 	mapRecurive(kid);
				element.connectWithChild(beKid);
	
			}
			
		}
		
		return element;
		
		
		
	}


	private InfoKategory mapEntity(DBKategorie rootEntity) {
		
		InfoKategory element = new InfoKategory(Identifier.create(rootEntity.getPK(), rootEntity.getUuid()), rootEntity.getName() );
		
		mapBasicPropertiesFromDatabaseToModel(element, rootEntity);
		element.setObject(rootEntity.isObject());
		element.setOrdinal(rootEntity.getOrdinal());
	
		return element;
		
	}

	 
	

	    public void log(IStatus status)
	    {
	        Platform.getLog(FrameworkUtil.getBundle(IMatrosServiceService.class)).log(status);
	    }

	    public void log(Throwable t)
	    {
	        log(new Status(Status.ERROR, "net.schwehla.matrosdms.persistenceservice", t.getMessage(), t));
	    }

	    public void log(Throwable t, String hint)
	    {
	        log(new Status(Status.ERROR, "net.schwehla.matrosdms.persistenceservice", t.getMessage() + "::" + hint, t));
	    }
	    
	    public void log(String message)
	    {
	        log(new Status(Status.ERROR, "net.schwehla.matrosdms.persistenceservice", message));
	    }

	    public void log(List<Exception> errors)
	    {
	        for (Exception e : errors)
	            log(e);
	    }

	    public boolean isDevelopmentMode()
	    {
	        return System.getProperty("osgi.dev") != null; //$NON-NLS-1$
	    }


		@Override
		public boolean verifyDatabase(VerifyMessage data) throws MatrosServiceException {
			
			boolean allOk = true;
			
			Map <String, VerifyItem> basicCache = new HashMap<>();
	  
						
					  List <DBItem> itemList = em.createNamedQuery("DBItem.findAllForVerify", DBItem.class).getResultList();
				
					
					  for (DBItem dbItemToVerify : itemList) {
							
							Identifier ident =  fromBaseEntity(dbItemToVerify);
							VerifyItem verifyItem = new VerifyItem( );
							verifyItem.setName(dbItemToVerify.getName());
							verifyItem.setUuid(dbItemToVerify.getUuid());
							
							verifyItem.setFoundInDatabase(true);
							
							basicCache.put(dbItemToVerify.getUuid(), verifyItem);
							
							
							
					  }
					  
					  List <Path> allFiles = getObjectStore().getAllFiles();
					  
					  for (Path file : allFiles) {
						  
						  if (basicCache.containsKey(file.getFileName().toString() )) {
						
							  VerifyItem verifyItem = basicCache.get(file.getFileName().toString() );							  
							  verifyItem.setFondInStorage(true);
							  
						  } else {
							  
								VerifyItem verifyItem = new VerifyItem( );
								verifyItem.setName(file.getFileName().toString() );
								verifyItem.setUuid(Identifier.createNEW().getUuid());
								
								verifyItem.setFondInStorage(true);
								basicCache.put(verifyItem.getUuid(), verifyItem);
							  
						  }
						  
							
					  }
						
					  // build the Delta
					  
					  
			
							
					  
					
					  
					 for  (VerifyItem item: basicCache.values()) {
						  
							  
							try {
								
						  if (item.isFondInDatabase()) {
							  
							  if (item.isFondInStorage()) {
								  eventBroker.post(MyEventConstants.TOPIC__VERIFYELEMENT_SUCESS, item );
							  } else {
								  
								  item.setErrotext("only in database");
									allOk &= false;
								  
								  eventBroker.post(MyEventConstants.TOPIC__VERIFYELEMENT_ERROR, item );  
							  }
							  
							  
							  
							  
						  } else {
							  
							  item.setErrotext("only in filesystem");
								allOk &= false;
							  eventBroker.post(MyEventConstants.TOPIC__VERIFYELEMENT_ERROR, item );  
							  
						  }
						  
						  
						  
						  
							
							} catch (Exception e) {
								
								allOk &= false;
								
								logger.warning("Cannot verify Item " + item + "\n" + e.getMessage() );
								
								item.setErrotext(e.getMessage());
								
								eventBroker.post(MyEventConstants.TOPIC__VERIFYELEMENT_ERROR, item );
							}
						  
					  }


			
			return allOk;
		}




		




		private void mapInfoItemToDbItem(InfoItem infoItem, DBItem dbItem) {
			// map missing elements
		    for (ITagInterface art : infoItem.getTypList()) {
			    	
			    	DBKategorie rootEntity = em.createNamedQuery("DBKategorie.findByUUID", DBKategorie.class)
			    			.setParameter("uuid", art.getIdentifier().getUuid()).getSingleResult();
			    	
					dbItem.getTypList().add(rootEntity);
			    	
			    }
			    

		}



		@Override
		public List<InfoEvent> loadEventList() throws MatrosServiceException {
			// TODO Auto-generated method stub
			
			InfoEvent dummy = new InfoEvent( Identifier.createNEW(), "dummy");
			ArrayList a = new ArrayList<>();
			a.add(dummy);
			
			return a;
			
		}




		private Identifier fromBaseEntity(DBItem dbItemToVerify) {
			return Identifier.create(dbItemToVerify.getPK(), dbItemToVerify.getUuid());
		}



		
		public MatrosObjectStore getObjectStore() throws MatrosServiceException {
			
			
	
			if (os == null) {
				
				Map <String,String> configMap = readConfigTable;
				
				String file = configMap.get(MyGlobalConstants.Configuration.MATROSCOUD_ROOTATH) + File.separator + "documents";

				
				E_CLOUDCRYPTION crypt = E_CLOUDCRYPTION.valueOf(  configMap.get(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_TYPE) );
				
				String passwd = configMap.get(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_PASSWORD);

				
				switch (crypt) {

					
					case EXTERNAL:
						
						String exepath = configMap.get(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_EXEPATH);
						String encryptline = configMap.get(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_ENCRYPTLINE);
						String decryptline = configMap.get(MyGlobalConstants.Configuration.CONFIG_OBJECTSTORE_CRYPTED_ENCRYPTLINE);
						
						MatrosExternalCryptor externalCryptor = new MatrosExternalCryptor(Paths.get(file));
						
						externalCryptor.setCryptLine(encryptline);
						externalCryptor.setPassword(passwd);
						externalCryptor.setPathToExe(exepath);
						externalCryptor.setUncryptLine(decryptline);
						
						os = new MatrosObjectStore(externalCryptor);
						
					break;
					
					
					// Not implemented
					case INTERNAL:
						throw new MatrosServiceException("Not implemented");
						
					case NONE:
						
						MatrosNoCryptor matrosNoCryptor = new MatrosNoCryptor(Paths.get(file));
						
						os = new MatrosObjectStore(matrosNoCryptor);
						
						break;
				
				}
				
				
				

					
			
			}
			
			return os;
			
		}


		@Override
		public void backup() throws MatrosServiceException {
			
			Map <String,String> configMap = readConfigTable;
			
			String file = 
			configMap.get(MyGlobalConstants.Configuration.MATROSCOUD_ROOTATH);
			
			String backupPath =  file + File.separator + "backup" + File.separator +  "dbbackup.zip";
			
			// XXX max three rolling backubs and cryption
			
			 em.createNativeQuery("BACKUP TO '" + backupPath + "'").executeUpdate();
		}

		public void setConfiguration(Map<String,String> readConfigTable) {
			
			Objects.requireNonNull(readConfigTable);
			
			this.readConfigTable = readConfigTable;
		}


		@Override
		public AttributeType loadAttributeTypeByIdentifier(Identifier identifier) throws MatrosServiceException {
			
			
			DBAttributeType dbType = em.createNamedQuery("DBAttributeType.findByUUID", DBAttributeType.class).setHint(QueryHints.REFRESH, HintValues.TRUE)					
					.setParameter("uuid",identifier.getUuid()).getSingleResult();
			
			AttributeType element = createAttributeTypeFromDatabase(dbType);
				
			return element;
		
			
		}
	
		@Override
		public List<SearchedInfoItemElement> searchInfoContextItems(SearchItemInput input) throws MatrosServiceException {
	
			List<SearchedInfoItemElement> resultList = new ArrayList<>();
	
			String query =
	
			"SELECT DISTINCT " +

			"  c.CONTEXT_ID as CONTEXT_ID " + ", c.NAME as CON_NAME " + ", c.UUID as CON_UUID "
			+ ", c.STAGE as CON_STAGE " + ", I.ITEM_ID " + ", I.NAME as ITEM_NAME "
			+ ", I.UUID as ITEM_UUID " + ", c.DATEARCHIVED as CON_DATEARCHIVED "
			+ ", I.DATEARCHIVED as ITEM_DATEARCHIVED "
			+ ", I.DATEARCHIVED is not null or c.DATEARCHIVED is not null as ELEMENT_ARCHIVED "
			+ ",I.STORE_STORE_ID " + ",I.STORAGEITEMIDENTIFIER " +

			"FROM CONTEXT C LEFT OUTER JOIN ITEM I ON I.CONTEXT_ID = C.CONTEXT_ID "
			+ "  LEFT OUTER JOIN ATTRIBUTE A ON I.ITEM_ID = A.ITEM_ID "
			+ "  LEFT OUTER JOIN item_kategorie ON I.item_id = item_kategorie.item_id "
			+ "  LEFT OUTER JOIN context_kategorie ON I.context_id = context_kategorie.context_id " +

			// keine leeren Contexte
			"where I.name is not null " ;
			
			if (input.getQueryString() != null) {
				query += input.getQueryString();
			}
			
			query += " ORDER BY CONTEXT_ID,  CON_NAME ";

			try {
				
				List<VW_SEARCH> dbResult = em.createNativeQuery(query, VW_SEARCH.class).getResultList();
				addToResult(resultList, dbResult);
				
			} catch(Exception e) {
				MatrosServiceException mse = new MatrosServiceException(e.getMessage(), e);
				throw mse;
			}
			
			return resultList;
	
		}

		private void addToResult(List<SearchedInfoItemElement> resultList, List<VW_SEARCH> dbResult) {
			InfoContext context = null;
			 
			 for (VW_SEARCH element : dbResult) {

				 	if (context == null || context.getIdentifier().getPk() != element.getCONTEXT_ID()) {
				 		context = new InfoContext(Identifier.create(element.getCONTEXT_ID(), element.getCON_UUID()), element.getCON_NAME());
				 	}
				 
				 
					SearchedInfoItemElement i = new SearchedInfoItemElement(context,Identifier.create(element.getITEM_ID(), element.getITEM_UUID()),
							element.getITEM_NAME() );
					
					i.setContextName(element.getCON_NAME());
					i.setName(element.getITEM_NAME());
					i.setDateArchived(element.getITEM_DATEARCHIVED());
					i.setEffectiveArchived( element.isELEMENT_ARCHIVED() );
					
					i.setSearchFilterString(element.getCON_NAME() + "|" + element.getITEM_NAME());
					
					resultList.add(i);
					
			 }
		}


		@Override
		public String getMetadata() throws MatrosServiceException {
			
			try {
				
				MatrosMetadataGenerator g = new MatrosMetadataGenerator(this);				
				return g.generate();
				
			} catch (Exception e) {
				throw new MatrosServiceException("cannot generate Metadata",e );
			}
		}

		

		@Override
		public boolean existsInMasterdata() throws MatrosServiceException {
	
			 List <VW_MASTERDATA_UUID> dbResult =  em.createNamedQuery("VW_MASTERDATA_UUID.findAll", VW_MASTERDATA_UUID.class).getResultList(); 
			 return  ! dbResult.isEmpty();
			 
			 
			
		}


		@Override
		public boolean existsInTransactiondata() throws MatrosServiceException {
	
			 List <VW_TRANSACTIONDATA_UUID> dbResult =  em.createNamedQuery("VW_TRANSACTIONDATA_UUID.findAll", VW_TRANSACTIONDATA_UUID.class).getResultList(); 
			 return  ! dbResult.isEmpty();
			
		}
		


}
