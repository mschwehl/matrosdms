package net.schwehla.matrosdms.domain.admin;

import net.schwehla.matrosdms.domain.api.IComplete;

public class CryptSettings implements IComplete {
			
	E_CLOUDCRYPTION cryption = E_CLOUDCRYPTION.NONE ;

	String password;
	String exePath;
	String exeEncryptline;
	String exeDecryptline;
	
	
	public String getExePath() {
		return exePath;
	}

	public void setExePath(String exePath) {
		this.exePath = exePath;
	}

	public String getExeEncryptline() {
		return exeEncryptline;
	}

	public void setExeEncryptline(String exeEncryptline) {
		this.exeEncryptline = exeEncryptline;
	}

	public String getExeDecryptline() {
		return exeDecryptline;
	}

	public void setExeDecryptline(String exeDecryptline) {
		this.exeDecryptline = exeDecryptline;
	}

	public boolean isCrypted() {
		return password != null;
	}

	public void setCryption(E_CLOUDCRYPTION crypted) {
		this.cryption = crypted;
	}

	public String getPassword() {
		return password;
	}

	public E_CLOUDCRYPTION getCryption() {
		return cryption;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public boolean isComplete() {
		return password != null;
	}

	
	


}