package com.smartlab.tagman.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	Properties prop;
	Session session;
	String from = "himesh.nandani@dal.ca";

	public SendEmail() {
		prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.mailtrap.io");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
		session = Session.getDefaultInstance(prop);

	}

	public boolean sendEmail(String to) {

		try {
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress());
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Invitation to participate in the Research Project");
			message.setText("Hello, this is example of sending email  ");

			// Send message
			Transport.send(message);
			System.out.println("message sent successfully....");
			return true;

		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}

	}
}
