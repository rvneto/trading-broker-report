package com.rvneto.broker.report.repository;

import com.rvneto.broker.report.document.WalletSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WalletSnapshotRepository extends MongoRepository<WalletSnapshot, String> {

    List<WalletSnapshot> findByUserIdOrderByEventTimestampDesc(String userId);

    List<WalletSnapshot> findByUserIdAndEventTimestampBetweenOrderByEventTimestampDesc(
            String userId, LocalDateTime from, LocalDateTime to);

    List<WalletSnapshot> findByUserIdAndTransactionTypeOrderByEventTimestampDesc(
            String userId, String transactionType);
}
