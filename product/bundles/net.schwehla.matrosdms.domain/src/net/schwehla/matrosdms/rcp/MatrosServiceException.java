package net.schwehla.matrosdms.rcp;

public class MatrosServiceException extends Exception {


	public MatrosServiceException(Exception e,  String text) {
		super(text,e);
	}

	
	public MatrosServiceException(String text) {
		super(text);
	}

}
