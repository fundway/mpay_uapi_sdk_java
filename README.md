# mPay uAPI SDK for Java

Official Java SDK for the Mpay Credit Card User API, covering wallet, cardholder, and card
management endpoints.

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.mpay</groupId>
    <artifactId>uapi-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

Requires Java 11 or later.

## Quickstart

```java
import cards.mpay.uapi.MpayUapiClient;
import holder.model.cards.mpay.uapi.HolderInfo;
import wallet.model.cards.mpay.uapi.WalletBalance;
import card.model.cards.mpay.uapi.CardInfo;

MpayUapiClient client = MpayUapiClient.builder()
        .accessKey("ak_xxx")
        .secretKey("sk_xxx")
        .build();

WalletBalance balance = client.wallet().getWalletBalance();
HolderInfo holder = client.holder().getHolderInfo();
List<CardInfo> cards = client.card().getCards(null);
```

Optional builder settings:

```java
MpayUapiClient client = MpayUapiClient.builder()
        .accessKey("ak_xxx")
        .secretKey("sk_xxx")
        .baseUrl("https://uapidev.mpay.cards") // sandbox/staging override
        .timeout(Duration.ofSeconds(15))
        .userAgent("my-app/1.0")
        .build();
```

## Package layout

- `cards.mpay.uapi.MpayUapiClient` -- top level SDK entry point.
- `com.mpay.uapi.auth` -- HMAC-SHA256 request signing (`HmacAuth`, `SignatureUtil`).
- `com.mpay.uapi.http` -- low level HTTP transport (`BaseHttpClient`).
- `com.mpay.uapi.exception` -- SDK exception types.
- `com.mpay.uapi.api` -- resource clients: `WalletApi`, `HolderApi`, `CardApi`.
- `com.mpay.uapi.model` -- response POJOs, organized by resource
  (`model.wallet`, `model.holder`, `model.card`).
- `com.mpay.uapi.examples` -- runnable quickstart examples.

## Resource clients

### Wallet -- `client.wallet()`

| Method | Endpoint |
| --- | --- |
| `getWalletBalance()` | `GET /v1/wallet/balance` |
| `getWalletTransactions(direction, page, limit)` | `GET /v1/wallet/transactions` |
| `getDepositChains()` | `GET /v1/deposit/chains` |
| `getDepositOptions(groupBy)` | `GET /v1/deposit/options` |
| `getDepositAddress(chainId)` | `GET /v1/deposit/address` |
| `getDepositTransactions(chainId, page, limit)` | `GET /v1/deposit/transactions` |

### Holder -- `client.holder()`

| Method | Endpoint |
| --- | --- |
| `getHolderInfo()` | `GET /v1/holder/info` |
| `setHolderInfo(firstName, lastName)` | `POST /v1/holder/set` |

### Card -- `client.card()`

| Method | Endpoint |
| --- | --- |
| `getProducts()` | `GET /v1/card/products` |
| `getStatuses()` | `GET /v1/card/statuses` |
| `getCards(status)` | `GET /v1/card/list` |
| `getCardInfo(cardId)` | `GET /v1/card/info` |
| `getCardSensitive(cardId)` | `GET /v1/card/sensitive` |
| `getCardTransactions(cardId, page, limit)` | `GET /v1/card/transactions` |
| `remarkCard(cardId, remark)` | `POST /v1/card/remark` |
| `createCard(productId)` | `POST /v1/card/create` |
| `rechargeCard(cardId, amount)` | `POST /v1/card/recharge` |
| `getCardOperationStatus(operationId)` | `GET /v1/card/operation/status` |

`createCard` and `rechargeCard` are asynchronous: they return immediately
with `status == "PROCESSING"`. Use `MpayUapiClient.waitForCardOperation`
to poll until the operation reaches a terminal state:

```java
CardOperation operation = client.card().createCard(productId);
CardOperationStatus result = client.waitForCardOperation(operation.getOperationId());
```

## Authentication

Every request is signed with HMAC-SHA256 using your `secretKey`. The SDK
computes and attaches the `X-Api-Key`, `X-Timestamp`, `X-Nonce`, and
`X-Signature` headers automatically -- there is nothing else to configure.

See `auth.cards.mpay.uapi.SignatureUtil` for the exact canonicalization and
signing rules if you need to verify signatures independently.

## Error handling

All SDK-specific errors extend `exception.cards.mpay.uapi.MpayException`:

- `MpayConfigException` -- the client is misconfigured (missing credentials, etc.).
- `MpayApiException` -- the server returned a business/API-level error.
  Exposes `getCode()`, `getHttpStatus()`, and `getResponse()`.
- `MpaySignatureException` -- request signing failed.
- `MpayNetworkException` -- a network-level error occurred.

```java
try {
    client.holder().getHolderInfo();
} catch (MpayApiException e) {
    System.out.println("API error: " + e);
}
```

## Examples

Runnable examples are provided under `com.mpay.uapi.examples`:

- `WalletExample` -- wallet balance, transactions, and deposit endpoints.
- `HolderExample` -- retrieving and updating cardholder information.
- `CardExample` -- listing products/cards, card info, and the
  create/recharge async operation flow.

```
ACCESS_KEY=xxx SECRET_KEY=xxx MPAY_BASE_URL=https://uapidev.mpay.cards \
    java -cp target/uapi-sdk-1.0.0.jar examples.cards.mpay.uapi.HolderExample
```

## Building from source

```
mvn clean package
```
