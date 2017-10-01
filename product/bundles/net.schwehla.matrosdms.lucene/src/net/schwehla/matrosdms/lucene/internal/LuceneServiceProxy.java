package net.schwehla.matrosdms.lucene.internal;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.eclipse.e4.core.services.log.Logger;

import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.lucene.ILuceneService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;


@Singleton
public class LuceneServiceProxy implements ILuceneService {

	
    @Inject
    Logger logger;
    
	/*
	@Override
	public void demo() throws Exception {

		

		File file = new File("D:/java/forschung/lucene-tika-master/data/s2016.pdf");//

		// Instantiating tika facade class
		Tika tika = new Tika();
		tika.setMaxStringLength(-1);
		

		// detecting the file type using detect method
		String filetype = tika.detect(file);
		System.out.println(filetype);

		// Parser method parameters
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);
		
		
		 
		TikaInputStream  tikaInputStream2 = TikaInputStream.get(inputstream);
	       
		
		ParseContext context = new ParseContext();
		parser.parse(tikaInputStream2, handler, metadata, context);
	
		
		String content = handler.toString();
		
	      
	//      System.out.println("Contents of the PDF :" + content);
	  	
	
		
	//	System.out.println(handler.toString());

		// getting the list of all meta data elements
		String[] metadataNames = metadata.names();

		for (String name : metadataNames) {
			System.out.println(name + ": " + metadata.get(name));
		}
		
		

		
		// index
		

		
		 Directory directory = FSDirectory.open(new File("c:\\temp\\index").toPath());
		
			IndexWriterConfig config = new IndexWriterConfig(
					new StandardAnalyzer());
			IndexWriter indexWriter = new IndexWriter(directory, config);
		 
		 
			Document doc = new Document();
			doc.add(new TextField("path", file.getName(), Store.YES));
		
			doc.add(new TextField("UUID", UUID.randomUUID().toString(), Store.YES));
			
			
			doc.add(new TextField("contents",content,
					Store.YES));
			indexWriter.addDocument(doc);
			
			indexWriter.close();
	
			
			String searchText = "Burtea";
	
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser queryParser = new QueryParser("contents",
						new StandardAnalyzer());
				Query query = queryParser.parse(searchText);

				String termText = searchText;
				Term termInstance = new Term("contents", termText);
				long termFreq = indexReader.totalTermFreq(termInstance);
				long docCount = indexReader.docFreq(termInstance);

				System.out.println("term: " + termText + ", termFreq = " + termFreq
						+ ", docCount = " + docCount);

				TopDocs topDocs = indexSearcher.search(query, 10);
				System.out.println("totalHits " + topDocs.totalHits);
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					System.out.println("path " + document.get("path"));
					System.out.println("score " + scoreDoc.score);
					System.out.println("UUID " + document.get("UUID"));
				}
		
	
				
				
	}

*/
//	
//	public static String detectMimeType(final File file) throws IOException {
//	    TikaInputStream tikaIS = null;
//	    try {
//	        tikaIS = TikaInputStream.get(file);
//
//	        /*
//	         * You might not want to provide the file's name. If you provide an Excel
//	         * document with a .xls extension, it will get it correct right away; but
//	         * if you provide an Excel document with .doc extension, it will guess it
//	         * to be a Word document
//	         */
//	        final Metadata metadata = new Metadata();
//
//
//	        return DETECTOR.detect(tikaIS, metadata).toString();
//	        

    
	// Instantiating tika facade class
	Tika tika = new Tika();
	
	@Override
	public MatrosMetadata parseMetadata(File inboxElement) throws MatrosServiceException {
		
		// http://stackoverflow.com/questions/21163108/custom-thread-pool-in-java-8-parallel-stream
		
		synchronized (tika) {  
			
			HashCalculator hc = new HashCalculator();
			hc.setFile(inboxElement);
			
			TatinkaMimeParser mimeParser = new TatinkaMimeParser();
			mimeParser.setFile(inboxElement );
			
		       try {
		    	   
			 ExecutorService executor = Executors.newFixedThreadPool(2);
			 executor.execute(hc);
			 executor.execute(mimeParser);
	                 
	         // This will make the executor accept no new threads
	         // and finish all existing threads in the queue
	         executor.shutdown();
	         
	         // Wait until all threads are finish
	  
				executor.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new MatrosServiceException(e.getMessage());
			}
	         
	         if (hc.getResult() == null) {
	        	 throw new MatrosServiceException("Cannot calculate hash");
	         }
	         
	         if (mimeParser.getMetadata().getMimetype() == null) {
	        	 throw new MatrosServiceException("Cannot calculate mimetype");
	         }
	         
	         mimeParser.getMetadata().setSha256(hc.getResult());
	         
	 		
	         return mimeParser.getMetadata();
			
	         
		}

		
		
		
	}
	

	class TatinkaMimeParser  implements Runnable {
		

		
		MatrosMetadata mm = new MatrosMetadata();
		
			
		private File file;

		
		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
		
		public MatrosMetadata getMetadata() {
			return mm;
		}

		@Override
		public void run() {
		
			

			tika.setMaxStringLength(-1);
			
			try (TikaInputStream  tikaInputStream2 = TikaInputStream.get(new FileInputStream(file.getAbsolutePath()));) {
				
				Metadata metadata = new Metadata();
				metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
				

				// detecting the file type using detect method
				String filetype = tika.detect(tikaInputStream2,metadata);
				
				mm.setMimetype(filetype);
				mm.setFilename(new File(file.getAbsolutePath()).getName());
				mm.setFilesize( new Long(file.length()));
				// Parser method parameters
				Parser parser = new AutoDetectParser();
				
				// no end limit
				BodyContentHandler handler = new BodyContentHandler(-1);


				ParseContext context = new ParseContext();
				parser.parse(tikaInputStream2, handler, metadata, context);
				
				mm.setTextLayer(handler.toString());
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			} 
			
		}
		
	}
	
	
	class HashCalculator implements Runnable {

		private File file;
		private String result;
		
		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		@Override
		public void run() {


			
			try {
				StringBuffer hexString = new StringBuffer();
				

				
			//	FileInputStream inputStream = null;

				try ( FileInputStream  inputStream = new FileInputStream(file);
					    FileChannel channel = inputStream.getChannel(); ) {
				    MessageDigest md = MessageDigest.getInstance("SHA-256");
				 

				    long length = file.length();
				    if(length > Integer.MAX_VALUE) {
				        // you could make this work with some care,
				        // but this code does not bother.
				        throw new IOException("File "+file.getAbsolutePath()+" is too large.");
				    }

				    ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, length);

				    int bufsize = 1024 * 8;          
				    byte[] temp = new byte[bufsize];
				    int bytesRead = 0;

				    while (bytesRead < length) {
				        int numBytes = (int)length - bytesRead >= bufsize ? 
				                                     bufsize : 
				                                     (int)length - bytesRead;
				        buffer.get(temp, 0, numBytes);
				        md.update(temp, 0, numBytes);
				        bytesRead += numBytes;
				    }

				    byte[] mdbytes = md.digest();
				    
					for (int i = 0; i < mdbytes.length; i++) {
					    if ((0xff & mdbytes[i]) < 0x10) {
					        hexString.append("0"
					                + Integer.toHexString((0xFF & mdbytes[i])));
					    } else {
					        hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
					    }
					}
					
					result = hexString.toString();

				} catch (NoSuchAlgorithmException e) {
				    throw new IllegalArgumentException("Unsupported Hash Algorithm.", e);
				}
				finally {	
				        hexString = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		
			
		}
		
	}


	@Override
	public void bootstrap() {

		// TODO: do something usefull
		
	}
	


	
	
}
