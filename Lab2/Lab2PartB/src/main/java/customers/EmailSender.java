package customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSender implements IEmailSender {

	@Value("${mailserver.outgoing}")
	private String outgoingMailServer;

	private ILogger logger;

	@Autowired
	public EmailSender(ILogger logger) {
		this.logger = logger;
	}

	public String getOutgoingMailServer() {
		return outgoingMailServer;
	}

	public void sendEmail(String email, String message) {
		System.out.println("EmailSender: sending '" + message + "' to " + email);
		logger.log("Email is sent: message= " + message + " , emailaddress = " + email);
	}

}
