package com.zukalover.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
//ACTIVEMQ RECEIVER
@Component
public class MessageSender {
	
	private Logger logger = LoggerFactory.getLogger(MessageSender.class);
	
	JmsTemplate jmsTemplate;
	
	@Autowired
	public MessageSender(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate=jmsTemplate;
	}

	public void sendReturnMessage(String message)
	{
		
	/**
	 * 	this.jmsTemplate.convertAndSend("OutputQueue",message);
	 */
	}
	
	
}
