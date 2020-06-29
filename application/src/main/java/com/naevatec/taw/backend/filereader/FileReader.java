package com.naevatec.taw.backend.filereader;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

public class FileReader implements Serializable{

	private static final long serialVersionUID = 2211331555694532140L;

	public String parseToPlainText(File file) throws Exception {
		
		InputStream fileStream = new FileInputStream(file);
	    org.xml.sax.ContentHandler handler = new BodyContentHandler();
	    AutoDetectParser parser = new AutoDetectParser();
	    ParseContext context = new ParseContext();
	    Metadata metadata = new Metadata();
	    
	    try {
	    	System.out.println("Starting parsing...");
	        parser.parse(fileStream, handler, metadata, context);
	        System.out.println("Parsing finished...");
	        return handler.toString();
	    }
	    catch (IOException | SAXException | TikaException e) {
	    	System.out.println("Exception 1!");
			throw new Exception("TIKA was not able to exctract text of file '" + file.getName() + "'");
		} finally {
			try {
				System.out.println("Closing file...");
				fileStream.close();
			} catch (IOException e) {
				System.out.println("Exception 2!");
				throw new Exception(e);
			}
		}
	}
}
