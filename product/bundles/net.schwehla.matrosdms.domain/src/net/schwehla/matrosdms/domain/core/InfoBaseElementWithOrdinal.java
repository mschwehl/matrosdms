package net.schwehla.matrosdms.domain.core;

import java.util.Objects;

import net.schwehla.matrosdms.domain.util.Identifier;

public class InfoBaseElementWithOrdinal extends InfoBaseElement {

	int oridinal;
	
	public int getOridinal() {
		return oridinal;
	}


	public void setOridinal(int oridinal) {
		this.oridinal = oridinal;
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
		this.oridinal = other.oridinal;
		
	}
	
	
}
