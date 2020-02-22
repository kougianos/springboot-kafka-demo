package com.receiver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.receiver.dto.Person;
import com.receiver.kafka.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendPersonService {

    private static final Logger logger = LoggerFactory.getLogger(SendPersonService.class);

    private MessageProducer messageProducer;
    private ObjectMapper objectMapper;

    /**
     * Constructor.
     */
    public SendPersonService(MessageProducer messageProducer,
                              ObjectMapper objectMapper) {
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
    }

    /**
     * Send Person object as json string to kafka topic.
     */
    public void sendPersonToKafka(Person person) {

        try {
            messageProducer.sendMessage(objectMapper.writeValueAsString(person));
            logger.info("sent person to kafka: {}", person);
        } catch (RuntimeException | JsonProcessingException e) {
            logger.error("failed to send message to kafka: {}", e.getMessage());
        }

    }


}
