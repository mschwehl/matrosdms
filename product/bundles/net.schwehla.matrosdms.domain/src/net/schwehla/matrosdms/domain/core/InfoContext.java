package net.schwehla.matrosdms.domain.core;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.schwehla.matrosdms.adapter.LinkingInfoBaseElementAdapter;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategoryList;
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
@XmlAccessorType(XmlAccessType.FIELD) 
public class InfoContext extends InfoBaseElement {
	

	// CHACHING: Eventuell ein Observer-Paatern auf das Dictionary
	
	// Objektgraph wird zu groß
	// Hier ein cut, die Detailinformationen müssen über einen Service
	// nachgeladen werden

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlJavaTypeAdapter(LinkingInfoBaseElementAdapter.class) 
	private Map <Identifier,InfoKategoryList> dictionary;
	

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

	@XmlTransient
	public InfoItemListProxy getStorableInfoItemContainerListProxy() {
		return storableInfoItemContainerListProxy;
	}

	public void setStorableInfoItemContainerListProxy(
			InfoItemListProxy storableInfoItemContainerListProxy) {
		this.storableInfoItemContainerListProxy = storableInfoItemContainerListProxy;
	}

	public void setName(String name) {
		super.name = name;
		
	}


	int stage;
	
	transient boolean visible = true;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	
	
}


