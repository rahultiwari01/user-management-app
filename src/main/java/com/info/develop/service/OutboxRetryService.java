package com.info.develop.service;

import com.info.develop.model.OutboxMessage;
import com.info.develop.repository.OutboxMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "outbox.retry.enabled", havingValue = "true", matchIfMissing = true)
public class OutboxRetryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboxRetryService.class);

    private final OutboxMessageRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OutboxRetryService(OutboxMessageRepository outboxRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRateString = "${outbox.retry.fixedRate:60000}", initialDelayString = "${outbox.retry.initialDelay:30000}")
    public void retryFailedMessages() {
        List<OutboxMessage> messagesToRetry = outboxRepository.findAll();
        if (messagesToRetry.isEmpty()) {
            return;
        }

        LOGGER.info("Found {} message(s) in outbox. Attempting to resend.", messagesToRetry.size());

        for (OutboxMessage message : messagesToRetry) {
            // Note: The payload is a JSON string. The consumer must be able to handle this.
            var kafkaMessage = MessageBuilder
                    .withPayload(message.getPayload())
                    .setHeader(KafkaHeaders.TOPIC, message.getTopic())
                    .setHeader(KafkaHeaders.KEY, message.getMessageKey())
                    .build();

            kafkaTemplate.send(kafkaMessage).whenComplete((result, ex) -> {
                if (ex == null) {
                    LOGGER.info("Successfully resent message from outbox with key: {}", message.getMessageKey());
                    outboxRepository.delete(message);
                } else {
                    LOGGER.warn("Retry failed for outbox message with key: {}. Will try again later.", message.getMessageKey());
                }
            });
        }
    }
}