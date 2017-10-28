package net.schwehla.matrosdms.domain.core;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoBaseElementWithOrdinal extends InfoBaseElement {

	int ordinal;
	
	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int oridinal) {
		this.ordinal = oridinal;
	}

	private static final long serialVersionUID = 1L;

	protected InfoBaseElementWithOrdinal() {

	}
	
	
	public InfoBaseElementWithOrdinal(Identifier identifier, String name) {
		super(identifier,name);
	}

	public InfoBaseElementWithOrdinal(Identifier identifier, InfoBaseElement element) {
		super(identifier,element);
		
	}
	  
	  
	


	
	public void applyValues(InfoBaseElementWithOrdinal other) {
		
		Objects.requireNonNull(other, "Called with null argument");
		super.applyValues(other);
		this.ordinal = other.ordinal;
		
	}
	
	
}
