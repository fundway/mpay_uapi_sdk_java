package cards.mpay.uapi.examples;

import cards.mpay.uapi.MpayUapiClient;
import cards.mpay.uapi.exception.MpayApiException;
import cards.mpay.uapi.model.holder.HolderInfo;

/**
 * Quickstart example for the cardholder endpoints.
 *
 * <p>Run with:</p>
 * <pre>
 * ACCESS_KEY=xxx SECRET_KEY=xxx MPAY_BASE_URL=<a href="https://uapidev.mpay.cards">...</a> \
 *     java -cp target/uapi-sdk-1.0.0.jar com.mpay.uapi.examples.HolderExample
 * </pre>
 */
public final class HolderExample {

    private static final String ACCESS_KEY = System.getenv().getOrDefault("ACCESS_KEY", "ak_demo_apikey");
    private static final String SECRET_KEY = System.getenv().getOrDefault("SECRET_KEY", "sk_demo_apisecret");
    private static final String BASE_URL =
            System.getenv().getOrDefault("MPAY_BASE_URL", "https://uapidev.mpay.cards");

    private HolderExample() {
    }

    /** Retrieves cardholder information. */
    public static void getHolderInfo() {
        MpayUapiClient client = MpayUapiClient.builder()
                .accessKey(ACCESS_KEY)
                .secretKey(SECRET_KEY)
                .baseUrl(BASE_URL)
                .build();
        try {
            HolderInfo info = client.holder().getHolderInfo();
            System.out.println(">>>>> Cardholder information:\n" + info);
        } catch (MpayApiException e) {
            System.out.println("getHolderInfo error: " + e);
        }
    }

    /** Updates the cardholder information. */
    public static void setHolderInfo() {
        MpayUapiClient client = MpayUapiClient.builder()
                .accessKey(ACCESS_KEY)
                .secretKey(SECRET_KEY)
                .baseUrl(BASE_URL)
                .build();
        try {
            String firstName = "James";
            String lastName = "Brown";
            HolderInfo info = client.holder().setHolderInfo(firstName, lastName);
            System.out.println(">>>>> Latest cardholder information:\n" + info);
        } catch (MpayApiException e) {
            System.out.println("setHolderInfo error: " + e);
        }
    }

    public static void main(String[] args) {
        getHolderInfo();
        setHolderInfo();
    }
}
