package net.schwehla.matrosdms.persistenceservice.crypt.internal;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Tester {

	public static void main(String[] args) {

		MatrosInternalFileDecryptor c = new MatrosInternalFileDecryptor();
		
		MatrosFileEncryptor b = new MatrosFileEncryptor();

		Path in = Paths.get(URI.create("file:/d:/java/x.log"));
		Path out = Paths.get(URI.create("file:/d:/java/x.log.crypt"));
		Path outBack = Paths.get(URI.create("file:/d:/java/x.log.back"));
		
		/*
		Path in = Paths.get(URI.create("file:/d:/java/graphdms.7z"));
		Path out = Paths.get(URI.create("file:/d:/java/graphdms.7z.crypt"));
		Path outBack = Paths.get(URI.create("file:/d:/java/graphdms_back.7z"));
		*/

		out.toFile().delete();
		outBack.toFile().delete();
		
		

		
		
		System.out.println("start");
		
		try {
			
			String algo = "MD5";
			
			
		//	System.out.println("fin Crypt : " + c.copyTo(in, out, "MD5"));
			
			System.out.println("fin Decrypt : " + b.copyTo(out, outBack));

	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	


}
