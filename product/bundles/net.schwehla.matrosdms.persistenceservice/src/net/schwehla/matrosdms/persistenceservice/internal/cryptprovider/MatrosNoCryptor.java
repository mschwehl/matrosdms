package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider;

import java.io.File;
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public File getStoredElementFile(Identifier identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getDisplayLink(Identifier identifier) throws MatrosServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
