package com.rvneto.broker.report.repository;

import com.rvneto.broker.report.document.OrderSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderSnapshotRepository extends MongoRepository<OrderSnapshot, String> {

    List<OrderSnapshot> findByUserIdOrderByEventTimestampDesc(String userId);

    List<OrderSnapshot> findByUserIdAndStatusOrderByEventTimestampDesc(String userId, String status);

    List<OrderSnapshot> findByUserIdAndEventTimestampBetweenOrderByEventTimestampDesc(
            String userId, LocalDateTime from, LocalDateTime to);

    List<OrderSnapshot> findByUserIdAndTickerOrderByEventTimestampDesc(String userId, String ticker);
}
