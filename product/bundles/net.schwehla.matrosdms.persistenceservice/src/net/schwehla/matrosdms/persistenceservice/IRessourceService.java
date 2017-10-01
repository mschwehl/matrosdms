package net.schwehla.matrosdms.persistenceservice;

import java.util.List;

import net.schwehla.matrosdms.domain.util.InboxFile;

public interface IRessourceService {
	
    List<InboxFile> scanInbox(String path);

}
