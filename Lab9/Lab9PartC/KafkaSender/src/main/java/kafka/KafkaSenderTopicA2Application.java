package kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaSenderTopicA2Application implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaSenderTopicA2Application.class, args);
        context.close();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== KAFKA SENDER FOR TOPICA2 ===");
        
        // Send message to TopicA2
        String message = "Hello from TopicA2";
        System.out.println("Sending message to TopicA2: " + message);
        kafkaTemplate.send("TopicA2", message);
        
        System.out.println("Message has been sent to TopicA2");
        System.out.println("=== KAFKA SENDER TOPICA2 COMPLETED ===");
        
        // Small delay to ensure message is sent
        Thread.sleep(1000);
    }
} 