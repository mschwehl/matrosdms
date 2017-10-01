package net.schwehla.matrosdms.persistenceservice.crypt.internal;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import org.apache.commons.crypto.stream.CryptoInputStream;;
	
	public class MatrosFileEncryptor extends MatrosInternalFileCryptor {

		
		OpenOption[] optionsRead = new OpenOption[] { READ };
		OpenOption[] optionsWrite = new OpenOption[] { WRITE ,CREATE};
		
	public String copyTo(Path sourcePath, Path targetPath) throws IOException {
			
		 try (
				 FileChannel inChannel = FileChannel.open(sourcePath,optionsRead);
				 FileChannel outChannel = FileChannel.open(targetPath,optionsWrite);
				 
				 CryptoInputStream cis = new CryptoInputStream(transform, properties, inChannel, key, iv)) {

	            while (cis.read(buffer) != -1) {
	            	
	            	buffer.flip();
	            	outChannel.write(buffer);
	            	buffer.compact();
	            }

	   
	            buffer.flip();
	            
	            while(buffer.hasRemaining() ) {
	            	outChannel.write(buffer);
	            }

	            cis.close();

	        }
		 
		 return "";
		
	}

	public ReadableByteChannel stream(Path sourcePath) throws IOException {
		

			 FileChannel inChannel = FileChannel.open(sourcePath,optionsRead);
			 
			 CryptoInputStream cis = new CryptoInputStream(transform, properties, inChannel, key, iv);

		 return cis;
	
	}
	
}
