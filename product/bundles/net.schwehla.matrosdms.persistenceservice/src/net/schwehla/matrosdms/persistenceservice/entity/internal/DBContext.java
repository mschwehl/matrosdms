package net.schwehla.matrosdms.persistenceservice.entity.internal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="Context")

@NamedQueries ( {
@NamedQuery(name="DBContext.findAll", query="SELECT c FROM DBContext c"),
@NamedQuery(name="DBContext.findByUUID", query="SELECT c FROM DBContext c where c.uuid = :uuid") })

/**
 * a virtual folder 
 * @author Martin
 *
 */
public class DBContext extends AbstractDBInfoBaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="CONTEXT_ID")
	protected Long id;
	
	// important else parent is given
	public Long getPK() {
		return id;
	}
	

	@OneToMany(mappedBy="infoContext", fetch=FetchType.EAGER)
	List <DBItem> itemList = new ArrayList<>();
	
	
	  @ManyToMany
	  @JoinTable(
	      name="Context_Kategorie",
	      joinColumns=@JoinColumn(name="CONTEXT_ID", referencedColumnName="CONTEXT_ID"),
	      inverseJoinColumns=@JoinColumn(name="KATEGORY_ID", referencedColumnName="KATEGORY_ID"))
	  private List<DBKategorie> kategorieList = new ArrayList<>();



	public List<DBKategorie> getKategorieList() {
		return kategorieList;
	}

	public void setKategorieList(List<DBKategorie> kategorieList) {
		this.kategorieList = kategorieList;
	}



	public List<DBItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<DBItem> itemList	) {
		this.itemList = itemList;
	}


	
}
