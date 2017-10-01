package net.schwehla.matrosdms.persistenceservice.entity.internal.management;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.schwehla.matrosdms.persistenceservice.entity.internal.AbstractDBInfoBaseEntity;

@Entity
@Table(name="User")

@NamedQueries ( {
@NamedQuery(name="DBUser.findAll", query="SELECT c FROM DBUser c"),
@NamedQuery(name="DBUser.findByUUID", query="SELECT c FROM DBUser c where c.uuid = :uuid") ,
@NamedQuery(name="DBUser.findByNameAndPasswordhash", query="SELECT c FROM DBUser c where c.name = :name and c.passwordhash = :passwordhash") })


public class DBUser extends AbstractDBInfoBaseEntity  {
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="USER_ID")
	private Long id;
	
	public Long getPK() {
		return id;
	}
	
	
	public String passwordhash;
	
	public String email;
	
	
	public String getPasswordhash() {
		return passwordhash;
	}


	public void setPasswordhash(String passwordhash) {
		this.passwordhash = passwordhash;
	}

	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	List <DBPermission> permissionList = new ArrayList<DBPermission>();
	
	
}
