package net.schwehla.matrosdms.rcp.wizzard.model;

public class ExportWizzardData {


	String path;
	String pattern;
	String usecase;
	
	boolean crypted;
	boolean metadata;
	
	public String getUsecase() {
		return usecase;
	}
	public void setUsecase(String usecase) {
		this.usecase = usecase;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public boolean isCrypted() {
		return crypted;
	}
	public void setCrypted(boolean crypted) {
		this.crypted = crypted;
	}
	public boolean isMetadata() {
		return metadata;
	}
	public void setMetadata(boolean metadata) {
		this.metadata = metadata;
	}
	
}
