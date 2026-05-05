package com.rvneto.broker.report.repository;

import com.rvneto.broker.report.document.AssetPriceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssetPriceHistoryRepository extends MongoRepository<AssetPriceHistory, String> {

    List<AssetPriceHistory> findByTickerOrderByTimestampDesc(String ticker);

    List<AssetPriceHistory> findByTickerAndTimestampBetweenOrderByTimestampDesc(
            String ticker, LocalDateTime from, LocalDateTime to);

    Optional<AssetPriceHistory> findTopByTickerOrderByTimestampDesc(String ticker);
}
