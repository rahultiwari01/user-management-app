package com.info.develop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a message to a Kafka topic and returns a CompletableFuture for the send result.
     * This allows the calling service to optionally handle the result asynchronously.
     * The service also logs the outcome of the send operation.
     *
     * @param topic   The target topic.
     * @param key     The message key, used for partitioning.
     * @param payload The message payload.
     * @param <T>     The type of the payload.
     * @return A CompletableFuture holding the SendResult, allowing for further asynchronous processing.
     */
    public <T> CompletableFuture<SendResult<String, Object>> sendMessage(String topic, String key, T payload) {
        LOGGER.info("Sending message to Kafka topic: [{}] with key: [{}]", topic, key);
        Message<T> message = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOGGER.debug("Successfully sent message with key '{}' to topic {} and offset {}", key, topic, result.getRecordMetadata().offset());
            } else {
                LOGGER.error("Failed to send message with key '{}' to topic {}: {}", key, topic, ex.getMessage(), ex);
            }
        });
        return future;
    }
}