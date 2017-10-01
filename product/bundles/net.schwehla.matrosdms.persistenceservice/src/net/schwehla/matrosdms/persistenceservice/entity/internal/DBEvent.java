package net.schwehla.matrosdms.persistenceservice.entity.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Event")

@NamedQueries ( {
@NamedQuery(name="DBEvent.findByID", query="SELECT c FROM DBEvent c where c.id = :id "),
@NamedQuery(name="DBEvent.findAll", query="SELECT c FROM DBEvent c"),


@NamedQuery(name="DBEvent.findByUUID", query="SELECT c FROM DBEvent c where c.uuid = :uuid") })


public class DBEvent extends AbstractDBInfoBaseEntity  {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="EVENT_ID")
	private Long id;
	
	// important else parent is given
	public Long getPK() {
		return this.id;
	}

	
	@JoinColumn(name="ITEM_ID" ,nullable = false)
	private DBItem item;


	@Temporal(TemporalType.TIMESTAMP)
	Date dateScheduled;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date dateCompleted;

	
	String actionscript;

	
	
}
