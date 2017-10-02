package net.schwehla.matrosdms.domain.admin;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.util.Identifier;


public class MatrosUser extends InfoBaseElement {
	
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

	public String firstname;
	public String secondname;
	public String password;
	public String email;
	
	
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
