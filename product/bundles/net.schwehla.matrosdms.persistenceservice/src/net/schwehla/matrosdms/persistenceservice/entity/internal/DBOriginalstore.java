package net.schwehla.matrosdms.persistenceservice.entity.internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Store")

@NamedQueries ( {
@NamedQuery(name="DBOriginalstore.findAll", query="SELECT c FROM DBOriginalstore c"),
@NamedQuery(name="DBOriginalstore.findByUUID", query="SELECT c FROM DBOriginalstore c where c.uuid = :uuid"),
@NamedQuery(name="DBOriginalstore.findById", query="SELECT c FROM DBOriginalstore c where c.id = :id") })

public class DBOriginalstore extends AbstractDBInfoBaseEntity  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="STORE_ID")
	private Long id;
	
	String shortname;
	
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	
	public Long getStoreId() {
		return this.id;
	}

	@Override
	public Long getPK() {
		return id;
	}
	
}
