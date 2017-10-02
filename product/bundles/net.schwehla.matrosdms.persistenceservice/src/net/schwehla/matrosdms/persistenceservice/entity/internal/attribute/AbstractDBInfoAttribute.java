package net.schwehla.matrosdms.persistenceservice.entity.internal.attribute;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.persistenceservice.entity.internal.AbstractDBInfoBaseEntity;
import net.schwehla.matrosdms.persistenceservice.entity.internal.DBItem;

/**
 * @author Martin
 *
 */

@Entity
@Inheritance
@DiscriminatorColumn(name="ATTR_SUBTPYE")
@Table(name="Attribute")

@NamedQuery(name="AbstractDBInfoAttribute.findByPK", query="SELECT c FROM AbstractDBInfoAttribute c where c.id = :id") 

public abstract class AbstractDBInfoAttribute  extends AbstractDBInfoBaseEntity {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="ATTRIBUTE_ID")
	private Long id;
	
	
	public Long getPK() {
		return id;
	}
	
	
	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	@JoinColumn(name="ITEM_ID" ,nullable = false)
	private DBItem item;
	
	public DBItem getItem() {
		return item;
	}

	public void setItem(DBItem item) {
		this.item = item;
	}

	DBAttributeType attributeType;
	
	@Column(unique=false,nullable=true,updatable=true)
	Date relevancefrom;
	
	@Column(unique=false,nullable=true,updatable=true)
	Date relevanceto;

	public Date getRelevancefrom() {
		return relevancefrom;
	}

	public void setRelevancefrom(Date relevancefrom) {
		this.relevancefrom = relevancefrom;
	}

	public Date getRelevanceto() {
		return relevanceto;
	}

	public void setRelevanceto(Date relevanceto) {
		this.relevanceto = relevanceto;
	}

	public DBAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(DBAttributeType attributeType) {
		this.attributeType = attributeType;
	}
	
	
	public abstract AbstractInfoAttribute buildBusinessEntity(AttributeType type);

	public abstract void updateAdvancedAttributesByBusinessObject(AbstractInfoAttribute e) ;

	public void mapReleance(AbstractInfoAttribute b) {
		
		b.getFixcalendar().setRelevancefrom(relevancefrom );
		b.getFixcalendar().setRelevanceto(relevanceto );
		
	}
}
