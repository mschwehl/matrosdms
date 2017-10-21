package net.schwehla.matrosdms.persistenceservice.entity.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.schwehla.matrosdms.persistenceservice.entity.internal.attribute.AbstractDBInfoAttribute;
import net.schwehla.matrosdms.persistenceservice.entity.internal.management.DBUser;

@Entity
@Table(name="Item")

@NamedQueries ( {
@NamedQuery(name="DBItem.findByID", query="SELECT c FROM DBItem c where c.id = :id "),
@NamedQuery(name="DBItem.findAll", query="SELECT c FROM DBItem c"),
@NamedQuery(name="DBItem.findAllForVerify", query="SELECT c FROM DBItem c"),
@NamedQuery(name="DBItem.countOriginals", query="SELECT count(c) FROM DBItem c where c.store = :id "),
@NamedQuery(name="DBItem.findByMetadataId", query="SELECT c FROM DBItem c where c.file.id = :id "),
@NamedQuery(name="DBItem.findAllNotArchivedByContextid", query="SELECT c FROM DBItem c where c.infoContext.id = :id and c.dateArchived is null "),
@NamedQuery(name="DBItem.findAllByContextid", query="SELECT c FROM DBItem c where c.infoContext.id = :id "),


@NamedQuery(name="DBItem.findByUUID", query="SELECT c FROM DBItem c where c.uuid = :uuid") })


public class DBItem extends AbstractDBInfoBaseEntity  {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="ITEM_ID")
	private Long id;
	
	// important else parent is given
	public Long getPK() {
		return this.id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	@JoinColumn(name="CONTEXT_ID" ,nullable = false)
	private DBContext infoContext;
	
	private DBOriginalstore store;
	
	@JoinColumn(name="USER_ID" ,nullable = false)
	private DBUser user;
	

	@Temporal(TemporalType.TIMESTAMP)
	Date lastIndexRun;
	
	@Column
	private int indexState;

	
	@Temporal(TemporalType.TIMESTAMP)
	Date issueDate;
	
	
  public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

@ManyToMany 
  @JoinTable(
      name="Item_Kategorie",
      joinColumns=@JoinColumn(name="ITEM_ID", referencedColumnName="ITEM_ID"),
      inverseJoinColumns=@JoinColumn(name="KATEGORY_ID", referencedColumnName="KATEGORY_ID"))
  private List<DBKategorie> typList = new ArrayList<DBKategorie>();


	  
	public Date getLastIndexRun() {
	return lastIndexRun;
}

public void setLastIndexRun(Date lastIndexRun) {
	this.lastIndexRun = lastIndexRun;
}

public int getIndexState() {
	return indexState;
}

public void setIndexState(int indexState) {
	this.indexState = indexState;
}

// Incomplete usw
  @Column
  private int stage;

	

	public List<DBKategorie> getTypList() {
		return typList;
	}

	public void setTypList(List<DBKategorie> typList) {
		this.typList = typList;
	}


	@OneToMany(mappedBy="item" , orphanRemoval=true)
	private List <AbstractDBInfoAttribute> attributeList = new ArrayList<AbstractDBInfoAttribute> ();

	/** number of the document */
	private String storageItemIdentifier;

	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="FILE_ID", unique= true, nullable=true, insertable=true, updatable=true)
	private DBItemMetadata file;

	public DBUser getUser() {
		return user;
	}
	
	public void setUser(DBUser user) {
		this.user = user;
	}



	public DBOriginalstore getStore() {
		return store;
	}
	

	public List<AbstractDBInfoAttribute> getAttributeList() {
		return attributeList;
	}


	public void setAttributeList(List<AbstractDBInfoAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public String getStorageItemIdentifier() {
		return storageItemIdentifier;
	}


	public void setStorageItemIdentifier(String storageItemIdentifier) {
		this.storageItemIdentifier = storageItemIdentifier;
	}


	public void setStore(DBOriginalstore store) {
		this.store = store;
	}



	public DBItemMetadata getFile() {
		return file;
	}


	public void setFile(DBItemMetadata file) {
		this.file = file;
	}


	public DBContext getInfoContext() {
		return infoContext;
	}


	public void setInfoContext(DBContext infoContext) {
		this.infoContext = infoContext;
	}


	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}



	
}
