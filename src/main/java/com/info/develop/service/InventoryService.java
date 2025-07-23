package com.info.develop.service;

import com.info.develop.config.KafkaTopicConfig;
import com.info.develop.model.InventoryUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public InventoryService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void processInventoryUpdate(InventoryUpdate inventoryUpdate) {
        // Here you might have business logic to validate the inventory change
        // before publishing the event.
        kafkaProducerService.sendMessage(KafkaTopicConfig.TOPIC_INVENTORY_UPDATES, inventoryUpdate.getProductId(), inventoryUpdate);
    }
}