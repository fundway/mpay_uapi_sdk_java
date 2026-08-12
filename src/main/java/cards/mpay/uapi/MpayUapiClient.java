package cards.mpay.uapi;

import cards.mpay.uapi.api.CardApi;
import cards.mpay.uapi.api.HolderApi;
import cards.mpay.uapi.api.WalletApi;
import cards.mpay.uapi.exception.MpayException;
import cards.mpay.uapi.http.BaseHttpClient;
import cards.mpay.uapi.model.card.CardOperationStatus;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Set;

/**
 * Top level client for the mPay uAPI.
 *
 * <p>Example:</p>
 * <pre>{@code
 * MpayUapiClient client = MpayUapiClient.builder()
 *         .accessKey("ak_xxx")
 *         .secretKey("sk_xxx")
 *         .build();
 *
 * client.wallet().getWalletBalance();
 * client.holder().getHolderInfo();
 * client.card().getCards(null);
 * }</pre>
 */
public class MpayUapiClient {

    private static final Set<String> TERMINAL_OPERATION_STATUSES =
            Set.of("SUCCESS", "FAILED", "FAILURE", "CANCELLED", "REJECTED");

    private final BaseHttpClient http;
    private final WalletApi wallet;
    private final HolderApi holder;
    private final CardApi card;

    private MpayUapiClient(Builder builder) {
        this.http = new BaseHttpClient(
                builder.accessKey,
                builder.secretKey,
                builder.baseUrl,
                builder.timeout,
                builder.httpClient,
                builder.userAgent);
        this.wallet = new WalletApi(http);
        this.holder = new HolderApi(http);
        this.card = new CardApi(http);
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Client for {@code /v1/wallet/*} and {@code /v1/deposit/*} endpoints. */
    public WalletApi wallet() {
        return wallet;
    }

    /** Client for {@code /v1/holder/*} endpoints. */
    public HolderApi holder() {
        return holder;
    }

    /** Client for {@code /v1/card/*} endpoints. */
    public CardApi card() {
        return card;
    }

    /**
     * Polls {@code GET /v1/card/operation/status} until the operation
     * reaches a terminal state.
     *
     * <p>Convenience helper for asynchronous operations such as
     * {@link CardApi#createCard(String)} and
     * {@link CardApi#rechargeCard(String, double)}, which return
     * immediately with {@code status == "PROCESSING"}.</p>
     *
     * @param operationId the operation ID returned by the triggering call
     * @param interval    time to wait between polling attempts
     * @param maxAttempts maximum number of polling attempts before giving up
     * @return the final operation status payload once it leaves {@code PROCESSING}
     * @throws MpayException if the operation does not reach a terminal state within {@code maxAttempts} polls
     */
    public CardOperationStatus waitForCardOperation(String operationId, Duration interval, int maxAttempts) {
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            CardOperationStatus result = card.getCardOperationStatus(operationId);
            String status = result.getStatus() == null ? "" : result.getStatus().toUpperCase();
            if (TERMINAL_OPERATION_STATUSES.contains(status)) {
                return result;
            }
            if (attempt < maxAttempts - 1) {
                sleep(interval);
            }
        }
        throw new MpayException(
                "Operation " + operationId + " did not reach a terminal state after " + maxAttempts + " attempts");
    }

    /** Polls with the default interval (2 seconds) and up to 30 attempts. */
    public CardOperationStatus waitForCardOperation(String operationId) {
        return waitForCardOperation(operationId, Duration.ofSeconds(2), 30);
    }

    private static void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MpayException("Interrupted while waiting for card operation", e);
        }
    }

    /**
     * Builder for {@link MpayUapiClient}.
     */
    public static final class Builder {
        private String accessKey;
        private String secretKey;
        private String baseUrl = BaseHttpClient.DEFAULT_BASE_URL;
        private Duration timeout = BaseHttpClient.DEFAULT_TIMEOUT;
        private HttpClient httpClient;
        private String userAgent = BaseHttpClient.DEFAULT_USER_AGENT;

        private Builder() {
        }

        /** Your mPay API access key (public identifier). Required. */
        public Builder accessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        /**
         * Your mPay API secret key, used to HMAC-sign requests. Required.
         * Keep this value confidential and never expose it client-side.
         */
        public Builder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        /** Overrides the API base URL (useful for sandbox/staging environments). */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /** Per-request timeout. Defaults to 30 seconds. */
        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /** Optionally provide a pre-configured {@link HttpClient} (e.g. to customize proxies, TLS settings). */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /** Overrides the {@code User-Agent} header sent with every request. */
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public MpayUapiClient build() {
            return new MpayUapiClient(this);
        }
    }
}
