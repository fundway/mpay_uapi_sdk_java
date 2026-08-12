package cards.mpay.uapi.examples;

import cards.mpay.uapi.MpayUapiClient;
import cards.mpay.uapi.exception.MpayApiException;
import cards.mpay.uapi.model.Page;
import cards.mpay.uapi.model.card.CardInfo;
import cards.mpay.uapi.model.card.CardOperation;
import cards.mpay.uapi.model.card.CardOperationStatus;
import cards.mpay.uapi.model.card.CardProduct;
import cards.mpay.uapi.model.card.CardSensitive;
import cards.mpay.uapi.model.card.CardTransaction;

import java.util.List;

/**
 * Quickstart example for the card endpoints.
 *
 * <p>Run with:</p>
 * <pre>
 * ACCESS_KEY=xxx SECRET_KEY=xxx MPAY_BASE_URL=<a href="https://uapidev.mpay.cards">...</a> \
 *     java -cp target/uapi-sdk-1.0.0.jar com.mpay.uapi.examples.CardExample
 * </pre>
 */
public final class CardExample {

    private static final String ACCESS_KEY = System.getenv().getOrDefault("ACCESS_KEY", "ak_demo_apikey");
    private static final String SECRET_KEY = System.getenv().getOrDefault("SECRET_KEY", "sk_demo_apisecret");
    private static final String BASE_URL =
            System.getenv().getOrDefault("MPAY_BASE_URL", "https://uapidev.mpay.cards");

    private CardExample() {
    }

    private static MpayUapiClient newClient() {
        return MpayUapiClient.builder()
                .accessKey(ACCESS_KEY)
                .secretKey(SECRET_KEY)
                .baseUrl(BASE_URL)
                .build();
    }

    /** Retrieves the list of available card products. */
    public static List<CardProduct> getProducts() {
        try {
            List<CardProduct> products = newClient().card().getProducts();
            System.out.println(">>>>> Card products:\n" + products);
            return products;
        } catch (MpayApiException e) {
            System.out.println("getProducts error: " + e);
            return null;
        }
    }

    /**
     * Retrieves the list of available card statuses.
     */
    public static void getStatuses() {
        try {
            List<String> statuses = newClient().card().getStatuses();
            System.out.println(">>>>> Card statuses:\n" + statuses);
        } catch (MpayApiException e) {
            System.out.println("getStatuses error: " + e);
        }
    }

    /** Retrieves the list of cards. */
    public static List<CardInfo> getCards() {
        try {
            List<CardInfo> cards = newClient().card().getCards(null);
            System.out.println(">>>>> Cards:\n" + cards);
            return cards;
        } catch (MpayApiException e) {
            System.out.println("getCards error: " + e);
            return null;
        }
    }

    /**
     * Retrieves information about a card.
     */
    public static void getCardInfo(String cardId) {
        try {
            CardInfo info = newClient().card().getCardInfo(cardId);
            System.out.println(">>>>> Card info:\n" + info);
        } catch (MpayApiException e) {
            System.out.println("getCardInfo error: " + e);
        }
    }

    /**
     * Retrieves sensitive information for a card.
     */
    public static void getCardSensitive(String cardId) {
        try {
            CardSensitive sensitive = newClient().card().getCardSensitive(cardId);
            System.out.println(">>>>> Card sensitive info:\n" + sensitive);
        } catch (MpayApiException e) {
            System.out.println("getCardSensitive error: " + e);
        }
    }

    /**
     * Retrieves the transactions for a card.
     */
    public static void getCardTransactions(String cardId) {
        try {
            Page<CardTransaction> transactions = newClient().card().getCardTransactions(cardId, 1, 20);
            System.out.println(">>>>> Card transactions:\n" + transactions);
        } catch (MpayApiException e) {
            System.out.println("getCardTransactions error: " + e);
        }
    }

    /**
     * Adds or updates a remark for a card.
     */
    public static void remarkCard(String cardId) {
        try {
            CardInfo info = newClient().card().remarkCard(cardId, "Business card");
            System.out.println(">>>>> Updated card info:\n" + info);
        } catch (MpayApiException e) {
            System.out.println("remarkCard error: " + e);
        }
    }

    /**
     * Creates a new card and waits for the creation operation to complete.
     */
    public static void createCard(String productId) {
        MpayUapiClient client = newClient();
        try {
            CardOperation operation = client.card().createCard(productId);
            System.out.println(">>>>> Create card operation submitted:\n" + operation);
            CardOperationStatus result = client.waitForCardOperation(operation.getOperationId());
            System.out.println(">>>>> Create card operation result:\n" + result);
        } catch (MpayApiException e) {
            System.out.println("createCard error: " + e);
        }
    }

    /**
     * Recharges a card and waits for the recharge operation to complete.
     */
    public static void rechargeCard(String cardId, double amount) {
        MpayUapiClient client = newClient();
        try {
            CardOperation operation = client.card().rechargeCard(cardId, amount);
            System.out.println(">>>>> Recharge card operation submitted:\n" + operation);
            CardOperationStatus result = client.waitForCardOperation(operation.getOperationId());
            System.out.println(">>>>> Recharge card operation result:\n" + result);
        } catch (MpayApiException e) {
            System.out.println("rechargeCard error: " + e);
        }
    }

    public static void main(String[] args) {
        List<CardProduct> products = getProducts();
        getStatuses();
        List<CardInfo> cards = getCards();

        if (cards != null && !cards.isEmpty()) {
            String cardId = cards.get(0).getCardId();
            getCardInfo(cardId);
            getCardSensitive(cardId);
            getCardTransactions(cardId);
            remarkCard(cardId);
            rechargeCard(cardId, 25.0);
        }

        if (products != null && !products.isEmpty()) {
            createCard(products.get(0).getProductId());
        }
    }
}
