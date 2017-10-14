package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;

import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.internal.StoreResult;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public class MatrosNoCryptor extends AbstractMatrosCryptor implements IMatrosStoreCryptor {

	public MatrosNoCryptor(Path localPath) {
		super(localPath);
	}

	@Override
	public StoreResult persist(File droppedFile, Identifier identifier) throws MatrosServiceException {

		
		StoreResult result = new StoreResult();
		
		File finalFileName = buildFinalStoredFilename(identifier);
		File finalFileNameParent = finalFileName.getParentFile();

		if (!finalFileNameParent.exists() && ! finalFileNameParent.mkdirs()) {
			throw new MatrosServiceException("cannot create directory " + finalFileNameParent.getAbsolutePath());
		}
      
        try {
        	
            String sha = getSHA256(finalFileName);
            result.setSHA256(sha);
     
            
		} catch (Exception e) {
			throw new MatrosServiceException(e, "Error packing: " + e);
		}
          
	
		return result;
		
		
		
		
	
	}


	@Override
	public File getStoredElementFile(Identifier identifier) {
		return buildFinalStoredFilename(identifier);
	}



	
	private File buildFinalStoredFilename(Identifier identifier) {
		
		File fItem = new File( cloudRoot.toFile().getAbsolutePath() + File.separator + identifier.getUuid().toLowerCase().substring(0, 2)
				+  File.separator + identifier.getUuid().toLowerCase() );
		return fItem;
	}

	@Override
	public FileInputStream getStreamedContent(Identifier identifier) throws MatrosServiceException {
		throw new RuntimeException("not implemented");
	}
	
	
}
