package com.transformer.kafka;

import com.transformer.dto.Person;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Setter
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Value(value = "${kafka.topic.transformer}")
    private String topicName;

    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaTemplate<String, Person> personKafkaTemplate;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate,
                           KafkaTemplate<String, Person> personKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.personKafkaTemplate = personKafkaTemplate;
    }

    /**
     * Send message to kafka as string.
     */
    public boolean sendMessage(String message) {
        try {
            kafkaTemplate.send(topicName, message);
            return true;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Send message to kafka as Asset object.
     */
    public boolean sendPersonMessage(Person person) {
        try {
            personKafkaTemplate.send(topicName, person);
            return true;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

}
