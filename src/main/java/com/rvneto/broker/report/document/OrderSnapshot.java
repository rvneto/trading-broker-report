package com.rvneto.broker.report.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_snapshots")
public class OrderSnapshot {

    @Id
    private String id;

    @Indexed
    private Long orderId;

    @Indexed
    private String userId;

    private String ticker;
    private String side;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal executedPrice;

    @Indexed
    private String status;

    private LocalDateTime eventTimestamp;
    private LocalDateTime savedAt;
}
