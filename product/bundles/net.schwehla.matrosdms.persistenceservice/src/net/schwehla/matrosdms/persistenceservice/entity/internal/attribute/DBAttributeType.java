package net.schwehla.matrosdms.persistenceservice.entity.internal.attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import net.schwehla.matrosdms.persistenceservice.entity.internal.AbstractDBInfoBaseEntity;

/**
 * @author Martin
 *
 */

@NamedQueries ({
@NamedQuery(name="DBAttributeType.findAll", query="SELECT c FROM DBAttributeType c") ,
@NamedQuery(name="DBAttributeType.findByUUID", query="SELECT c FROM DBAttributeType c where c.uuid = :uuid") 
})


@Entity
@Table(name="Attributetype")
public class DBAttributeType  extends AbstractDBInfoBaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="ATTRIBUTETYPE_ID")
	private Long id;
	
	
	public Long getPK() {
		return id;
	}

	@Column(nullable = false) 
	String key;
	
	@Column(nullable = false) 
	String type;
	

	@Column
	String defaultValuescript;
	
	@Column
	String validateScript;
	
	@Column
	String pattern;

	@Column
	String unit;
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValidateScript() {
		return validateScript;
	}

	public void setValidateScript(String validateScript) {
		this.validateScript = validateScript;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDefaultValuescript() {
		return defaultValuescript;
	}

	public void setDefaultValuescript(String defaultValuescript) {
		this.defaultValuescript = defaultValuescript;
	}



	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// type of the subtable
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
}
