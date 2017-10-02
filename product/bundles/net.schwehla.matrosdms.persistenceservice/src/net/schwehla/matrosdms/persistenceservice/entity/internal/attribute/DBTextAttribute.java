package net.schwehla.matrosdms.persistenceservice.entity.internal.attribute;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.attribute.InfoTextAttribute;
import net.schwehla.matrosdms.domain.util.Identifier;

/**
 * @author Martin
 *
 */

@Entity
@DiscriminatorValue("TEXT")
public class DBTextAttribute extends AbstractDBInfoAttribute {
	
  @Column
  private String textValue;

	public String getTextValue() {
		return textValue;
	}
	
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	@Override
	public AbstractInfoAttribute buildBusinessEntity(AttributeType type) {

		InfoTextAttribute attribute = new InfoTextAttribute(Identifier.create(getPK(), getUuid() ), getName());
		attribute.setValue(textValue);
		attribute.setType(type);
		
		super.mapReleance(attribute);
		
		return attribute;
	
	}
	
	
	@Override
	public void updateAdvancedAttributesByBusinessObject(AbstractInfoAttribute e) {
		
		if (e instanceof InfoTextAttribute) {
			
			InfoTextAttribute toUpdate = (InfoTextAttribute) e;
			
			setTextValue((String)toUpdate.getValue());
			
		} else {
			throw new IllegalArgumentException("Wrong Parameter: " +e.toString());
		}
		
		
		
	}
	
	

}


