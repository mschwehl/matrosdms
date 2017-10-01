package net.schwehla.matrosdms.persistenceservice.entity.internal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@NamedQuery(name="DBKategorie.findByUUID", query="SELECT c FROM DBKategorie c where c.uuid = :uuid") 

@Entity
@Table(name = "Kategory")
public class DBKategorie extends AbstractDBInfoBaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="KATEGORY_ID")
	private Long id;
	
	public Long getPK() {
		return id;
	}
	
	@Column(unique=false,nullable=true)
	boolean object;
	
	@ManyToOne
    private DBKategorie parent;
	
    @OneToMany(mappedBy="parent")
    private List <DBKategorie> children;

	
	public boolean isObject() {
		return object;
	}
	public void setObject(boolean object) {
		this.object = object;
	}
	public DBKategorie getParent() {
		return parent;
	}
	public void setParent(DBKategorie parent) {
		this.parent = parent;
	}
	public List<DBKategorie> getChildren() {
		return children;
	}
	public void setChildren(List<DBKategorie> children) {
		this.children = children;
	}
	
}
