package com.transformer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformer.dto.Person;
import com.transformer.service.TransformPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);
    private TransformPersonService transformPersonService;
    private MessageProducer messageProducer;
    private ObjectMapper objectMapper;


    public MessageListener(TransformPersonService transformPersonService, MessageProducer messageProducer, ObjectMapper objectMapper) {
        this.transformPersonService = transformPersonService;
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic.receiver}", containerFactory = "personKafkaListenerContainerFactory")
    public void listenPerson(@Payload Person person) throws JsonProcessingException {
        logger.info("received person:{}", person);
        Person transformedPerson = transformPersonService.transformPerson(person);
        logger.info("sending transformed person to kafka:'{}'", transformedPerson);
        messageProducer.sendMessage(objectMapper.writeValueAsString(transformedPerson));
    }

}
