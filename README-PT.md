# Broker Report API

Serviço de relatórios orientado a eventos do ecossistema **My Broker B3**. Consome eventos Kafka de toda a plataforma e expõe relatórios consolidados via REST — sem nunca acessar diretamente o banco de dados de nenhum outro serviço.

> Este serviço faz parte de uma série de artigos documentando o ecossistema **My Broker B3**.
> Acompanhe a série completa em [dev.to/rvneto](https://dev.to/rvneto).

---

## Arquitetura e Fluxo
[broker-order-api]
│── order-events-v1 ─────────────────────────┐
│
[broker-wallet-api]                               ▼
│── wallet-events-v1 ───────────► [broker-report-api :8087]
│
[broker-market-data-api]                          │── MongoDB (broker-report-db)
│── assets-market-data-v1 ──────►             │
endpoints REST
GET /api/v1/reports/...
▲
[broker-gateway-api :8080]

O `broker-report-api` é um consumer puro — nunca produz eventos, nunca chama REST de outros serviços e nunca acessa bancos alheios. Tudo que ele sabe vem dos tópicos Kafka que assina.

---

## Stack Tecnológica

| Tecnologia | Uso |
| :--- | :--- |
| **Java 21** + **Spring Boot 3.5.11** | Core do serviço |
| **Spring Kafka** | Consumo de 3 tópicos de eventos |
| **MongoDB** | Persistência documental flexível para relatórios |
| **SpringDoc OpenAPI** | Documentação via Swagger UI |

---

## Tópicos Consumidos

| Tópico | Produtor | Dados |
| :--- | :--- | :--- |
| `order-events-v1` | broker-order-api | Ciclo de vida das ordens (PENDING, FILLED, REJECTED) |
| `wallet-events-v1` | broker-wallet-api | Movimentações financeiras (RESERVE, SETTLEMENT, REFUND, DEPOSIT, WITHDRAWAL) |
| `assets-market-data-v1` | broker-market-data-api | Cotações dos ativos |

---

## Collections MongoDB

| Collection | Descrição | Índices principais |
| :--- | :--- | :--- |
| `order_snapshots` | Um documento por evento de ordem | userId, orderId, status, eventTimestamp |
| `wallet_snapshots` | Um documento por transação financeira | userId, eventTimestamp |
| `asset_price_history` | Histórico de preço dos ativos | ticker, timestamp |

---

## API REST

| Método | Endpoint | Descrição | Auth |
| :--- | :--- | :--- | :--- |
| GET | `/api/v1/reports/{userId}/orders` | Histórico completo de ordens | JWT |
| GET | `/api/v1/reports/{userId}/orders/status/{status}` | Ordens filtradas por status | JWT |
| GET | `/api/v1/reports/{userId}/orders/period` | Ordens em intervalo de datas | JWT |
| GET | `/api/v1/reports/{userId}/orders/ticker/{ticker}` | Ordens de um ativo específico | JWT |
| GET | `/api/v1/reports/{userId}/statement` | Extrato financeiro completo | JWT |
| GET | `/api/v1/reports/{userId}/statement/period` | Extrato em intervalo de datas | JWT |
| GET | `/api/v1/reports/{userId}/statement/type/{type}` | Extrato por tipo de transação | JWT |
| GET | `/api/v1/reports/assets/{ticker}/history` | Histórico completo de preços do ativo | JWT |
| GET | `/api/v1/reports/assets/{ticker}/history/period` | Histórico de preços em intervalo | JWT |

Swagger UI: http://localhost:8087/swagger-ui.html

---

## Variáveis de Ambiente

| Variável | Descrição | Padrão |
| :--- | :--- | :--- |
| `MONGO_URI` | URI de conexão com o MongoDB | `mongodb://root:password@localhost:27017/broker_report_db?authSource=admin` |
| `KAFKA_HOST` | Host do servidor Kafka | `localhost` |

---

## Pré-requisitos

- Kafka rodando com os tópicos `order-events-v1`, `wallet-events-v1`, `assets-market-data-v1`
- MongoDB rodando na porta `27017`
- `broker-wallet-api` publicando `wallet-events-v1` (adicionado nesta iteração)

---

## Rodando com Docker

docker build -t broker-report-api .

docker run --network finance-network \
  -p 8087:8087 \
  -e MONGO_URI=mongodb://root:password@mongo:27017/broker_report_db?authSource=admin \
  -e KAFKA_HOST=kafka \
  broker-report-api

---

## Health Check

- Endpoint: GET /actuator/health
- Porta: 8087