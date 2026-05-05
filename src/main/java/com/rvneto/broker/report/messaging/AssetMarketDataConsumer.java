package com.rvneto.broker.report.messaging;

import com.rvneto.broker.report.document.AssetPriceHistory;
import com.rvneto.broker.report.dto.AssetMarketDataDTO;
import com.rvneto.broker.report.repository.AssetPriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetMarketDataConsumer {

    private final AssetPriceHistoryRepository assetPriceHistoryRepository;

    @KafkaListener(
            topics = "${app.kafka.consumer.topic-asset-market-data}",
            groupId = "broker-report-api",
            containerFactory = "assetMarketDataKafkaListenerContainerFactory"
    )
    public void consume(AssetMarketDataDTO event) {
        log.info("Asset market data received: ticker={} price={}", event.getTicker(), event.getPrice());
        try {
            AssetPriceHistory history = AssetPriceHistory.builder()
                    .ticker(event.getTicker())
                    .name(event.getName())
                    .price(event.getPrice())
                    .timestamp(event.getTimestamp() != null ? event.getTimestamp() : LocalDateTime.now())
                    .savedAt(LocalDateTime.now())
                    .build();

            assetPriceHistoryRepository.save(history);
            log.info("Asset price history saved for ticker={}", event.getTicker());
        } catch (Exception e) {
            log.error("Failed to process asset market data for ticker={}: {}", event.getTicker(), e.getMessage(), e);
            throw e;
        }
    }
}
