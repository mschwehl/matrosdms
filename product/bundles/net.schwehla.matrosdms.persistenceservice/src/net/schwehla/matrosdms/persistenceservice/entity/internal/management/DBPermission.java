package net.schwehla.matrosdms.persistenceservice.entity.internal.management;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Permission")
public class DBPermission   {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="PERMISSION_ID")
	private Long id;
	
	private String name;
	
	private String key;
	
	
	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	@JoinColumn(name="USER_ID" ,nullable = false)
	private DBUser user;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public DBUser getUser() {
		return user;
	}


	public void setUser(DBUser user) {
		this.user = user;
	}


	public Long gePermissionId() {
		return id;
	}
	
	
}
