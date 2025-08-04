package bank;

import bank.logging.ILogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class BankAopAspect {

    @Autowired
    private ILogger logger;

    // (a) Log all DAO method calls
    @Before("execution(* bank.dao..*(..))")
    public void logDaoCall(JoinPoint joinPoint) {
        logger.log("DAO method called: " + joinPoint.getSignature().toShortString());
    }

    // (b) Time all service method executions
    @Around("execution(* bank.service..*(..))")
    public Object measureServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(joinPoint.getSignature().getName());

        Object result = joinPoint.proceed();

        stopWatch.stop();
        long totalTimeMillis = stopWatch.getLastTaskTimeMillis();
        System.out.println("Method [" + joinPoint.getSignature().getName() + "] executed in " + totalTimeMillis + " ms");

        return result;
    }

    // (c) Log every JMS message sent
    @After("execution(* bank.jms.JMSSender.sendJMSMessage(..)) && args(message)")
    public void logJmsMessage(String message) {
        logger.log("JMS message logged via AOP: " + message);
    }
}
