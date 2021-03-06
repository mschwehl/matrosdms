package net.schwehla.matrosdms.persistenceservice.entity.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.schwehla.matrosdms.adapter.DateAdapter;

@Entity
@Table(name="VW_SEARCH")

@NamedQueries ({
@NamedQuery(name="VW_SEARCH.findAll", query="SELECT c FROM VW_SEARCH c") ,
@NamedQuery(name="VW_SEARCH.findAllNotArchived", query="SELECT c FROM VW_SEARCH c where c.ELEMENT_ARCHIVED = true") 



})


public class VW_SEARCH  {
	

	public String getITEM_UUID() {
		return ITEM_UUID;
	}
	

	@Column(unique=true,nullable=false,updatable=false)
	private Long CONTEXT_ID;
	
	@Column(unique=true,nullable=false,updatable=false)
	private String CON_NAME;
	
	
	@Column(unique=false,nullable=false,updatable=false)
	private String STORAGEITEMIDENTIFIER;
	
	@Column(unique=false,nullable=false,updatable=false)
	private long STORE_STORE_ID;

	@Column(unique=true,nullable=false,updatable=false)
	private String CON_UUID;
	
	@Column(unique=false,nullable=false,updatable=false)
	private int CON_STAGE;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="ITEM_ID")
	private Long ITEM_ID;
	
	@Column(unique=true,nullable=false,updatable=false)
	private String ITEM_NAME;

	@Column(unique=true,nullable=false,updatable=false)
	private String ITEM_UUID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date CON_DATEARCHIVED;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ITEM_DATEARCHIVED;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ITEM_ISSUEDATE;


	@Column(unique=true,nullable=false,updatable=false)
	private boolean ELEMENT_ARCHIVED;


	public Long getCONTEXT_ID() {
		return CONTEXT_ID;
	}


	public String getCON_NAME() {
		return CON_NAME;
	}


	public Long getITEM_ID() {
		return ITEM_ID;
	}


	public String getITEM_NAME() {
		return ITEM_NAME;
	}


	public Date getCON_DATEARCHIVED() {
		return CON_DATEARCHIVED;
	}


	public Date getITEM_DATEARCHIVED() {
		return ITEM_DATEARCHIVED;
	}


	public boolean isELEMENT_ARCHIVED() {
		return ELEMENT_ARCHIVED;
	}
	
	
	public String getCON_UUID() {
		return CON_UUID;
	}
	

	public Date getITEM_ISSUEDATE() {
		return ITEM_ISSUEDATE;
	}

	public String getSTORAGEITEMIDENTIFIER() {
		return STORAGEITEMIDENTIFIER;
	}


	public void setSTORAGEITEMIDENTIFIER(String sTORAGEITEMIDENTIFIER) {
		STORAGEITEMIDENTIFIER = sTORAGEITEMIDENTIFIER;
	}
	

	public long getSTORE_STORE_ID() {
		return STORE_STORE_ID;
	}


	public void setSTORE_STORE_ID(long sTORE_STORE_ID) {
		STORE_STORE_ID = sTORE_STORE_ID;
	}

	
	

}
