package net.schwehla.matrosdms.persistenceservice.entity.internal;

import javax.persistence.MappedSuperclass;


/**
 * Base-Entity
 * @author Martin
 */

@MappedSuperclass
public abstract class DBBaseEntity {
	
	public static int MATROSDMS_DB_VERSION = 1;
	
}
