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

    private SendPersonService sendPersonService;
    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    public MessageListener(SendPersonService sendPersonService) {
        this.sendPersonService = sendPersonService;
    }

    @KafkaListener(topics = "${kafka.topic.transmitter}", containerFactory = "personKafkaListenerContainerFactory")
    public void listenAsset(@Payload Person person) {
        logger.info("received person message='{}'", person);
    }

}
