package net.schwehla.matrosdms.rcp.parts.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.persistenceservice.internal.MatrosConfigReader;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

@Creatable
public class DesktopHelper  {
	
	/*	
	
	// let's save the last 50 file-calls for this session
    Map <String,File> tempFileCache = createLRUMap(50);
    
    
    @PreDestroy
    void clear() {
    	
    	if (tempFileCache != null) {
        	tempFileCache.clear();
        	tempFileCache = null;
    	}

    }
    
    public static <K, V> Map<K, V> createLRUMap(final int maxEntries) {
        return new LinkedHashMap<K, V>(maxEntries*10/7, 0.7f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxEntries;
            }
        };
    }

    try {
		if (tempFileCache.containsKey(doc.getIdentifier().getUuid())) {
			
			if (tempFileCache.get(doc.getIdentifier()).exists() ) {
				return tempFileCache.get(doc.getIdentifier().getUuid()).getCanonicalPath();
			}
		} 
	} catch(Exception e) {
		
	}
    
	}
    
*/
    
    
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
		

		String extension = "";
		String path = file.getAbsolutePath();
		
		int lastSeparator =  path.lastIndexOf(File.separator);
		
		if (lastSeparator > 0) {
			path = file.getAbsolutePath().substring(lastSeparator+1);
		}

		int i = path.lastIndexOf('.');
		if (i > 0) {
		    extension = path.substring(i+1);
		}
		
		if (extension.length() > 0) {
			extension = "." + extension;
		}
		
		
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

	
	
}
