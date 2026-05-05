package com.rvneto.broker.report.service;

import com.rvneto.broker.report.document.AssetPriceHistory;
import com.rvneto.broker.report.document.OrderSnapshot;
import com.rvneto.broker.report.document.WalletSnapshot;
import com.rvneto.broker.report.repository.AssetPriceHistoryRepository;
import com.rvneto.broker.report.repository.OrderSnapshotRepository;
import com.rvneto.broker.report.repository.WalletSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderSnapshotRepository orderSnapshotRepository;
    private final WalletSnapshotRepository walletSnapshotRepository;
    private final AssetPriceHistoryRepository assetPriceHistoryRepository;

    public List<OrderSnapshot> getOrderHistory(String userId) {
        log.info("Fetching order history for userId={}", userId);
        return orderSnapshotRepository.findByUserIdOrderByEventTimestampDesc(userId);
    }

    public List<OrderSnapshot> getOrderHistoryByStatus(String userId, String status) {
        log.info("Fetching order history for userId={} status={}", userId, status);
        return orderSnapshotRepository.findByUserIdAndStatusOrderByEventTimestampDesc(userId, status.toUpperCase());
    }

    public List<OrderSnapshot> getOrderHistoryByPeriod(String userId, LocalDateTime from, LocalDateTime to) {
        log.info("Fetching order history for userId={} from={} to={}", userId, from, to);
        return orderSnapshotRepository.findByUserIdAndEventTimestampBetweenOrderByEventTimestampDesc(userId, from, to);
    }

    public List<OrderSnapshot> getOrderHistoryByTicker(String userId, String ticker) {
        log.info("Fetching order history for userId={} ticker={}", userId, ticker);
        return orderSnapshotRepository.findByUserIdAndTickerOrderByEventTimestampDesc(userId, ticker.toUpperCase());
    }

    public List<WalletSnapshot> getStatement(String userId) {
        log.info("Fetching financial statement for userId={}", userId);
        return walletSnapshotRepository.findByUserIdOrderByEventTimestampDesc(userId);
    }

    public List<WalletSnapshot> getStatementByPeriod(String userId, LocalDateTime from, LocalDateTime to) {
        log.info("Fetching financial statement for userId={} from={} to={}", userId, from, to);
        return walletSnapshotRepository.findByUserIdAndEventTimestampBetweenOrderByEventTimestampDesc(userId, from, to);
    }

    public List<WalletSnapshot> getStatementByType(String userId, String transactionType) {
        log.info("Fetching financial statement for userId={} type={}", userId, transactionType);
        return walletSnapshotRepository.findByUserIdAndTransactionTypeOrderByEventTimestampDesc(userId, transactionType.toUpperCase());
    }

    public List<AssetPriceHistory> getAssetPriceHistory(String ticker) {
        log.info("Fetching price history for ticker={}", ticker);
        return assetPriceHistoryRepository.findByTickerOrderByTimestampDesc(ticker.toUpperCase());
    }

    public List<AssetPriceHistory> getAssetPriceHistoryByPeriod(String ticker, LocalDateTime from, LocalDateTime to) {
        log.info("Fetching price history for ticker={} from={} to={}", ticker, from, to);
        return assetPriceHistoryRepository.findByTickerAndTimestampBetweenOrderByTimestampDesc(ticker.toUpperCase(), from, to);
    }
}
