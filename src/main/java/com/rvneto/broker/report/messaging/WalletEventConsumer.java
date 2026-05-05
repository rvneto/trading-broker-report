package com.rvneto.broker.report.messaging;

import com.rvneto.broker.report.document.WalletSnapshot;
import com.rvneto.broker.report.dto.WalletEventDTO;
import com.rvneto.broker.report.repository.WalletSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletEventConsumer {

    private final WalletSnapshotRepository walletSnapshotRepository;

    @KafkaListener(
            topics = "${app.kafka.consumer.topic-wallet-events}",
            groupId = "broker-report-api",
            containerFactory = "walletKafkaListenerContainerFactory"
    )
    public void consume(WalletEventDTO event) {
        log.info("Wallet event received: type={} userId={} amount={}",
                event.getTransactionType(), event.getUserId(), event.getAmount());
        try {
            WalletSnapshot snapshot = WalletSnapshot.builder()
                    .userId(event.getUserId())
                    .transactionType(event.getTransactionType())
                    .amount(event.getAmount())
                    .ticker(event.getTicker())
                    .orderId(event.getOrderId())
                    .balanceAfter(event.getBalanceAfter())
                    .blockedBalanceAfter(event.getBlockedBalanceAfter())
                    .eventTimestamp(event.getEventTimestamp())
                    .savedAt(LocalDateTime.now())
                    .build();

            walletSnapshotRepository.save(snapshot);
            log.info("Wallet snapshot saved for userId={} type={}", event.getUserId(), event.getTransactionType());
        } catch (Exception e) {
            log.error("Failed to process wallet event for userId={}: {}", event.getUserId(), e.getMessage(), e);
            throw e;
        }
    }
}
