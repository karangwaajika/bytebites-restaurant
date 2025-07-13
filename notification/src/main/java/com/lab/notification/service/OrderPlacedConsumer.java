package com.lab.notification.service;

import com.lab.order_service.events.OrderPlacedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderPlacedConsumer {

    @KafkaListener(topics = "order.placed", groupId = "notification-service")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        System.out.println("Received order placed event in restaurant-service: " + event);

    }
}

