package net.schwehla.matrosdms.rcp;

import net.schwehla.matrosdms.domain.core.Identifier;

public interface MyGlobalConstants {


	
	public static final Identifier ROOT_WER = Identifier.create(0L,"ROOT_WER"); 
	public static final Identifier ROOT_WAS = Identifier.create(-1L,"ROOT_WAS"); 
	public static final Identifier ROOT_WO =  Identifier.create(-2L,"ROOT_WO"); 
	public static final Identifier ROOT_ART = Identifier.create(-3L,"ROOT_ART"); 
	

	// Livecycle-manager removes all parts on close
	public static final String TAG_REMOVEONCLOSE = "removeonclose";


	public static final String SELECTED_ROOT = "SELECTED_ROOT";


	
	// with java8 interfaces are fine
	public interface Preferences {
		
		public static final String DELIMITER = ";";		 		//$NON-NLS-1$
		public static final String INBOX_PATH = "INBOX_PATH"; 	//$NON-NLS-1$
		
		public static final String NODE_COM_MATROSDMS = "com.matrosdms";


		
		public static final String EXPORT_PATH = "EXPORT_PAHT"; 	//$NON-NLS-1$
		public static final String EXPORT_PATTERN = "EXPORT_PATTERN"; 	//$NON-NLS-1$
		
		
		public static final String LAST_LOGIN = "LAST_LOGIN"; 	//$NON-NLS-1$
		public static final String LAST_LOGIN_CHECKBOX = "LAST_LOGIN_CHECKBOX"; 	//$NON-NLS-1$
		
		public static final String PROCESSED_PATH = "PROCESSED_PATH"; //$NON-NLS-1$
		public static final String NOT_PROCESSED_PATH = "NOT_PROCESSED_PATH"; //$NON-NLS-1$
	    
		
	}
	
	// with java8 interfaces are fine
	public interface ConfigIniFile {
	
		public static final String APPLICATION_FOLDER = "APPLICATION_FOLDER"; //$NON-NLS-1$
		
		public static final String DB_USERNAME = "DB_USERNAME"; //$NON-NLS-1$
		public static final String DB_PASSWORD 	= "DB_PASSWORD"; //$NON-NLS-1$

		
	}
	
	
	
	public interface Configuration {
		
		public static final String MATROSCOUD_ROOTATH = "MATROSCOUD_ROOTATH";  //$NON-NLS-1$
		public static final String CONFIG_OBJECTSTORE_CRYPTED_PASSWORD = "CONFIG_OBJECTSTORE_CRYPTED_PASSWORD";  //$NON-NLS-1$
		public static final String CONFIG_OBJECTSTORE_CRYPTED_TYPE = "CONFIG_OBJECTSTORE_CRYPTED_TYPE";  //$NON-NLS-1$
		public static final String CONFIG_OBJECTSTORE_CRYPTED_EXEPATH = "CONFIG_OBJECTSTORE_CRYPTED_EXEPATH";
		public static final String CONFIG_OBJECTSTORE_CRYPTED_ENCRYPTLINE = "CONFIG_OBJECTSTORE_CRYPTED_ENCRYPTLINE";
		public static final String CONFIG_OBJECTSTORE_CRYPTED_DECRYPTLINE = "CONFIG_OBJECTSTORE_CRYPTED_DECRYPTLINE";
		
	}
			
	

	
}
