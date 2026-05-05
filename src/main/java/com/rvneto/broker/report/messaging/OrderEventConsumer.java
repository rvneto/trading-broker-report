package com.rvneto.broker.report.messaging;

import com.rvneto.broker.report.document.OrderSnapshot;
import com.rvneto.broker.report.dto.OrderEventDTO;
import com.rvneto.broker.report.repository.OrderSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderSnapshotRepository orderSnapshotRepository;

    @KafkaListener(
            topics = "${app.kafka.consumer.topic-order-events}",
            groupId = "broker-report-api",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void consume(OrderEventDTO event) {
        log.info("Order event received: orderId={} userId={} status={}",
                event.getOrderId(), event.getUserId(), event.getStatus());
        try {
            OrderSnapshot snapshot = OrderSnapshot.builder()
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .ticker(event.getTicker())
                    .side(event.getSide())
                    .quantity(event.getQuantity())
                    .price(event.getPrice())
                    .executedPrice(event.getExecutedPrice())
                    .status(event.getStatus())
                    .eventTimestamp(event.getEventTimestamp())
                    .savedAt(LocalDateTime.now())
                    .build();

            orderSnapshotRepository.save(snapshot);
            log.info("Order snapshot saved for orderId={}", event.getOrderId());
        } catch (Exception e) {
            log.error("Failed to process order event for orderId={}: {}", event.getOrderId(), e.getMessage(), e);
            throw e;
        }
    }
}
