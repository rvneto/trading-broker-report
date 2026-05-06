# Broker Report API

Event-driven report service for the **My Broker B3** ecosystem. Consumes Kafka events from across the platform and exposes consolidated reports via REST — without ever directly accessing any other service's database.

> This service is part of a series of articles documenting the **My Broker B3** ecosystem.
> Follow the full series on [dev.to/rvneto](https://dev.to/rvneto).

---

## Architecture & Flow
[broker-order-api]
│── order-events-v1 ─────────────────────────┐
│
[broker-wallet-api]                               ▼
│── wallet-events-v1 ───────────► [broker-report-api :8087]
│
[broker-market-data-api]                          │── MongoDB (broker-report-db)
│── assets-market-data-v1 ──────►             │
REST endpoints
GET /api/v1/reports/...
▲
[broker-gateway-api :8080]

The `broker-report-api` is a **pure consumer** — it never produces events, never calls REST on other services, and never touches foreign databases. Everything it knows comes from the Kafka topics it subscribes to.

---

## Tech Stack

| Technology | Usage |
| :--- | :--- |
| **Java 21** + **Spring Boot 3.5.11** | Service core |
| **Spring Kafka** | Consuming 3 event topics |
| **MongoDB** | Flexible document persistence for reports |
| **SpringDoc OpenAPI** | Swagger UI documentation |

---

## Consumed Topics

| Topic | Producer | Data |
| :--- | :--- | :--- |
| `order-events-v1` | broker-order-api | Order lifecycle events (PENDING, FILLED, REJECTED) |
| `wallet-events-v1` | broker-wallet-api | Financial movements (RESERVE, SETTLEMENT, REFUND, DEPOSIT, WITHDRAWAL) |
| `assets-market-data-v1` | broker-market-data-api | Asset price quotes |

---

## MongoDB Collections

| Collection | Description | Key Indexes |
| :--- | :--- | :--- |
| `order_snapshots` | One document per order event | userId, orderId, status, eventTimestamp |
| `wallet_snapshots` | One document per wallet transaction | userId, eventTimestamp |
| `asset_price_history` | Asset price over time | ticker, timestamp |

---

## REST API

| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| GET | `/api/v1/reports/{userId}/orders` | Full order history | JWT |
| GET | `/api/v1/reports/{userId}/orders/status/{status}` | Orders filtered by status | JWT |
| GET | `/api/v1/reports/{userId}/orders/period` | Orders in date range | JWT |
| GET | `/api/v1/reports/{userId}/orders/ticker/{ticker}` | Orders for a specific asset | JWT |
| GET | `/api/v1/reports/{userId}/statement` | Full financial statement | JWT |
| GET | `/api/v1/reports/{userId}/statement/period` | Statement in date range | JWT |
| GET | `/api/v1/reports/{userId}/statement/type/{type}` | Statement by transaction type | JWT |
| GET | `/api/v1/reports/assets/{ticker}/history` | Full price history for asset | JWT |
| GET | `/api/v1/reports/assets/{ticker}/history/period` | Price history in date range | JWT |

Swagger UI: http://localhost:8087/swagger-ui.html

---

## Environment Variables

| Variable | Description | Default |
| :--- | :--- | :--- |
| `MONGO_URI` | MongoDB connection URI | `mongodb://root:password@localhost:27017/broker_report_db?authSource=admin` |
| `KAFKA_HOST` | Kafka bootstrap server host | `localhost` |

---

## Prerequisites

- Kafka running with topics `order-events-v1`, `wallet-events-v1`, `assets-market-data-v1`
- MongoDB running on port `27017`
- `broker-wallet-api` publishing `wallet-events-v1` (added in this iteration)

---

## Running with Docker

docker build -t broker-report-api .

docker run --network finance-network \
  -p 8087:8087 \
  -e MONGO_URI=mongodb://root:password@mongo:27017/broker_report_db?authSource=admin \
  -e KAFKA_HOST=kafka \
  broker-report-api

---

## Health Check

- Endpoint: GET /actuator/health
- Port: 8087