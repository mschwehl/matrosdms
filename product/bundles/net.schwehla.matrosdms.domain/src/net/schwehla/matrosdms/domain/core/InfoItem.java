package net.schwehla.matrosdms.domain.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.schwehla.matrosdms.adapter.IDAdapter;
import net.schwehla.matrosdms.adapter.ParentArrayAdapter;
import net.schwehla.matrosdms.domain.api.ITagInterface;
import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategoryList;
import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;


@XmlAccessorType(XmlAccessType.FIELD)
public class InfoItem extends InfoBaseElement {
	

	private static final long serialVersionUID = 1L;

	private MatrosMetadata metadata ;


	// auf optional umstellen
	public MatrosMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(MatrosMetadata metadata) {
			this.metadata = metadata;
	}
	
	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}


	public List<AbstractInfoAttribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<AbstractInfoAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	@XmlJavaTypeAdapter(IDAdapter.class) 
	private InfoContext context;
	
	private Identifier storeIdentifier;
	
	private String storeItemNumber;
	


	public String getStoreItemNumber() {
		return storeItemNumber;
	}

	// Flag showing that tagging is incomplete
	private int stage;
	


	private Date lastIndexRun;
	
	private Date issueDate;
	

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getLastIndexRun() {
		return lastIndexRun;
	}

	public void setLastIndexRun(Date lastIndexRun) {
		this.lastIndexRun = lastIndexRun;
	}

	public int getIndexState() {
		return indexState;
	}

	public void setIndexState(int lastIndexState) {
		this.indexState = lastIndexState;
	}

	private int indexState;


	public void setStoreItemNumber(String identifier) {
		this.storeItemNumber = identifier;
	}



	public Identifier getStoreIdentifier() {
		return storeIdentifier;
	}

	public void setStoreIdentifier(Identifier storeIdentifier) {
		this.storeIdentifier = storeIdentifier;
	}

	@XmlJavaTypeAdapter(ParentArrayAdapter.class) 
	private InfoKategoryList typList;
	
	
	@XmlElementWrapper(name="attributeList")
	@XmlElement(name="attribute")
	private List<AbstractInfoAttribute> attributeList;
	


	public InfoItem(InfoContext context, Identifier key , String name) {
		super(key, name);
	
		this.attributeList = new ArrayList<AbstractInfoAttribute>();
		this.typList = new InfoKategoryList(MyGlobalConstants.ROOT_ART, "ART");
		this.context = context;	
	}

	public int getCount() {
		return typList.size();
	}

	public String getTypListAsString() {

		StringBuffer sb = new StringBuffer();

		for (ITagInterface s : typList) {
			sb.append(s.getName());
		}

		return sb.toString();

	}
	

	public InfoContext getContext() {
		return context;
	}

	public void setContext(InfoContext context) {
		this.context = context;
	}



	public InfoKategoryList getTypList() {
		return typList;
	}




}
