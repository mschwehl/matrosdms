package net.schwehla.matrosdms.persistenceservice;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.admin.MatrosUser;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoEvent;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.InfoItemList;
import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.domain.util.VerifyMessage;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public interface IMatrosServiceService {
	
	
	// rootTagsExistent
	public boolean isDatabaseSetupComplete() throws MatrosServiceException;
	
	// initiale Datenbak anlegen
	public void setupInitialCategories() throws MatrosServiceException;
	
	// Kategorie laden
    public InfoKategory getInfoKategoryByIdentifier(Identifier id) throws MatrosServiceException; ;
    
    public void createInfoKategory(InfoKategory kategorie, Identifier parent) throws MatrosServiceException;;
    
    // Alle dokumente laden
    public List<InfoContext> loadInfoContextList(boolean inclArchive) throws MatrosServiceException;;
        
    // Alle dokumente speichern
    public InfoContext  loadInfoContextByIdentifier(Identifier identifier) throws MatrosServiceException;;
    
    // neuen Mappe anlegen
    public void createContext(InfoContext con) throws MatrosServiceException;;

    // Dokument der Mappe hinzuf�gen
    public void appendContainer(File droppedFile,  InfoItem myNewContainer) throws MatrosServiceException;;

    // �berblick laden
    public InfoItemList loadInfoItemList(InfoContext context, boolean inclArchive) throws MatrosServiceException;
    
    
    public List <InfoItem> checkForDuplicate(MatrosMetadata metadata) throws MatrosServiceException;
 
    //---- Attribute
    
    public List <AttributeType> loadAttributeTypeList()  throws MatrosServiceException;
    
    public AttributeType getAttributeTypeByIdentifier(Identifier identifier) throws MatrosServiceException;
    
    
    public List <InfoOrginalstore> loadInfoStoreList()  throws MatrosServiceException;
    
    public InfoOrginalstore getInfoStoreByIdentifer(Identifier identifier)  throws MatrosServiceException;
    
    public void createUser(MatrosUser user)  throws MatrosServiceException;
    
    public void grantPermission(String userUUID, String permissionKey)  throws MatrosServiceException;
    
    public void revokePermission(String userUUID, String permissionKey)  throws MatrosServiceException;

	public MatrosUser checkLogin(MatrosConnectionCredential dbCredentials) throws MatrosServiceException;

	public FileInputStream  getStreamedContent(Identifier identifier) throws MatrosServiceException;

	public void createOrignalStore(InfoOrginalstore store) throws MatrosServiceException;

	public int getNextFreeOriginalstoreNumber(Identifier identifier) throws MatrosServiceException;

	public void createAttributeType(AttributeType attributeType) throws MatrosServiceException;

	public void archivareContext(Identifier identifier)  throws MatrosServiceException;

	public void archivareItem(Identifier identifier) throws MatrosServiceException;

	public boolean verifyDatabase(VerifyMessage data) throws MatrosServiceException;

	public void moveInfoitems(Identifier target, List<InfoItem> intersection) throws MatrosServiceException;

	public void updateInfoElement(InfoItem infoItem)  throws MatrosServiceException;

	public InfoOrginalstore loadOrginalstoreByIdentifier(Identifier storeIdentifier) throws MatrosServiceException;

	public InfoItem loadInfoItemByIdentifier(Identifier identifier)throws MatrosServiceException;

	public List<InfoEvent> loadEventList() throws MatrosServiceException;

	public void updateInfoContext(InfoContext _context) throws MatrosServiceException;

	public void updateCloudSettings(CloudSettings settings)  throws MatrosServiceException;

	public void backup()  throws MatrosServiceException;

	public AttributeType loadAttributeTypeByIdentifier(Identifier identifier) throws MatrosServiceException;

	public List <SearchedInfoItemElement> searchInfoContextItems(SearchItemInput input) throws MatrosServiceException;

	public void updateInfoKategory(InfoKategory localTreeroot, Identifier identifier) throws MatrosServiceException;; 

    
    
}
