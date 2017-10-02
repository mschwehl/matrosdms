package net.schwehla.matrosdms.persistenceservice.entity.internal.attribute;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;

/**
 * @author Martin
 *
 */

@Entity
@DiscriminatorValue("LINK")


public class DBLinkAttribute extends AbstractDBInfoAttribute {

	@Column
	String url;
	  @Column
	boolean internalUrl;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isInternalUrl() {
		return internalUrl;
	}
	public void setInternalUrl(boolean internalUrl) {
		this.internalUrl = internalUrl;
	}

	
	@Override
	public AbstractInfoAttribute buildBusinessEntity(AttributeType type) {
		// 
		// XXX
//		TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateAdvancedAttributesByBusinessObject(AbstractInfoAttribute e) {
		// TODO Auto-generated method stub
		
	}
	

	

}


