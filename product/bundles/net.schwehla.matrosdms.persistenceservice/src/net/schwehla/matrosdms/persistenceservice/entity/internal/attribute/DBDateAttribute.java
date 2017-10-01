package net.schwehla.matrosdms.persistenceservice.entity.internal.attribute;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.attribute.InfoDateAttribute;
import net.schwehla.matrosdms.domain.util.Identifier;

/**
 * @author Martin
 *
 */

@Entity
@DiscriminatorValue("DATE")


public class DBDateAttribute extends AbstractDBInfoAttribute {
	
	
  @Temporal(TemporalType.TIMESTAMP)
  @Column
  private Date dateValue;

	public Date getDateValue() {
		return dateValue;
	}
	
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	@Override
	public AbstractInfoAttribute buildBusinessEntity(AttributeType type) {

		
		InfoDateAttribute date = new InfoDateAttribute(Identifier.create(getPK(), getUuid() ), getName());
		date.setValue(dateValue);
		date.setType(type);
		
		super.mapReleance(date);
		
		return date;
	
	}

	@Override
	public void updateAdvancedAttributesByBusinessObject(AbstractInfoAttribute e) {
		
		if (e instanceof InfoDateAttribute) {
			
			InfoDateAttribute toUpdate = (InfoDateAttribute) e;
			
			setDateValue((Date) toUpdate.getValue());
			
		} else {
			throw new IllegalArgumentException("Wrong Parameter: " +e.toString());
		}
		
		
		
	}
	

}


