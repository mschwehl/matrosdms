package net.schwehla.matrosdms.persistenceservice.entity.internal.management;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import net.schwehla.matrosdms.persistenceservice.entity.internal.DBBaseEntity;

@Entity
@Table(name="CONFIG")

@NamedQueries ( {
	@NamedQuery(name="DBConfig.findAll", query="SELECT c FROM DBConfig c"),
	@NamedQuery(name="DBConfig.findByKey", query="SELECT c FROM DBConfig c where c.key = :key ")
})

public class DBConfig extends DBBaseEntity   {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="CONFIG_ID")
	private Long id;
	
	String key;
	
	String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

}
