package com.lab.restaurant_service.service;

import com.lab.order_service.events.OrderPlacedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderPlacedConsumer {

    @KafkaListener(topics = "order.placed", groupId = "restaurant-service")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        System.out.println("Received order placed event in restaurant-service: " + event);

        log.debug("Event consumed successfully: {}", event);
    }
}

