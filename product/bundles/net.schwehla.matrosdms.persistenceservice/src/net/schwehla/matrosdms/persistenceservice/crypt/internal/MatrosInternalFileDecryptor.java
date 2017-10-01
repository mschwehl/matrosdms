package net.schwehla.matrosdms.persistenceservice.crypt.internal;

;
	
	public class MatrosInternalFileDecryptor extends MatrosInternalFileCryptor {

		/*
	public String copyTo(Path sourcePath, Path targetPath, String hashAlgorithm) throws IOException, NoSuchAlgorithmException {
		
		// XXX eventuell copyAttributes

		OpenOption[] optionsRead = new OpenOption[] { READ };
		OpenOption[] optionsWrite = new OpenOption[] { WRITE ,CREATE};
		
		MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
		
		 try (
				 FileChannel inChannel = FileChannel.open(sourcePath,optionsRead);
				 FileChannel outChannel =  FileChannel.open(targetPath,optionsWrite);
				 
				 MyCannel my = new MyCannel(outChannel,digest);
				 				 
				 CryptoOutputStream cos = new CryptoOutputStream(transform, properties, my, key, iv)) {

	            while (inChannel.read(buffer) != -1) {
	            	buffer.flip();
	            	cos.write(buffer);
	            	buffer.compact();
	            }

	            buffer.flip();   
	                	
	            while(buffer.hasRemaining() ) {
	            	cos.write(buffer);
	            }

	        }
		 
		 long sourceLength = Files.size(sourcePath);
		 long targetLength = Files.size(targetPath);
		 
		 if (sourceLength > 0 
				 && targetLength > 0
				 && targetLength >= sourceLength
				 // padding
				 && targetLength - sourceLength < 17) {
			 
			    // cos must be closed !! done by implementing autoclose
			 return convertByteArrayToHexString(digest.digest());
			 
		 }
		 
		 
		 throw new IllegalStateException("Something went wrong storing, check " + targetPath);
		
	}
	
	*/

	
    protected String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
 	
}
