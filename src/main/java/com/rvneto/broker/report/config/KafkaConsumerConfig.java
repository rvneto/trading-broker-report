package com.rvneto.broker.report.config;

import com.rvneto.broker.report.dto.AssetMarketDataDTO;
import com.rvneto.broker.report.dto.OrderEventDTO;
import com.rvneto.broker.report.dto.WalletEventDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseConsumerProps(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    // --- Order Events ---

    @Bean
    public ConsumerFactory<String, OrderEventDTO> orderConsumerFactory() {
        JsonDeserializer<OrderEventDTO> deserializer = new JsonDeserializer<>(OrderEventDTO.class, false);
        deserializer.addTrustedPackages("*");
        ErrorHandlingDeserializer<OrderEventDTO> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(deserializer);
        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps("broker-report-api"),
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEventDTO> orderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        return factory;
    }

    // --- Wallet Events ---

    @Bean
    public ConsumerFactory<String, WalletEventDTO> walletConsumerFactory() {
        JsonDeserializer<WalletEventDTO> deserializer = new JsonDeserializer<>(WalletEventDTO.class, false);
        deserializer.addTrustedPackages("*");
        ErrorHandlingDeserializer<WalletEventDTO> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(deserializer);
        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps("broker-report-api"),
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WalletEventDTO> walletKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WalletEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(walletConsumerFactory());
        return factory;
    }

    // --- Asset Market Data ---

    @Bean
    public ConsumerFactory<String, AssetMarketDataDTO> assetMarketDataConsumerFactory() {
        JsonDeserializer<AssetMarketDataDTO> deserializer = new JsonDeserializer<>(AssetMarketDataDTO.class, false);
        deserializer.addTrustedPackages("*");
        ErrorHandlingDeserializer<AssetMarketDataDTO> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(deserializer);
        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps("broker-report-api"),
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AssetMarketDataDTO> assetMarketDataKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AssetMarketDataDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(assetMarketDataConsumerFactory());
        return factory;
    }
}
