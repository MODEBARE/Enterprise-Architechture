package kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiverTopicA2 {

    @KafkaListener(topics = "TopicA2", groupId = "group1")
    public void receive(String message) {
        System.out.println("ReceiverTopicA2 received message: " + message);
    }
} 