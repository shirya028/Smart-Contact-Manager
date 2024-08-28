package com.smartContactM.helper;

import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	public void sendEmail(String subject ,String message,String to) {
		String host ="smtp.gmail.com";
		Properties prop = System.getProperties();
		
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.sslenable", "true");
		prop.put("mail.smtp.auth", "true");
		
	}

}
