package customers;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class EmailLogAspect {

    @After("execution(* customers.EmailSender.sendEmail(..)) && args(email, message)")
    public void logFullEmailDetails(JoinPoint joinPoint, String email, String message) {
        // Get the target object (EmailSender)
        EmailSender emailSender = (EmailSender) joinPoint.getTarget();
        String outgoingServer = emailSender.getOutgoingMailServer();

        // Log all details
        System.out.println(LocalDateTime.now() + " method=sendEmail address=" + email);
        System.out.println("message= " + message);
        System.out.println("outgoing mail server = " + outgoingServer);
    }
}
