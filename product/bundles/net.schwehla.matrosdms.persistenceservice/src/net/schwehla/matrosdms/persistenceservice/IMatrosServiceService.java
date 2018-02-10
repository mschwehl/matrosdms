package net.schwehla.matrosdms.persistenceservice;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoEvent;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.InfoItemList;
import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.idm.MatrosUser;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;
import net.schwehla.matrosdms.domain.util.VerifyMessage;
import net.schwehla.matrosdms.persistenceservice.internal.cache.MatrosTransactional;
import net.schwehla.matrosdms.persistenceservice.internal.cache.Matroscache;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public interface IMatrosServiceService {
	
	
	@Matroscache(name=CacheConstants.INFOKATEGORY)
    public InfoKategory getInfoKategoryByIdentifier(Identifier id) throws MatrosServiceException; ;
    
	@Matroscache(evictAll=true)
    public void createInfoKategory(InfoKategory kategorie, Identifier parent) throws MatrosServiceException;;
    
    // Alle dokumente laden
    public List<InfoContext> loadInfoContextList(boolean inclArchive) throws MatrosServiceException;;
        
    // Alle dokumente speichern
    public InfoContext  loadInfoContextByIdentifier(Identifier identifier) throws MatrosServiceException;;
    
	@Matroscache(evictAll=true)
	public void createContext(InfoContext con) throws MatrosServiceException;;

	@Matroscache(evictAll=true)
	public void appendContainer(File droppedFile,  InfoItem myNewContainer) throws MatrosServiceException;;

    // �berblick laden
    public InfoItemList loadInfoItemList(InfoContext context, boolean inclArchive) throws MatrosServiceException;
    
    
    public List <InfoItem> checkForDuplicate(MatrosMetadata metadata) throws MatrosServiceException;
 
    //---- Attribute
    
    public List <AttributeType> loadAttributeTypeList()  throws MatrosServiceException;
    
    public AttributeType getAttributeTypeByIdentifier(Identifier identifier) throws MatrosServiceException;
    
/* Originalstore */    
    
	@Matroscache(name=CacheConstants.ORIGINALSTORE)
    public List <InfoOrginalstore> loadOriginalStoreList()  throws MatrosServiceException;
    
	@Matroscache(name=CacheConstants.ORIGINALSTORE)
    public InfoOrginalstore getOriginalStoreByIdentifer(Identifier identifier)  throws MatrosServiceException;
	
	@Matroscache(name=CacheConstants.ORIGINALSTORE, evictAll=true)
	public void createOriginalStore(InfoOrginalstore store) throws MatrosServiceException;
	
/* end Originalstore */
	
	@Matroscache(evictAll=true)
    public void createUser(MatrosUser user)  throws MatrosServiceException;
    
	@Matroscache(evictAll=true)
    public void grantPermission(String userUUID, String permissionKey)  throws MatrosServiceException;
    
	@Matroscache(evictAll=true)
    public void revokePermission(String userUUID, String permissionKey)  throws MatrosServiceException;

	public MatrosUser checkLogin(MatrosConnectionCredential dbCredentials) throws MatrosServiceException;

	public FileInputStream  getStreamedContent(Identifier identifier) throws MatrosServiceException;

	public int getNextFreeOriginalstoreNumber(Identifier identifier) throws MatrosServiceException;

	@Matroscache(evictAll=true)
	public void createAttributeType(AttributeType attributeType) throws MatrosServiceException;

	@Matroscache(evictAll=true)
	public void archivareContext(Identifier identifier)  throws MatrosServiceException;

	public boolean verifyDatabase(VerifyMessage data) throws MatrosServiceException;
	
	@Matroscache(name=CacheConstants.INFOITEM)
	public InfoItem loadInfoItemByIdentifier(Identifier identifier)throws MatrosServiceException;

	@Matroscache(evictAll=true)
	public void updateInfoElement(InfoItem infoItem)  throws MatrosServiceException;

	@Matroscache(evictAll=true)
	public void moveInfoitems(Identifier target, List<InfoItem> intersection) throws MatrosServiceException;
	
	@Matroscache(evictAll=true)
	public void archivareItem(Identifier identifier) throws MatrosServiceException;

	public List<InfoEvent> loadEventList() throws MatrosServiceException;

	@Matroscache(evictAll=true)
	public void updateInfoContext(InfoContext _context) throws MatrosServiceException;

	@Matroscache(evictAll=true)
	public void updateCloudSettings(CloudSettings settings)  throws MatrosServiceException;

	public AttributeType loadAttributeTypeByIdentifier(Identifier identifier) throws MatrosServiceException;
	
	@MatrosTransactional(Transaction=false)
	public List <SearchedInfoItemElement> searchInfoContextItems(SearchItemInput input) throws MatrosServiceException;

	@Matroscache(evictAll=true)
	public void updateInfoKategory(InfoKategory localTreeroot, Identifier identifier) throws MatrosServiceException;

// Database-Functions	
	
	// create initial database
	public void setupInitialCategories() throws MatrosServiceException;

	// rootTagsExistent
	public boolean isDatabaseSetupComplete() throws MatrosServiceException;

	public void backup()  throws MatrosServiceException;
	
// Exporting functions
	
	public String getMetadata() throws MatrosServiceException;

	public boolean existsInMasterdata() throws MatrosServiceException;

	public boolean existsInTransactiondata() throws MatrosServiceException;

    
}
