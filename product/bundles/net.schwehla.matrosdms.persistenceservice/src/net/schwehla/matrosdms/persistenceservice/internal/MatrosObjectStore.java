package net.schwehla.matrosdms.persistenceservice.internal;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.internal.cryptprovider.IMatrosStoreCryptor;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

class MatrosObjectStore {
	
	private Logger logger = Logger.getLogger(MatrosObjectStore.class.getName());

	public IMatrosStoreCryptor storeCryptor;
	
	MatrosObjectStore(IMatrosStoreCryptor storeCryptor) {
		
		this.storeCryptor = storeCryptor;
		
		if (! storeCryptor.getCloudRoot().toFile().exists()) {
			throw new IllegalArgumentException("Path to Cloudroot not exists: " + storeCryptor.getCloudRoot());
		}
		
	}


	public StoreResult persist(File droppedFile, Identifier identifier) throws MatrosServiceException {
		File rootDir = storeCryptor.getCloudRoot().toFile();

	
		try {
			return storeCryptor.persist(droppedFile, identifier);
		} catch (Exception e) {
			 throw new MatrosServiceException(e.getMessage());
		}
			

		
	}



	public File  getDisplayLink(Identifier uuid) throws MatrosServiceException {
	
		File rootDir = storeCryptor.getCloudRoot().toFile();
		
		if (!rootDir.exists()) {
			logger.info("Rootdir not exists " + rootDir);
		}
		
		try {
			return storeCryptor.getDisplayLink(uuid);
		} catch (Exception e) {
			 throw new MatrosServiceException(e.getMessage());
		}
		
		
	}

	
	public boolean exists(Identifier uuid) {
		
		File fItem = storeCryptor.getStoredElementFile(uuid);
		return fItem.exists();
	}
	
	
	List <Path> getAllFiles() throws MatrosServiceException {
		
		File rootDir = storeCryptor.getCloudRoot().toFile();
		
		try {
			
			List <Path > files = 
					Files.find(Paths.get(rootDir.getAbsolutePath()),
					           Integer.MAX_VALUE,
					           (filePath, fileAttr) -> fileAttr.isRegularFile()).collect(Collectors.toList());
					

			
			return files;
			
			
			
		} catch(Exception e) {
			throw new MatrosServiceException(e, "Cannot retrieve all files");
		}
		

		
	}
	
	public boolean deleteFileIfExists(Identifier identifier) {
		
		File fItem = storeCryptor.getStoredElementFile(identifier);

		if (fItem.exists()) {
			return fItem.delete();
		}
			
		return false;
		
	}



}
