package com.zukalover.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
@EnableJms
@SpringBootApplication
public class Sender {

	/**
	 * WORKS WITH BLACK SCREEN FINAL APP
	 * HAS TESSERACT
	 * THE QUEUE RECEIVER
	 */
	public static void main(String[] args) {
		SpringApplication.run(Sender.class, args);
	}

}

