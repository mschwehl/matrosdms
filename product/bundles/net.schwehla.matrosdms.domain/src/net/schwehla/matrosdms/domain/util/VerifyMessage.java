package net.schwehla.matrosdms.domain.util;

public class VerifyMessage {



	public static String FILESIZE_ONLY 	= "FILESIZE_ONLY";
	public static String CONTENT 		= "CONTENT";
	
	String usecase;
	boolean includeArchived;

	public boolean isIncludeArchived() {
		return includeArchived;
	}

	public void setIncludeArchived(boolean includeArchived) {
		this.includeArchived = includeArchived;
	}

	public String getUsecase() {
		return usecase;
	}

	public void setUsecase(String usecase) {
		this.usecase = usecase;
	}

}
