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
@Document(collection = "asset_price_history")
public class AssetPriceHistory {

    @Id
    private String id;

    @Indexed
    private String ticker;

    private String name;
    private BigDecimal price;

    @Indexed
    private LocalDateTime timestamp;

    private LocalDateTime savedAt;
}
