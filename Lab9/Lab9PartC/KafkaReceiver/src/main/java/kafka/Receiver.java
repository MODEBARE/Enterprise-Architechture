package kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    @KafkaListener(topics = "topicA", groupId = "group1")
    public void receive(String message) {
        System.out.println("Receiver received message: " + message);
    }
} 