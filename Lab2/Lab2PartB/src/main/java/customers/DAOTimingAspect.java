package customers;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class DAOTimingAspect {

    @Around("execution(* customers.CustomerDAO.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint call) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start(call.getSignature().getName());

        Object result = call.proceed();  // Proceed with the method

        sw.stop();
        long totalTime = sw.getLastTaskTimeMillis();

        System.out.println("Method " + call.getSignature().getName() +
                " executed in " + totalTime + " ms");

        return result;
    }
}
