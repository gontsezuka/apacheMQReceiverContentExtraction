package com.zukalover.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.zukalover.demo.service.TesseractService;

@Component //enables the detection of jmslistener annotations
public class MessageConsumer {
	
	
	
	/**
	 * THIS IS FOR THE ACTIVEMQ RECEIVER
	 */
	
	
	@Autowired
	TesseractService tesseractService;
	private final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

	/**
	 * Author: Gontse Mochoana
	 * Date: 2019 December
	 * Purpose: This method listens for messages on the Queue
	 * @param message
	 * @throws IOException
	 */
	@JmsListener(destination="imageQueue1")//connects to queue and listens to messages on queue
	public void listener(String message) 
	{
		String documentID = getDocumentID(message);
		logger.info(message);
		
		logger.info("SENDING IMAGE FOR META-DATA EXTRACTION");
		
		
			try {
				Thread.sleep(3500);
			} catch (InterruptedException e1) {
				logger.info(e1.getMessage());
			}

		try {
			tesseractService.sendToTesseract(message,documentID);
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		
	}
	
	/**
	 * Author: 	Gontse Mochoama
	 * Date: 2019 December
	 * Purpose: This method extracts the document ID from the message received on queue
	 * @param message
	 * @return
	 */
	public String getDocumentID(String message)
	{
		/**
		 * String subName= message.substring(28);
		 */
		String subName = getSubstringWithRemovedPathFromMessage(message);
		
		/**
		 * int dashpos= subName.indexOf('-');
		 */
		int dashpos = getIndexOfSpecifiedCharacter('-', subName);
		
		return subName.substring(0, dashpos); //minus 1 from dashpos
		
	
	}
	
	/**
	 * Author: Gontse Mochoana
	 * Date: 2019 December
	 * Purpose: This method removes the image file path
	 * @param message
	 * @return
	 */
	public String getSubstringWithRemovedPathFromMessage(String message)
	{
		return message.substring(28);
	}
	
	/**
	 * Author: Gontse Mochoana
	 * Date: 2019 December
	 * Purpose: This method gets the index of the specified character in a string
	 * @param characterToIndex
	 * @param subNameToSearchCharacter
	 * @return
	 */
	public int getIndexOfSpecifiedCharacter(char characterToIndex, String subNameToSearchCharacter)
	{
		return subNameToSearchCharacter.indexOf(characterToIndex);
	}
}
