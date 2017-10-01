package net.schwehla.matrosdms.persistenceservice.entity.internal.attribute;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.attribute.InfoNumberAttribute;
import net.schwehla.matrosdms.domain.util.Identifier;

/**
 * @author Martin
 */

@Entity
@DiscriminatorValue("Number")
public class DBNumberAttribute extends AbstractDBInfoAttribute {
	
	
	@Column
	Double numbervalue;



	public Double getNumberValue() {
		return numbervalue;
	}


	public void setNumberValue(Double value) {
		this.numbervalue = value;
	}


	@Override
	public AbstractInfoAttribute buildBusinessEntity(AttributeType type) {
		
		InfoNumberAttribute  number = new InfoNumberAttribute(Identifier.create(getPK(), getUuid() ), getName()) ;
		
		number.setValue(numbervalue);
		number.setType(type);
		
		super.mapReleance(number);
		
		return number;
	}

	@Override
	public void updateAdvancedAttributesByBusinessObject(AbstractInfoAttribute e) {
		
		if (e instanceof InfoNumberAttribute) {
			
			InfoNumberAttribute toUpdate = (InfoNumberAttribute) e;
			
			setNumberValue((Double)toUpdate.getValue());
			
		} else {
			throw new IllegalArgumentException("Wrong Parameter: " +e.toString());
		}
		
		
	}
	

}


