package net.schwehla.matrosdms.persistenceservice.internal.cryptprovider;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.MessageDigest;

import net.schwehla.matrosdms.persistenceservice.internal.MatrosConfigReader;


public abstract class AbstractMatrosCryptor implements IMatrosStoreCryptor {
	
	// XXX
	protected MatrosConfigReader configReader = new MatrosConfigReader();

	Path cloudRoot;
	final ByteBuffer buffer = ByteBuffer.allocateDirect(4*1024);
	

    protected byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }
    
    

	
	OpenOption[] optionsRead = new OpenOption[] { READ };
	OpenOption[] optionsWrite = new OpenOption[] { WRITE ,CREATE};
	
	
	public AbstractMatrosCryptor(Path localPath) {
		this.cloudRoot = localPath;
	}
	
	public Path getCloudRoot() {
		return cloudRoot;
	}
	
	
    protected String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
	
    
    protected synchronized String getSHA256(File sourcePath) throws Exception {
    	
  
    	 final int SIZE = 32 * 1024; 
    	 final int SIZE2 = SIZE * 1024 * 100;
    	 
    	 try (FileInputStream in = new FileInputStream(sourcePath)) {
    		 
    	   MessageDigest md = MessageDigest.getInstance("SHA-256");
    	   
    	 FileChannel channel = in.getChannel(); 
    	 long length = channel.size(); 
    	 long iterations = length / SIZE2; 
    	 long reverseLength = 0L; 
    	 
    	 for (int i = 0; i <= iterations; i++) {
    		 
    	   	 MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY,
    	   			reverseLength, i == iterations ? length % SIZE2: SIZE2);
    	   	 
    	   	 reverseLength += SIZE2; 
    	   	 
    	   	 byte[] bytes = new byte[SIZE];
    	 	 int nGet; 
        	 while (mb.hasRemaining()) {
        		  nGet = Math.min(mb.remaining(), SIZE);
        		  mb.get(bytes, 0, nGet);
        		  md.update(bytes, 0, nGet); 
        		  
        	 } 
  
    		 
    	 }
    	
    	 return convertByteArrayToHexString(md.digest());
    	 
    	 }
    }
    
    
    
}
