package net.schwehla.matrosdms.persistenceservice.crypt.internal;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MatrosInternalFileCryptor {
	
	
	final ByteBuffer buffer = ByteBuffer.allocateDirect(4*1024);
	

    protected byte[] getUTF8Bytes(String input) {

        return input.getBytes(StandardCharsets.UTF_8);

    }
    
    final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes("1234567890123456"),"AES");

    final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));

    Properties properties = new Properties();

    final String transform = "AES/CBC/PKCS5Padding";

    /**

     * Converts String to UTF8 bytes

     *

     * @param input the input string

     * @return UTF8 bytes

     */



	
}
