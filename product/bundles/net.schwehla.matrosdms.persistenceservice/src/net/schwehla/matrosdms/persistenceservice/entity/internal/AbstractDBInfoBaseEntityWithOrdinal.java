package net.schwehla.matrosdms.persistenceservice.entity.internal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Base-Entity
 * @author Martin
 */

@MappedSuperclass
public abstract class AbstractDBInfoBaseEntityWithOrdinal extends AbstractDBInfoBaseEntity {
	
	public int getOridinal() {
		return oridinal;
	}

	public void setOridinal(int oridinal) {
		this.oridinal = oridinal;
	}

	@Column(unique=true,nullable=false,updatable=true)
	int oridinal;

	
}
