package bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaBankApplication.class, args);
        System.out.println("🏦 Kafka Bank Application is running and waiting for operations...");
        System.out.println("   Listening on topic: bankOperations");
        System.out.println("   Supported operations: CREATE_ACCOUNT, DEPOSIT, WITHDRAW");
        System.out.println();
    }
} 