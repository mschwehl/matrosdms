package net.schwehla.matrosdms.persistenceservice.entity.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Base-Entity
 * @author Martin
 */

@MappedSuperclass
public abstract class AbstractDBInfoBaseEntity extends DBBaseEntity {
	
	public abstract Long getPK();

	@Column(unique=true,nullable=false,updatable=false)
	String uuid;

	@Column(unique=false,nullable=false,updatable=true)
	String name;
	
	@Column(unique=false,nullable=true,updatable=true)
	String icon;
	
	@Column(unique=false,nullable=true,updatable=true)
	String description;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	Date dateCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date dateUpdated;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date dateArchived;
	
	public Date getDateUpdated() {
		return dateUpdated;
	}

	public Date getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}


	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}




    /**
     * automatic property set before any database persistence
     */
    @PreUpdate
    @PrePersist
    public void updateDates() {
    	
    	Date d = new Date();
    	
    	if (dateCreated == null) {
    		dateCreated = d;
    	}
    	
    	dateUpdated = d;

    }

    @Override
    public String toString() {
    	return getName();
    }
    
	
}
