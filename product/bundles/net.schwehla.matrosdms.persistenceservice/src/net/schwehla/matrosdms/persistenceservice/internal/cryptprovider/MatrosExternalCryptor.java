package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.internal.StoreResult;
import net.schwehla.matrosdms.persistenceservice.internal.cryptprovider.externalcommand.ExternalCommand;
import net.schwehla.matrosdms.persistenceservice.internal.cryptprovider.externalcommand.ExternalCommandResult;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public class MatrosExternalCryptor extends AbstractMatrosCryptor implements IMatrosStoreCryptor {


	String password;
	String pathToExe;
	String cryptLine;
	String uncryptLine;
	
	public MatrosExternalCryptor(Path localPath) {
		super(localPath);
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPathToExe() {
		return pathToExe;
	}
	public void setPathToExe(String pathToExe) {
		this.pathToExe = pathToExe;
	}
	public String getCryptLine() {
		return cryptLine;
	}
	public void setCryptLine(String cryptLine) {
		this.cryptLine = cryptLine;
	}
	public String getUncryptLine() {
		return uncryptLine;
	}
	public void setUncryptLine(String uncryptLine) {
		this.uncryptLine = uncryptLine;
	}
	
	
	
	@Override
	public StoreResult persist(File droppedFile, Identifier identifier) throws MatrosServiceException {
		
		StoreResult result = new StoreResult();
		
		File finalFileName = buildFinalFilename(identifier);
		File finalFileNameParent = finalFileName.getParentFile();

		if (!finalFileNameParent.exists() && ! finalFileNameParent.mkdirs()) {
			throw new MatrosServiceException("cannot create directory " + finalFileNameParent.getAbsolutePath());
		}
		
	// 	Command pack = new Command( "-mhe=on" , "a", "destination2.7z" ,  "C:\\temp\\mittwoch.jar" ,  "-pSECTRET"  ) {
        ExternalCommand pack = new ExternalCommand( "-mhe=on" , "a" , "-aoa" ,  finalFileName.getName() , "\"" + droppedFile.getAbsolutePath() + "\"",  "-p"  + password  ) {

               @Override
               protected File directory() {
                     return buildFinalFilename(identifier).getParentFile().getAbsoluteFile();
               }

               @Override
               protected String command() {
                     return pathToExe;
               }

        };

       
        
        try {
        	ExternalCommandResult externalResult = pack.execute();

        	
        	if (Objects.isNull(externalResult) || externalResult.code != 0) {
        		throw new MatrosServiceException("Returncode != 0 " + externalResult.output);
        	}
        	
            String sha = getSHA256(finalFileName);
            
            result.setSHA256(sha);
            result.setCryptSettings("7z");

            
		} catch (Exception e) {
			throw new MatrosServiceException(e, "Error packing");
		}
          
	
		return result;
		
		
		
		
	}
	
	
    
    public static <K, V> Map<K, V> createLRUMap(final int maxEntries) {
        return new LinkedHashMap<K, V>(maxEntries*10/7, 0.7f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxEntries;
            }
        };
    }


    
    
	@Override
	public File getDisplayLink(Identifier identifier)   {
		
		File finalFileName = buildFinalFilename(identifier);
		

    //    Command command = new Command( "e", "destination2.7z" , "-aoa" , "-oc:\\temp\\unpacked.vbs" ,  "-pSECTRET"  ) {

   
        ExternalCommand unpack = new ExternalCommand(   "e" , "-aoa" ,  finalFileName.getAbsolutePath() , "-oc:\\temp\\" + identifier.getUuid()  ,  "-p"  + password  ) {

            @Override
            protected File directory() {
                  return new File("c:\\temp");
            }

            @Override
            protected String command() {
                  return pathToExe;
            }

        };

     
        try {
        	
        	
       	System.out.println(unpack);
        	ExternalCommandResult externalResult = unpack.execute();
         	
         	if (Objects.isNull(externalResult) || externalResult.code != 0) {
         		throw new MatrosServiceException("Returncode != 0 " + externalResult.output);
         	}
         	
         	
         	File root = new File("c:\\temp\\" + identifier.getUuid() );
         	
         	File testDirectory = root;
         	File[] files = testDirectory.listFiles();
         	
         	if (files.length == 1) {
         		return files[0];
         	}
         	
         	throw new RuntimeException("Cannot extract: " + identifier);
         	
        } catch(Exception e) {
        	
        	throw new RuntimeException(e);
        }
        	
   
     
	}

	@Override
	public File getStoredElementFile(Identifier identifier) {
		return buildFinalFilename(identifier);
	}
	

	private File buildFinalFilename(Identifier identifier) {
		
		File fItem = new File( cloudRoot.toFile().getAbsolutePath() + File.separator + identifier.getUuid().toLowerCase().substring(0, 2)
				+  File.separator + identifier.getUuid().toLowerCase() + ".7z");
		return fItem;
	}
	
}
