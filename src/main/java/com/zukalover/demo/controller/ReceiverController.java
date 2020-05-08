package com.zukalover.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.zukalover.demo.service.TesseractService;

@Controller
public class ReceiverController {

	@Autowired
	TesseractService tesseractService;
	Logger logger = LoggerFactory.getLogger(ReceiverController.class);
	
	@GetMapping("/textArea")
	public String redirectOutput(HttpServletRequest request) throws IOException
	{	
		//COMMENT THIS WHOLE PART WITH MAIN APP- LOGIN -REGISTRATION
		String output = "";
		try {
			 output = tesseractService.readText();
			
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		request.setAttribute("nav", "TRUE");
		request.setAttribute("textOutput", output);
		request.setAttribute("mode","MODE_HOME");
		
		
		return "output";
		
	}
}
