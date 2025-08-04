package kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaSenderApplication implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaSenderApplication.class, args);
        context.close();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== KAFKA SENDER APPLICATION ===");
        
        // Send message to topicA
        String message = "Hello World";
        System.out.println("Sending message to topicA: " + message);
        kafkaTemplate.send("topicA", message);
        
        System.out.println("Message has been sent");
        System.out.println("=== KAFKA SENDER COMPLETED ===");
        
        // Small delay to ensure message is sent
        Thread.sleep(1000);
    }
} 