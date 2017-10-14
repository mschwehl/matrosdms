package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;

import net.schwehla.matrosdms.domain.util.Identifier;
import net.schwehla.matrosdms.persistenceservice.internal.StoreResult;
import net.schwehla.matrosdms.rcp.MatrosServiceException;

public interface IMatrosStoreCryptor {

	public StoreResult persist(File droppedFile, Identifier identifier)  throws MatrosServiceException  ;
	public Path getCloudRoot();
	public File getStoredElementFile(Identifier identifier);
	public FileInputStream getStreamedContent(Identifier identifier) throws MatrosServiceException;

}
