package net.schwehla.matrosdms.domain.core.idm;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.FIELD) 
public class MatrosUser extends InfoBaseElement {

	private static final long serialVersionUID = 1L;

	public String firstname;
	public String secondname;
	public String email;

	@XmlTransient
	public String password;
	
	public MatrosUser(Identifier key, String name) {
		super(key, name);
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPassword() {
		return password;
	}


	
	public String getSecondname() {
		return secondname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}


	public void setPassword(String passwordhash) {
		this.password = passwordhash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void applyValues(MatrosUser other) {
		super.applyValues(other);

		setFirstname(other.getFirstname());
		setSecondname(other.getSecondname());
		setPassword(other.getPassword());
		setEmail(other.getEmail());
	}

}
