package net.schwehla.matrosdms.domain.core;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class Identifier implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	
	Long pk;
	String uuid;

	@XmlTransient
	private static final Identifier NONE = new Identifier(0L);
	
	public static Identifier createNEW() {
		Identifier temp = new Identifier(NONE.getPk(), UUID.randomUUID().toString().toLowerCase());
		return temp;
	}
	
	public static Identifier create(Long pk, String uuid) {
		Identifier temp = new Identifier(pk, uuid);
		return temp;
	}
	


	
	private Identifier(Long pk) {
		
		
		if (pk == null) {
			throw new IllegalStateException("pk is null");
		}
		
	 this.pk = pk;
	}
	
	private Identifier(Long pk, String uuid) {
		
		this(pk);
		
		if (uuid == null) {
			throw new IllegalStateException("UUID is null");
		}
		
		 this.uuid = uuid;
	}


	@Override
	public boolean equals(Object other) {
		
		
		if (other instanceof Identifier && this.uuid != null) {
			return this.uuid.equals( ((Identifier)other).getUuid() );
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	public Long getPk() {
		return pk;
	}

	public String getUuid() {
		return uuid;
	}
	
	@Override
	public String toString() {
		return uuid.toLowerCase();
	}

	public Object toDebug() {
		return "" + getClass().getName() + ":" + getUuid();
	}
	
	public void lastTest() {
		uuid = UUID.randomUUID().toString();
		pk = (long) (Math.random() * Long.MAX_VALUE);
	}
	
}
