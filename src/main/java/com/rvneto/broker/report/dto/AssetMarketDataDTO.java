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
public class AssetMarketDataDTO {

    private String ticker;
    private String name;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
