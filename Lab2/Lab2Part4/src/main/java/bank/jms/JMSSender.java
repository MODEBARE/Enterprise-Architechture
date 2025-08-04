package bank.jms;

import bank.logging.ILogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JMSSender implements IJMSSender {

	private final ILogger logger;

	@Autowired
	public JMSSender(ILogger logger) {
		this.logger = logger;
	}

	public void sendJMSMessage(String text) {
		System.out.println("JMSSender: sending JMS message = " + text);
		logger.log("JMS message sent: " + text);
	}
}
