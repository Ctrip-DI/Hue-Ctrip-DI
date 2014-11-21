package com.ctrip.di.hive.alert;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EMail {
	private static final Log logger = LogFactory.getLog(EMail.class);

	public final static String HOST = "*";
	public final static String USER = "*";
	public final static String PASSWORD = "*";
	public final static String FROM = "*";

	/**
	 * send email
	 * 
	 * @throws MessagingException
	 * @throws Exception
	 */
	public static int sendMail(String subject, String content, String mails,
			String cc) throws MessagingException {

		Properties props = new Properties();
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.starttls.enable", "true");
		// props.put("mail.smtp.port", "25");
		props.put("mail.smtp.auth", "true");
		// props.put("mail.debug", "true");
		Session mailSession = Session.getInstance(props, new MyAuthenticator());

		Message message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(FROM));
		message.addRecipients(Message.RecipientType.TO, getMailList(mails));
		message.addRecipients(Message.RecipientType.CC, getMailList(cc));

		message.setSubject(subject);
		message.setContent(content, "text/html;charset=utf-8");

		Transport transport = mailSession.getTransport("smtp");
		try {
			transport.connect(HOST, USER, PASSWORD);
			transport.sendMessage(message, message.getAllRecipients());
		} finally {
			if (transport != null)
				transport.close();
		}

		return 0;
	}

	public static InternetAddress[] getMailList(String mails) {
		if (mails == null) {
			return null;
		}
		String[] toMails = mails.split(";");
		InternetAddress[] to = new InternetAddress[toMails.length];
		try {
			for (int i = 0; i < toMails.length; i++) {
				to[i] = new InternetAddress(toMails[i].trim());
			}
		} catch (AddressException e) {
			logger.error("The email address error! Ignore:", e);
		}

		return to;
	}

}

class MyAuthenticator extends Authenticator {
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(EMail.USER, EMail.PASSWORD);
	}
}