package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider;

import java.io.File;
import java.nio.file.Path;

import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.internal.StoreResult;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public interface IMatrosStoreCryptor {

	public StoreResult persist(File droppedFile, Identifier identifier)  throws MatrosServiceException  ;
	
	public File getDisplayLink(Identifier identifier) throws MatrosServiceException;
	
	public Path getCloudRoot();
	public File getStoredElementFile(Identifier identifier);

}
