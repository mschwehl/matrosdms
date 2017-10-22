package net.schwehla.matrosdms.persistenceservice.entity.internal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Base-Entity
 * @author Martin
 */

@MappedSuperclass
public abstract class AbstractDBInfoBaseEntityWithOrdinal extends AbstractDBInfoBaseEntity {
	
	@Column(unique=false,nullable=false,updatable=true)
	int ordinal;
	
	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int Ordinal) {
		this.ordinal = Ordinal;
	}

	
}
