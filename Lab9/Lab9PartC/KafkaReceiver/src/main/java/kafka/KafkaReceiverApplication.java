package kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaReceiverApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaReceiverApplication.class, args);
        System.out.println("Receiver is running and waiting for messages");
    }
} 