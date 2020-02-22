package com.receiver.config;

import com.receiver.dto.Person;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaListenerConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.consumer.auto-offset-reset}")
    private String offsetConfig;

    private static final Logger logger = LoggerFactory.getLogger(KafkaListenerConfig.class);

    /**
     * Consumer basic configuration.
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetConfig);

        return props;
    }

    /**
     * Consumer factory.
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * String listener container factory.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
        kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /**
     * Person consumer factory.
     */
    @Bean
    public ConsumerFactory<String, Person> personConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(), new JsonDeserializer<>(Person.class));
    }

    /**
     * Person listener container factory.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Person> personKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Person> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(personConsumerFactory());
        factory.setErrorHandler(new ErrorHandler() {
            @Override
            public void handle(Exception e,
                               List<ConsumerRecord<?, ?>> records,
                               Consumer<?, ?> consumer,
                               MessageListenerContainer container) {
                String s = e.getMessage()
                        .split("Error deserializing key/value for partition ")[1]
                        .split(". If needed, please seek past the record to continue consumption.")[0];
                String topic = s.split("-")[0];
                int offset = Integer.parseInt(s.split("offset ")[1]);
                int partition = Integer.parseInt(s.split("-")[1].split(" at")[0]);

                TopicPartition topicPartition = new TopicPartition(topic, partition);
                logger.error("Error deserializing value in offset {}, moving to next one", offset);
                consumer.seek(topicPartition, (long)offset + 1);
            }

            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord) {
                // Override handle method
            }

            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord, Consumer<?,?> consumer) {
                // Override handle method
            }
        });

        return factory;
    }

}
