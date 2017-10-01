package net.schwehla.matrosdms.lucene;

import java.io.File;

import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;

public interface ILuceneService {
	
	public void bootstrap();
	
	public MatrosMetadata parseMetadata(File myNewContainer) throws Exception;
}
