package com.receiver.kafka;

import com.receiver.dto.Person;
import com.receiver.service.SendPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @KafkaListener(topics = "${kafka.topic.transformer}", containerFactory = "personKafkaListenerContainerFactory")
    public void listenPerson(@Payload Person person) {
        logger.info("received transformed person message='{}'", person);
    }

}
