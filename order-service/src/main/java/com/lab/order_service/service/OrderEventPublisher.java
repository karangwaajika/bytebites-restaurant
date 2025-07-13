package com.lab.order_service.service;

import com.lab.order_service.events.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void publishOrderPlacedEvent(OrderPlacedEvent event) {
        kafkaTemplate.send("order.placed", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("✅ Successfully sent event: " + event);
                    } else {
                        System.err.println("❌ Failed to send event: " + event + ". Reason: " + ex.getMessage());
                        // Optionally: save to DB or retry queue
                    }
                });
    }
}
