package kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Receiver2 {

    @KafkaListener(topics = "topicA", groupId = "group2")
    public void receive(String message) {
        System.out.println("Receiver2 received message: " + message);
    }
} 