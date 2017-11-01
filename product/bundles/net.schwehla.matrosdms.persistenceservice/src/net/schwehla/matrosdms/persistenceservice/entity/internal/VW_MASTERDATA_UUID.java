package net.schwehla.matrosdms.persistenceservice.entity.internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="VW_MASTERDATA_UUID")

@NamedQueries ({
@NamedQuery(name="VW_MASTERDATA_UUID.findAll", query="SELECT c FROM VW_MASTERDATA_UUID c") ,
})


public class VW_MASTERDATA_UUID  {
	

	@Column(unique=false,nullable=false, name="ITEM_ID")
	private Long ID;
	
	@Column(unique=false,nullable=false,updatable=false, name="TAB_TYPE")
	private String TYPE;

	@Id
	@Column(unique=true,nullable=false,updatable=false)
	private String UUID;

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}


}
