package com.rvneto.broker.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletEventDTO {

    private String eventId;
    private String userId;
    private String transactionType;
    private BigDecimal amount;
    private String ticker;
    private Long orderId;
    private BigDecimal balanceAfter;
    private BigDecimal blockedBalanceAfter;
    private LocalDateTime eventTimestamp;
}
