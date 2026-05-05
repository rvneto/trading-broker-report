package com.rvneto.broker.report.controller;

import com.rvneto.broker.report.document.AssetPriceHistory;
import com.rvneto.broker.report.document.OrderSnapshot;
import com.rvneto.broker.report.document.WalletSnapshot;
import com.rvneto.broker.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // --- Order History ---

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderSnapshot>> getOrderHistory(@PathVariable String userId) {
        return ResponseEntity.ok(reportService.getOrderHistory(userId));
    }

    @GetMapping("/{userId}/orders/status/{status}")
    public ResponseEntity<List<OrderSnapshot>> getOrderHistoryByStatus(
            @PathVariable String userId,
            @PathVariable String status) {
        return ResponseEntity.ok(reportService.getOrderHistoryByStatus(userId, status));
    }

    @GetMapping("/{userId}/orders/period")
    public ResponseEntity<List<OrderSnapshot>> getOrderHistoryByPeriod(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(reportService.getOrderHistoryByPeriod(userId, from, to));
    }

    @GetMapping("/{userId}/orders/ticker/{ticker}")
    public ResponseEntity<List<OrderSnapshot>> getOrderHistoryByTicker(
            @PathVariable String userId,
            @PathVariable String ticker) {
        return ResponseEntity.ok(reportService.getOrderHistoryByTicker(userId, ticker));
    }

    // --- Financial Statement ---

    @GetMapping("/{userId}/statement")
    public ResponseEntity<List<WalletSnapshot>> getStatement(@PathVariable String userId) {
        return ResponseEntity.ok(reportService.getStatement(userId));
    }

    @GetMapping("/{userId}/statement/period")
    public ResponseEntity<List<WalletSnapshot>> getStatementByPeriod(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(reportService.getStatementByPeriod(userId, from, to));
    }

    @GetMapping("/{userId}/statement/type/{transactionType}")
    public ResponseEntity<List<WalletSnapshot>> getStatementByType(
            @PathVariable String userId,
            @PathVariable String transactionType) {
        return ResponseEntity.ok(reportService.getStatementByType(userId, transactionType));
    }

    // --- Asset Price History ---

    @GetMapping("/assets/{ticker}/history")
    public ResponseEntity<List<AssetPriceHistory>> getAssetPriceHistory(@PathVariable String ticker) {
        return ResponseEntity.ok(reportService.getAssetPriceHistory(ticker));
    }

    @GetMapping("/assets/{ticker}/history/period")
    public ResponseEntity<List<AssetPriceHistory>> getAssetPriceHistoryByPeriod(
            @PathVariable String ticker,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(reportService.getAssetPriceHistoryByPeriod(ticker, from, to));
    }
}
