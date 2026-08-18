package cards.mpay.uapi.api;

import com.fasterxml.jackson.core.type.TypeReference;
import cards.mpay.uapi.http.BaseHttpClient;
import cards.mpay.uapi.api.args.card.*;
import cards.mpay.uapi.model.Page;
import cards.mpay.uapi.model.card.CardInfo;
import cards.mpay.uapi.model.card.CardOperation;
import cards.mpay.uapi.model.card.CardOperationStatus;
import cards.mpay.uapi.model.card.CardProduct;
import cards.mpay.uapi.model.card.CardSensitive;
import cards.mpay.uapi.model.card.CardTransaction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Client for {@code /v1/card/*} endpoints.
 */
public class CardApi {

    private final BaseHttpClient http;

    public CardApi(BaseHttpClient http) {
        this.http = http;
    }

    /**
     * Retrieves the list of available card products.
     *
     * <p>GET /v1/card/products</p>
     */
    public List<CardProduct> getProducts() {
        return http.get("/v1/card/products", null, new TypeReference<>() {
        });
    }

    /**
     * Retrieves the list of available card statuses.
     *
     * <p>GET /v1/card/statuses</p>
     */
    public List<String> getStatuses() {
        return http.get("/v1/card/statuses", null, new TypeReference<>() {
        });
    }

    /**
     * Retrieves the list of cards.
     *
     * <p>GET /v1/card/list</p>
     *
     * @param args card query arguments
     */
    public List<CardInfo> getCards(GetCardsArgs args) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("status", args == null ? null : args.getStatus());
        return http.get("/v1/card/list", params, new TypeReference<>() {
        });
    }

    /**
     * Retrieves the list of cards.
     *
     * <p>GET /v1/card/list</p>
     */
    public List<CardInfo> getCards() {
        return getCards(GetCardsArgs.builder().build());
    }

    /**
     * Retrieves information about a card.
     *
     * <p>GET /v1/card/info</p>
     *
     * @param cardId unique identifier of the card, required
     */
    public CardInfo getCardInfo(String cardId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("card_id", cardId);
        return http.get("/v1/card/info", params, CardInfo.class);
    }

    /**
     * Retrieves sensitive information for a card (PAN, CVV, expiry, PIN).
     *
     * <p>GET /v1/card/sensitive</p>
     *
     * <p><b>Note:</b> the returned data is highly sensitive. Handle it
     * according to PCI-DSS requirements: never log it, and avoid
     * persisting it unless strictly necessary.</p>
     *
     * @param cardId unique identifier of the card, required
     */
    public CardSensitive getCardSensitive(String cardId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("card_id", cardId);
        return http.get("/v1/card/sensitive", params, CardSensitive.class);
    }

    /**
     * Retrieves the transactions for a card.
     *
     * <p>GET /v1/card/transactions</p>
     *
     * @param args card transactions query arguments
     */
    public Page<CardTransaction> getCardTransactions(GetCardTransactionsArgs args) {
        Map<String, Object> params = new LinkedHashMap<>();
        if (args != null) {
            if (args.getCardId() != null) {
                params.put("card_id", args.getCardId());
            }
            if (args.getPage() != null) {
                params.put("page", args.getPage());
            }
            if (args.getLimit() != null) {
                params.put("limit", args.getLimit());
            }
        }
        return http.get("/v1/card/transactions", params, new TypeReference<Page<CardTransaction>>() {
        });
    }

    /**
     * Retrieves all transactions for a card.
     *
     * <p>GET /v1/card/transactions</p>
     *
     * @param cardId unique identifier of the card, required
     */
    public Page<CardTransaction> getCardTransactions(String cardId) {
        return getCardTransactions(GetCardTransactionsArgs.builder().cardId(cardId).build());
    }

    /**
     * Adds or updates a remark for a card.
     *
     * <p>POST /v1/card/remark</p>
     *
     * @param cardId unique identifier of the card, required
     * @param remark remark associated with the card, required
     * @return the updated card information
     */
    public CardInfo remarkCard(String cardId, String remark) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("card_id", cardId);
        body.put("remark", remark);
        return http.post("/v1/card/remark", body, CardInfo.class);
    }

    /**
     * Creates a new card.
     *
     * <p>POST /v1/card/create</p>
     *
     * <p>This initiates a card creation operation. When the returned
     * status is {@code "PROCESSING"}, poll
     * {@link #getCardOperationStatus(String)} with the returned
     * {@code operationId} until it resolves.</p>
     *
     * @param productId ID of the card product to create, required
     */
    public CardOperation createCard(String productId) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("product_id", productId);
        return http.post("/v1/card/create", body, CardOperation.class);
    }

    /**
     * Recharges a card.
     *
     * <p>POST /v1/card/recharge</p>
     *
     * <p>This initiates a card recharge operation. When the returned
     * status is {@code "PROCESSING"}, poll
     * {@link #getCardOperationStatus(String)} with the returned
     * {@code operationId} until it resolves.</p>
     *
     * @param cardId identifier of the card to be recharged, required
     * @param amount recharge amount (up to 2 decimal places), required
     */
    public CardOperation rechargeCard(String cardId, double amount) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("card_id", cardId);
        body.put("amount", amount);
        return http.post("/v1/card/recharge", body, CardOperation.class);
    }

    /**
     * Retrieves the status of a card operation (create/recharge/etc.).
     *
     * <p>GET /v1/card/operation/status</p>
     *
     * @param operationId unique identifier of the operation, as returned by
     *                    {@link #createCard(String)} or {@link #rechargeCard(String, double)}
     */
    public CardOperationStatus getCardOperationStatus(String operationId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("operation_id", operationId);
        return http.get("/v1/card/operation/status", params, CardOperationStatus.class);
    }
}
