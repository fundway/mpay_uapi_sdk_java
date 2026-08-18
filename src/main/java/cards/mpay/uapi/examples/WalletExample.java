package cards.mpay.uapi.examples;

import cards.mpay.uapi.MpayUapiClient;
import cards.mpay.uapi.exception.MpayApiException;
import cards.mpay.uapi.api.args.wallet.*;
import cards.mpay.uapi.model.Page;
import cards.mpay.uapi.model.wallet.Chain;
import cards.mpay.uapi.model.wallet.DepositAddress;
import cards.mpay.uapi.model.wallet.DepositOption;
import cards.mpay.uapi.model.wallet.DepositTransaction;
import cards.mpay.uapi.model.wallet.WalletBalance;
import cards.mpay.uapi.model.wallet.WalletTransaction;

import java.util.List;
import java.util.Map;

/**
 * Quickstart example for the wallet and deposit endpoints.
 *
 * <p>Run with:</p>
 * <pre>
 * ACCESS_KEY=xxx SECRET_KEY=xxx MPAY_BASE_URL=<a href="https://uapidev.mpay.cards">...</a> \
 *     java -cp target/uapi-sdk-1.0.0.jar com.mpay.uapi.examples.WalletExample
 * </pre>
 */
public final class WalletExample {

    private static final String ACCESS_KEY = System.getenv().getOrDefault("ACCESS_KEY", "ak_demo_apikey");
    private static final String SECRET_KEY = System.getenv().getOrDefault("SECRET_KEY", "sk_demo_apisecret");
    private static final String BASE_URL =
            System.getenv().getOrDefault("MPAY_BASE_URL", "https://uapidev.mpay.cards");

    private WalletExample() {
    }

    private static MpayUapiClient newClient() {
        return MpayUapiClient.builder()
                .accessKey(ACCESS_KEY)
                .secretKey(SECRET_KEY)
                .baseUrl(BASE_URL)
                .build();
    }

    /** Gets the user's wallet balance. */
    public static void getWalletBalance() {
        try {
            WalletBalance balance = newClient().wallet().getWalletBalance();
            System.out.println(">>>>> Wallet balance:\n" + balance);
        } catch (MpayApiException e) {
            System.out.println("getWalletBalance error: " + e);
        }
    }

    /** Gets the user's wallet transaction records. */
    public static void getWalletTransactions() {
        try {
            GetWalletTransactionsArgs args = GetWalletTransactionsArgs.builder()
                    .page(1)
                    .limit(20)
                    .build();
            Page<WalletTransaction> transactions = newClient().wallet().getWalletTransactions(args);
            System.out.println(">>>>> Wallet transactions:\n" + transactions);
        } catch (MpayApiException e) {
            System.out.println("getWalletTransactions error: " + e);
        }
    }

    /** Gets the list of supported blockchain networks for deposits. */
    public static void getDepositChains() {
        try {
            List<Chain> chains = newClient().wallet().getDepositChains();
            System.out.println(">>>>> Supported deposit chains:\n" + chains);
        } catch (MpayApiException e) {
            System.out.println("getDepositChains error: " + e);
        }
    }

    /** Gets available deposit options grouped by network. */
    public static void getDepositOptions() {
        try {
            Map<String, List<DepositOption>> options = newClient().wallet().getDepositOptions("network");
            System.out.println(">>>>> Deposit options:\n" + options);
        } catch (MpayApiException e) {
            System.out.println("getDepositOptions error: " + e);
        }
    }

    /** Gets the deposit wallet address for Ethereum (chain ID 1). */
    public static void getDepositAddress() {
        try {
            DepositAddress address = newClient().wallet().getDepositAddress(1);
            System.out.println(">>>>> Deposit address:\n" + address);
        } catch (MpayApiException e) {
            System.out.println("getDepositAddress error: " + e);
        }
    }

    /** Gets the user's deposit transaction records. */
    public static void getDepositTransactions() {
        try {
            GetDepositTransactionsArgs args = GetDepositTransactionsArgs.builder()
                    .chainId(1L)
                    .page(1)
                    .limit(20)
                    .build();
            Page<DepositTransaction> transactions = newClient().wallet().getDepositTransactions(args);
            System.out.println(">>>>> Deposit transactions:\n" + transactions);
        } catch (MpayApiException e) {
            System.out.println("getDepositTransactions error: " + e);
        }
    }

    public static void main(String[] args) {
        getWalletBalance();
        getWalletTransactions();
        getDepositChains();
        getDepositOptions();
        getDepositAddress();
        getDepositTransactions();
    }
}
