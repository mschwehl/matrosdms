package net.schwehla.matrosdms.domain.util;

public class VerifyItem  {
	
	String uuid;
	String name;

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	boolean fondInStorage;
	boolean fondInDatabase;
	
	String errorText;
	
	
	public String getErrorText() {
		return errorText;
	}



	
	
	public boolean isFondInStorage() {
		return fondInStorage;
	}
	public void setFondInStorage(boolean fondInStorage) {
		this.fondInStorage = fondInStorage;
	}
	public boolean isFondInDatabase() {
		return fondInDatabase;
	}
	public void setFoundInDatabase(boolean fondInDatabase) {
		this.fondInDatabase = fondInDatabase;
	}


	public void setErrotext(String errorText) {
		this.errorText = errorText;
		
	}

	@Override
	public String toString() {
		return "" + name + "->" + uuid;
	}

	
}
