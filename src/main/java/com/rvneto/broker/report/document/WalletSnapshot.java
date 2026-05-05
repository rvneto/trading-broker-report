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
@Document(collection = "wallet_snapshots")
public class WalletSnapshot {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String transactionType;
    private BigDecimal amount;
    private String ticker;
    private Long orderId;
    private BigDecimal balanceAfter;
    private BigDecimal blockedBalanceAfter;

    @Indexed
    private LocalDateTime eventTimestamp;

    private LocalDateTime savedAt;
}
