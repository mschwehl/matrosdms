package net.schwehla.matrosdms.persistenceservice.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.osgi.service.datalocation.Location;

import net.schwehla.matrosdms.rcp.MyGlobalConstants;

/**
 * Reads the Connection-Information
 * @author Martin
 */


public class MatrosConfigReader {
	
	private static String folder = "matrosconfig";
	
	private static String serverconfig = "serverconfig.properties";	 //$NON-NLS-1$
		
	public String getInstallHomeMessage() {
		
		try {
			
			String installdir = getInstallDir();
			
			if ( installdir != null) {
				return installdir;
			} 
			
		} catch (Exception e) {
			// Intensionally blank
		}
		
		return "HOME cannot be resolved";
		
	}
	
	
	

	
	public void setConfigIniFileProperties(String path, String user, String pw) throws Exception  {
		
		 Properties prefs = getProperties();

		 prefs.put(MyGlobalConstants.ConfigIniFile.APPLICATION_FOLDER, path);
		 prefs.put(MyGlobalConstants.ConfigIniFile.DB_USERNAME, user);
		 prefs.put(MyGlobalConstants.ConfigIniFile.DB_PASSWORD, pw);
		 
		 savePropertyFile(prefs);
		 
	}
	
	
	public String getApplicationCacheDir() throws Exception  {
		
		 Properties prefs = getProperties();
		 
		 String path = prefs.getProperty(MyGlobalConstants.ConfigIniFile.APPLICATION_FOLDER). toString();
		 
		 File f = new File(path + File.separator + "cache");
		 
		 if (! f.exists() && f.isDirectory()) {
			 throw new IllegalStateException("Cannot find cachedir");
			 
		 }
		 		 
		 return f.getCanonicalPath();
		 
	}
	
	
	public String getDBPath() throws Exception  {
		
		 Properties prefs = getProperties();
		 
		 String path = prefs.getProperty(MyGlobalConstants.ConfigIniFile.APPLICATION_FOLDER). toString();
		 
		 return path + File.separator + "db";
		 
	}
	
	
	public String getDBUser() throws Exception  {
		
		 Properties prefs = getProperties();
		 return prefs.getProperty(MyGlobalConstants.ConfigIniFile.DB_USERNAME).toString();
	}
	
	public String getDBPassword() throws Exception  {
		
		 Properties prefs = getProperties();
		 return prefs.getProperty(MyGlobalConstants.ConfigIniFile.DB_PASSWORD).toString();
	}
	
	
	private void savePropertyFile(Properties props) throws Exception {

		 try (FileOutputStream fis = new FileOutputStream(getFile())){
			props.store(fis, "changedate "+  new Date());
	     } catch (Exception e) {
	        throw e;
	     };
	     
		
	}

	private Properties getProperties() throws Exception {
		
		
			 Properties props = new Properties();
			    
			 try (InputStream fis = new FileInputStream(getFile())){

				props.load(fis);
				    
				      
		     } catch (Exception e) {
		        throw e;
		     };
			 
		     return props;
			 
			
	}
	
	private File getFile() throws Exception {
		
		String installDir = getInstallDir();
		
		 File clientconfig =new File(installDir + File.separator + folder + File.separator + serverconfig);
		 
		 if (!clientconfig.exists()) {
			 clientconfig.getParentFile().mkdirs();
			 clientconfig.createNewFile();
		 }
		 
		 return clientconfig;
		
	}
	
	private String getInstallDir(){
		  Location installLocation=Platform.getInstallLocation();
		  String installDir=null;
		  if (installLocation != null) {
		    URL installURL=installLocation.getURL();
		    if (installURL != null) {
		      installDir=installURL.getFile();
		    }
		  }
		  return installDir;
		}


}
