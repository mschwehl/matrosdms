package net.schwehla.matrosdms.rcp;

public class MatrosServiceException extends Exception {


	public MatrosServiceException( String text, Exception e ) {
		super(text,e);
	}

	
	public MatrosServiceException(String text) {
		super(text);
	}

}
