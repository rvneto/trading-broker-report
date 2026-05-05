package com.rvneto.broker.report.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Broker Report API")
                        .version("1.0.0")
                        .description("Report service for My Broker B3 — consumes Kafka events and exposes report endpoints. " +
                                "Data is sourced exclusively from order-events-v1, wallet-events-v1 and assets-market-data-v1 topics."));
    }
}
