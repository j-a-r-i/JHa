import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.smtp.SMTPTransport;

public class EMail {
	private Session session;
	
	public EMail() {
		Properties props = System.getProperties();
		props.put("mail.smtps.host","smtp.gmail.com");
		props.put("mail.smtps.auth","true");
		session = Session.getInstance(props, null);		
	}
	
	public void send() {
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("mail@tovare.com"));
			msg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(Config.getEmailTo(), false));
			msg.setSubject("JHa - "+System.currentTimeMillis());
			msg.setText("Ths is test message.");
			msg.setHeader("X-Mailer", "JHA program");
			msg.setSentDate(new Date());
			SMTPTransport t =
			    (SMTPTransport)session.getTransport("smtps");
			t.connect("smtp.gmail.com", "admin@tovare.com", "<insert password here>");
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		};
		
	}
}
