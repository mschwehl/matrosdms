package net.schwehla.matrosdms.persistenceservice.entity.internal.attribute;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.attribute.InfoBooleanAttribute;
import net.schwehla.matrosdms.domain.core.Identifier;

/**
 * @author Martin
 *
 */

@Entity
@DiscriminatorValue("BOOLEAN")

public class DBBooleanAttribute extends AbstractDBInfoAttribute {
	

  @Column
  private Boolean booleanValue;

	public Boolean getBooleanValue() {
		return booleanValue;
	}
	
	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	@Override
	public AbstractInfoAttribute buildBusinessEntity(AttributeType type) {
	
		InfoBooleanAttribute b = new InfoBooleanAttribute(Identifier.create(getPK(), getUuid() ), getName());
		b.setBooleanValue(booleanValue);
		b.setType(type);
		
		super.mapReleance( b);
		
		return b;
	}

	@Override
	public void updateAdvancedAttributesByBusinessObject(AbstractInfoAttribute e) {
		
		if (e instanceof InfoBooleanAttribute) {
			
			InfoBooleanAttribute toUpdate = (InfoBooleanAttribute) e;
			
			setBooleanValue((Boolean) toUpdate.getValue());
			
			 
		} else {
			throw new IllegalArgumentException("Wrong Parameter: " +e.toString());
		}
		
		
		
	}



}


