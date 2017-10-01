package net.schwehla.matrosdms.persistenceservice.entity.internal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="VW_CONTEXT")

@NamedQueries ({
@NamedQuery(name="VW_CONTEXT.findAll", query="SELECT c FROM VW_CONTEXT c") ,
@NamedQuery(name="VW_CONTEXT.findAllNotArchived", query="SELECT c FROM VW_CONTEXT c where c.dateArchived is null") ,
@NamedQuery(name="VW_CONTEXT.findByUUID", query="SELECT c FROM VW_CONTEXT c where c.uuid = :uuid") 
})


public class VW_CONTEXT extends AbstractDBInfoBaseEntity {
	
	
	@Column
	int sum;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO )
	@Column(unique=true,nullable=false, name="CONTEXT_ID")
	private Long id;
	

	// need to copy from dbcontext
	
	@ManyToMany
	@JoinTable(
	      name="Context_Kategorie",
	      joinColumns=@JoinColumn(name="CONTEXT_ID", referencedColumnName="CONTEXT_ID"),
	      inverseJoinColumns=@JoinColumn(name="KATEGORY_ID", referencedColumnName="KATEGORY_ID"))
	  private List<DBKategorie> kategorieList = new ArrayList<>();

	
	  
	public List<DBKategorie> getKategorieList() {
		return kategorieList;
	}

	public int getSum() {
		return sum;
	}


	public String getUuid() {
		return uuid;
	}


	public String getName() {
		return name;
	}


	public String getIcon() {
		return icon;
	}

	public Long getPK() {
		return id;
	}


}
