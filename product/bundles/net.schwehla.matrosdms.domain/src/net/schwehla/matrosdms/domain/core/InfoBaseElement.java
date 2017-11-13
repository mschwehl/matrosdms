package net.schwehla.matrosdms.domain.core;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.schwehla.matrosdms.adapter.DateAdapter;
import net.schwehla.matrosdms.domain.api.IIdentifiable;
import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoBaseElement implements IIdentifiable, Serializable,Comparable< InfoBaseElement> {

	protected Identifier identifier;
	
	private static final long serialVersionUID = 1L;

	protected InfoBaseElement() {
		throw new IllegalStateException("Called with no parameter");
	}
	

	protected String name;

	protected String icon;
	protected String description;
	
	@XmlJavaTypeAdapter(value = DateAdapter.class)
	protected Date dateCreated;
	
	@XmlJavaTypeAdapter(value = DateAdapter.class)
	protected Date dateArchived;
	
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}



	
	public String getDescription() {
		return description;
	}

	public Date getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public InfoBaseElement(Identifier identifier, String name) {

		if (identifier == null) {
			throw new RuntimeException("Parameter identifier empty");
		}
		
		if (name == null) {
			throw new RuntimeException("Parameter name empty");
		}
		
		this.identifier = identifier;
		this.name = name;

	}

	public InfoBaseElement(Identifier identifier, InfoBaseElement element) {

		if (identifier == null) {
			throw new RuntimeException("Parameter identifier empty");
		}
		
		if (element == null) {
			throw new RuntimeException("Parameter identifier empty");
		}
		
		this.identifier = identifier;
		this.name = element.getName();
		this.icon = element.getIcon();
		this.description = element.getDescription();
		
	}
	  
	  
	  
	  
	@Override
	public java.lang.String toString() {
		return identifier + "|" + name;
	}
	
//	http://eclipsesource.com/blogs/2012/09/04/the-3-things-you-should-know-about-hashcode/
//  both must be overridden

	@Override
	public int hashCode() {

		if (identifier == null) {
			return super.hashCode();
		}

		return identifier.hashCode();
	}
	
	
	

	@Override
	public boolean equals(Object other) {

		try {
			if (other instanceof IIdentifiable) {
				return identifier.equals(((IIdentifiable) other).getIdentifier());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return false;
	}

	@Override
	public String getName() {
		return name;
	}




	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Identifier getIdentifier() {
		return identifier;
	}

	
	public void applyValues(InfoBaseElement other) {
		
		Objects.requireNonNull(other, "Called with null argument");
		
		setName(other.getName());
		setDescription(other.getDescription());
		setIcon(other.getIcon());
		setDateArchived(other.getDateArchived());
		setDateCreated(other.getDateCreated());
		
	}

	@Override
	public int compareTo(InfoBaseElement o) {
		if (o == null) {
			return 1;
		}
		return - o.getIdentifier().getPk().compareTo(getIdentifier().getPk());
	}
	
	
}
