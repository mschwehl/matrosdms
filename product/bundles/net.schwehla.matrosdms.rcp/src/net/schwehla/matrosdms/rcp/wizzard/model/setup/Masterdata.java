package net.schwehla.matrosdms.rcp.wizzard.model.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import net.schwehla.matrosdms.domain.admin.AppSettings;
import net.schwehla.matrosdms.domain.admin.CloudSettings;
import net.schwehla.matrosdms.domain.admin.MatrosConnectionCredential;
import net.schwehla.matrosdms.domain.core.idm.MatrosUser;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;
import net.schwehla.matrosdms.rcp.parts.helper.MatrosPreferenceInbox;

@Singleton
@Creatable
public class Masterdata {

	public AppSettings getAppSettings() {
		return appSettings;
	}

	public void setAppSettings(AppSettings appSettings) {
		this.appSettings = appSettings;
	}
	
	
	AppSettings appSettings;
	CloudSettings  cloudSettings ;


	List <MatrosUser> userList = new ArrayList<>();
	List <MatrosPreferenceInbox> inboxList = new ArrayList<>();
	List <InfoOrginalstore> orignalStoreList = new ArrayList<>();
	


	List <AbstractInfoAttribute> infoAttributeList = new ArrayList<>();
	
	public List<AbstractInfoAttribute> getInfoAttributeList() {
		return infoAttributeList;
	}

	public void setInfoAttributeList(List<AbstractInfoAttribute> infoAttributeList) {
		this.infoAttributeList = infoAttributeList;
	}
	Map <Identifier,InfoKategory> dictionary;
	
	MatrosConnectionCredential dbConnection = new MatrosConnectionCredential();
	
	
	public MatrosConnectionCredential getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(MatrosConnectionCredential dbConnection) {
		this.dbConnection = dbConnection;
	}

	public Masterdata() {
		
		dictionary = new HashMap<>();

		{
			InfoKategory fakeroot = new InfoKategory(Identifier.createNEW(),  "ART");
			InfoKategory rootElement = new InfoKategory( MyGlobalConstants.ROOT_ART, "ART <Root>");
			fakeroot.connectWithChild(rootElement);
			
			dictionary.put(MyGlobalConstants.ROOT_ART, fakeroot);
		}{
			InfoKategory fakeroot = new InfoKategory(Identifier.createNEW(), "WER");
	
			InfoKategory rootElement = new InfoKategory(MyGlobalConstants.ROOT_WER, "WER <Root>");
			fakeroot.connectWithChild(rootElement);
			
			dictionary.put(MyGlobalConstants.ROOT_WER, fakeroot);
		}{
			InfoKategory fakeroot = new InfoKategory(Identifier.createNEW(), "WAS");
	
			InfoKategory rootElement = new InfoKategory( MyGlobalConstants.ROOT_WAS, "WAS <Root>");
			fakeroot.connectWithChild(rootElement);
			
			dictionary.put(MyGlobalConstants.ROOT_WAS, fakeroot);
			
		}{
			InfoKategory fakeroot = new InfoKategory(Identifier.createNEW(), "WO");
	
			InfoKategory rootElement = new InfoKategory( MyGlobalConstants.ROOT_WO, "WO <Root>");
			fakeroot.connectWithChild(rootElement);
			
			dictionary.put(MyGlobalConstants.ROOT_WO, fakeroot);
		}
		
		
		
	}
	
	public Map<Identifier, InfoKategory> getDictionary() {
		return dictionary;
	}
	public void setDictionary(Map<Identifier, InfoKategory> dictionary) {
		this.dictionary = dictionary;
	}

	
	public CloudSettings getCloudSettings() {
		return cloudSettings;
	}

	public void setCloudSettings(CloudSettings cloudSettings) {
		this.cloudSettings = cloudSettings;
	}

	public List<MatrosUser> getUserList() {
		return userList;
	}
	public void setUserList(List<MatrosUser> userList) {
		this.userList = userList;
	}
	public List<MatrosPreferenceInbox> getInboxList() {
		return inboxList;
	}
	public void setInboxList(List<MatrosPreferenceInbox> inboxList) {
		this.inboxList = inboxList;
	}
	public List<InfoOrginalstore> getOrignalStoreList() {
		return orignalStoreList;
	}
	public void setOrignalStoreList(List<InfoOrginalstore> orignalStoreList) {
		this.orignalStoreList = orignalStoreList;
	}	
	
	

	

	
	
}
