package net.schwehla.matrosdms.domain.core;

import java.util.HashMap;
import java.util.Map;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategoryList;
import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;



/**
 * Entspricht einer Mappe welche InfoItems aufnehmen kann Diese aber als proxy,
 * da die Detail-Informationen im Service nachgeladen werden müssen
 * 
 * Zur Zeit mit drei Taglisten ausgestattet
 * 
 * @author Martin
 *
 */
public class InfoContext extends InfoBaseElement {
	

	// CHACHING: Eventuell ein Observer-Paatern auf das Dictionary
	
	// Objektgraph wird zu groß
	// Hier ein cut, die Detailinformationen müssen über einen Service
	// nachgeladen werden

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map <Identifier,InfoKategoryList> dictionary;
	

	// Flag showing that tagging is incomplete
	private int stage;
	
	
	// Filtered or not
	transient boolean visible = true;
	
	private transient InfoItemListProxy  storableInfoItemContainerListProxy;
	
	private InfoContext() {
	}

	public void init() {
	
		if (dictionary == null) {
			dictionary = new HashMap<Identifier,InfoKategoryList>();
		}
	
		dictionary.clear();
		
		// Descrioption is the name in the GUI
		InfoKategoryList wer = new InfoKategoryList(MyGlobalConstants.ROOT_WER,"Wer");
		wer.setDescription("Wer");
		
		InfoKategoryList was = new InfoKategoryList(MyGlobalConstants.ROOT_WAS,"Was");
		wer.setDescription("Was");
		
		InfoKategoryList wo  = new InfoKategoryList(MyGlobalConstants.ROOT_WO ,"Wo");
		wo.setDescription("Wo");
	
		// Wer,Was,Wo
		dictionary.put(wer.getIdentifier(),wer);
		dictionary.put(was.getIdentifier(),was);
		dictionary.put(wo.getIdentifier() ,wo );
		
	}

	// Objektgraph wird zu groß
	// Hier ein cut, die Detailinformationen müssen über einen Service
	// nachgeladen werden

	public InfoContext(Identifier pk, String name) {

		super(pk, name);
		dictionary = new HashMap<Identifier,InfoKategoryList>();
		init();

	}

	public InfoKategoryList getTagList(Identifier key) {
		return dictionary.get(key);
	}

	
	public InfoContext(Identifier pk,  InfoBaseElement element) {

		super(pk, element);
		dictionary = new HashMap<Identifier,InfoKategoryList>();
		init();

	}

	public Map<Identifier, InfoKategoryList> getDictionary() {
		return dictionary;
	}


	public InfoItemListProxy getStorableInfoItemContainerListProxy() {
		return storableInfoItemContainerListProxy;
	}

	public void setStorableInfoItemContainerListProxy(
			InfoItemListProxy storableInfoItemContainerListProxy) {
		this.storableInfoItemContainerListProxy = storableInfoItemContainerListProxy;
	}


	
	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
	


	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
}


