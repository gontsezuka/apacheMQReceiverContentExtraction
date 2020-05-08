package com.zukalover.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zukalover.demo.MessageSender;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
@Transactional
public class TesseractService {
	
	@Autowired
	MessageSender messageSender;
	
	@Autowired
	SolrService solrService;
	
	private Logger logger = Logger.getLogger(TesseractService.class);
	public TesseractService()
	{
		
	}
	
	/*
	//THIS METHOD IS CURRENTLY NOT WORKING---BEFORE FEB 2020
	public void storeToText(String text) throws IOException
	{
		File f = new File("/srv/www/htdocs/tessOutput.txt");
	
			f.createNewFile();
		
	
		BufferedWriter writer= null;
		
		writer = new BufferedWriter(new FileWriter("/srv/www/htdocs/tessOutput.txt", true));
		logger.info("WRITING TO FILE");
			
		writer.newLine();  //Add new line - true if for appending
		writer.write(text);
		writer.close();
	}
	*/
	
	//SECOND METHOD FOR SENDING THE TEXT DOCUMENT NAME
	public void storeToText(String text, String name) throws IOException
	{
		File f = new File("/srv/www/htdocs/"+name+".txt");
	
			f.createNewFile();
		
	
		BufferedWriter writer= null;
		
		writer = new BufferedWriter(new FileWriter("/srv/www/htdocs/"+name+".txt", true));
		logger.info("WRITING TO FILE");
			
		writer.newLine();  //Add new line - true if for appending
		writer.write(text);
		writer.close();
	}
	
	
	
	/**
	 * METHOD FROM OLDER IMPLEMENTATION PRIOR 04 FEBRUARY
	 * @param message
	 * @param name
	 * @throws IOException
	 */
	/**
	public void sendToTesseract(String message) throws IOException 
	{
		logger.info("IMAGE RECEIVED FOR TESSERACT");
		File image = new File(message);
		String text="";
		Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tessdata/");
        
        try {
        	
        	logger.info("TESSERACT NOW EXTRACTING");
			 text = tesseract.doOCR(image);
			storeToText(text);// we will send to queue not to methods //COMMENT THIS WITH MAIN APP LOGIN-REGISTRATION
			 
			 
			// messageSender.sendReturnMessage(text); //USED TO RETURN TEXT TO MAIN APP LOGIN-REG
			logger.info("SENT TO BE APPENDED TO FILE");
			
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		//logger.info(text);
	}
	*/
	/**
	 * SECOND METHOD FOR SENDING THE TEXT DOCUMENT NAME
	 * @param message
	 * @param name
	 * @throws IOException
	 */
	public void sendToTesseract(String message,String name) throws IOException 
	{
		logger.info("IMAGE RECEIVED FOR META DATA EXTRACTION");
		File image = new File(message);
		String text="";
		Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tessdata/");
        
        try {
        	
        	logger.info("META DATA IS GETTING EXTRACTED");
			 text = tesseract.doOCR(image);
			 solrService.sendToSorl(message, text);
			 
			storeToText(text,name);// we will send to queue not to methods //COMMENT THIS WITH MAIN APP LOGIN-REGISTRATION
			 
			 
			// messageSender.sendReturnMessage(text); //USED TO RETURN TEXT TO MAIN APP LOGIN-REG
			logger.info("SENT TO BE APPENDED TO FILE");
			
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		/**
		 * logger.info(text);
		 */
	}
	
	/**
	 * THIS METHOD IS CURRENTLY NOT WORKING WITH THE MAIN APP(ONLY PROTOTYPE)
	 * @return
	 * @throws IOException
	 */
	public String readText() throws IOException
	{
		StringBuilder contentBuilder = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader("/home/gontse/tesseractOutput/tessOutput.txt"));
		
		String cLine;
		
		while((cLine = br.readLine()) != null)
		{
			contentBuilder.append(cLine).append("\n");
		}
		return contentBuilder.toString();
	}
}
