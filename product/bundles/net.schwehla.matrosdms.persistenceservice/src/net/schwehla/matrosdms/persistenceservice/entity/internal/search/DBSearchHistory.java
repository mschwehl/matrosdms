package net.schwehla.matrosdms.persistenceservice.entity.internal.search;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import net.schwehla.matrosdms.persistenceservice.entity.internal.management.DBUser;

@Entity
@Table(name="Serachhistory")


public class DBSearchHistory {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="SEARCH_ID")
	private Long id;
	
	// important else parent is given
	public Long getPK() {
		return this.id;
	}
	

	@JoinColumn(name="USER_ID" ,nullable = false)
	private DBUser user;

	@Column
	String kategory;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@Column
	private String queryString;

	
}
