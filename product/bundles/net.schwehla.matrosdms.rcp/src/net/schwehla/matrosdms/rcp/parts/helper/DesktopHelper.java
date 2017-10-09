package net.schwehla.matrosdms.rcp.parts.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
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
    
    */
    
    
	@SuppressWarnings("restriction")
	@Inject Logger logger;

	// XXX
	File localAppcache = new File("c:\\temp\\appcache");
	
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
    

    

    // evtl. cachen mit weakhashmap 10 elemente
	public  String getLocallink(InfoItem doc) throws MatrosServiceException {

		
	      // XXX
	      try {
	    	  
	    	  return service.getDisplayLink(doc.getIdentifier()).getAbsolutePath();
	    	  
		      
	      } catch (Exception ex) {
	    	  throw new MatrosServiceException("cannot stream file to temp-folder: " + ex);
	      }
		
		}
	
	
    
	public void openInboxFileAsClone(File file)   {
		

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
			
			localAppcache.mkdirs();
			
			 File f = new File(localAppcache,file.getName() + System.currentTimeMillis() + extension);
					 f.deleteOnExit();
					 
					 Files.copy(file.toPath(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);

			openUrl(f.getAbsolutePath());
			
		} catch (Exception e) {
			logger.error(e, "cannot open file: " + file.getName());
		}

		
		

        
	}

	
	
}
