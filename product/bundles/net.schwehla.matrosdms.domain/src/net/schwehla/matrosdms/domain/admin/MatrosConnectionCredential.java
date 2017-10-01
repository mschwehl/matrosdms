package net.schwehla.matrosdms.domain.admin;

public class MatrosConnectionCredential {

	public String getDbPath() {
		return dbPath;
	}
	String dbUser;
	String dbPasswd;
	String dbPath;
	
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPasswd() {
		return dbPasswd;
	}
	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	public boolean isSet() {
		return dbUser != null && dbPasswd != null ;
	}
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}
}
