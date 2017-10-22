package net.schwehla.matrosdms.rcp.parts.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.persistenceservice.internal.MatrosConfigReader;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;

@Creatable
public class DesktopHelper  {
	
	@Inject
	@Preference(nodePath = MyGlobalConstants.Preferences.NODE_COM_MATROSDMS) 
	IEclipsePreferences preferences ;
    
    
	@SuppressWarnings("restriction")
	@Inject Logger logger;

	// XXX
	MatrosConfigReader configReader = new MatrosConfigReader();
	
	@Inject 
	IMatrosServiceService service;
	
    // http://www.java-forums.org/new-java/69787-cant-open-shared-server-folder-spaces-name.html
    public  void openUrl(String path) {
		
		if (Desktop.isDesktopSupported()) {
		    try {
		      	
		    	// Window only 
		        File myFile = new File( path );
		        
		        String url = myFile.toURI().toURL().toExternalForm();
		        url = url.replace(" ", "%20");  //$NON-NLS-1$//$NON-NLS-2$
		        
		        URI u = new URI(url);
		        
		         Desktop desktop = Desktop.getDesktop();
		         desktop.browse(u);
		          
		    } catch (Exception ex) {
		    	logger.error(ex, "cannot open file: " + path);
		    }
		}
		
	}
    

	public  String getLocallink(InfoItem doc) throws MatrosServiceException {

		
	      // XXX
	      try  (FileInputStream fos = service.getStreamedContent(doc.getIdentifier() )) {
	    	  
	    		File tempFoder = new File (configReader.getApplicationCacheDir() + File.separator + "docs");
				tempFoder.mkdirs();
	  
				String filename = doc.getMetadata().getFilename();
				
				File total = new File(tempFoder.getAbsolutePath() + File.separator + filename);
				
				if (!total.exists()) {
					
					Files.copy(fos,  total.toPath(), StandardCopyOption.REPLACE_EXISTING);
					total.deleteOnExit();
			
				}
		      
				return total.getAbsolutePath();
				
	      } catch (Exception ex) {
	    	  throw new MatrosServiceException("cannot stream file to temp-folder: " + ex);
	      }
		
		}
	
	
    
	public String getInboxNonBlockingLink(File file) throws Exception   {
		

		String extension = getExtension(file.getAbsolutePath());

		
		
		 // creates temporary file

		try {
			
			File tempFoder = new File (configReader.getApplicationCacheDir() + File.separator + "docs");
			tempFoder.mkdirs();
			
			String filename = file.lastModified() + "_" +file.hashCode() + "_" +  file.getName();
			
			File total = new File(tempFoder.getAbsolutePath() + File.separator + filename);
			
			if (!total.exists()) {
				Files.copy(file.toPath(), total.toPath(), StandardCopyOption.REPLACE_EXISTING);
				total.deleteOnExit();
			}
			

			return total.getAbsolutePath();
			
		} catch (Exception e) {
			logger.error(e, "cannot open file: " + file.getName());
			throw e;
		}

		
		

        
	}

	public static String getExtension(String filename) {
					
			String extension = "";
			String path = filename;
			
			int lastSeparator =  path.lastIndexOf(File.separator);
			
			if (lastSeparator > 0) {
				path = filename.substring(lastSeparator+1);
			}

			int i = path.lastIndexOf('.');
			if (i > 0) {
			    extension = path.substring(i+1);
			}
			
			if (extension.length() > 0) {
				extension = "." + extension;
			}
			
			return extension;
		}


	public void moveFromInbox(File file) throws MatrosServiceException {

		try {
			String inboxNotProcessed =  preferences.get(MyGlobalConstants.Preferences.NOT_PROCESSED_PATH, "" );
			inboxNotProcessed = inboxNotProcessed.replaceAll(";","");
			
			if (inboxNotProcessed == null || inboxNotProcessed.trim().length() == 0) {
				throw new MatrosServiceException("no processed folder specified, please go to the preferences");
			}
			
			
			File targetProcessed = new File(inboxNotProcessed);
				
				if(! targetProcessed.exists()) {
					
					boolean create = targetProcessed.mkdirs();
					
					if (!create) {
						throw new MatrosServiceException("cannot create processed folder");
					}

				}
				
			if(!file.exists()) {
				throw new MatrosServiceException("Source file not exists anymore");
			}
			
			Path moveSourcePath = file.toPath() ;
			
			File target = new File( inboxNotProcessed + File.separator + file.getName() ) ;
			
			if (target.exists()) {
				target = new File(inboxNotProcessed + File.separator + System.currentTimeMillis() + "_" + file.getName() );
			}
			
			Path moveTargetPath = target.toPath();
			moveTargetPath.getParent().toFile().mkdirs();
			
			Files.move( moveSourcePath, moveTargetPath );
			
			if(! moveTargetPath.toFile().exists()) {
				throw new MatrosServiceException("targetfile not created " +  moveTargetPath.getFileName());
			}
			
			if( moveSourcePath.toFile().exists()) {
				throw new MatrosServiceException("sourcefile not moved " +  moveSourcePath.getFileName());
			}
			


			
		} catch (MatrosServiceException e1) {
			throw e1;
		} catch (Exception e) {
			throw new MatrosServiceException("Error moving file" + e);
		}
		
		
	}

	
}
