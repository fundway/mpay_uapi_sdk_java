package cards.mpay.uapi.api;

import com.fasterxml.jackson.core.type.TypeReference;
import cards.mpay.uapi.http.BaseHttpClient;
import cards.mpay.uapi.model.Page;
import cards.mpay.uapi.model.wallet.Chain;
import cards.mpay.uapi.model.wallet.DepositAddress;
import cards.mpay.uapi.model.wallet.DepositOption;
import cards.mpay.uapi.model.wallet.DepositTransaction;
import cards.mpay.uapi.model.wallet.WalletBalance;
import cards.mpay.uapi.model.wallet.WalletTransaction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Client for {@code /v1/wallet/*} and {@code /v1/deposit/*} endpoints.
 */
public class WalletApi {

    private final BaseHttpClient http;

    public WalletApi(BaseHttpClient http) {
        this.http = http;
    }

    /**
     * Gets the user's wallet balance.
     *
     * <p>GET /v1/wallet/balance</p>
     */
    public WalletBalance getWalletBalance() {
        return http.get("/v1/wallet/balance", null, WalletBalance.class);
    }

    /**
     * Gets the user's wallet transaction records.
     *
     * <p>GET /v1/wallet/transactions</p>
     *
     * @param direction {@code "in"} or {@code "out"}. Pass {@code null} to return both directions.
     * @param page      page number, starting from 1
     * @param limit     number of records to return per page
     */
    public Page<WalletTransaction> getWalletTransactions(String direction, Integer page, Integer limit) {
        Map<String, Object> params = new LinkedHashMap<>();
        if (direction != null) {
            params.put("direction", direction);
        }
        if (page != null) {
            params.put("page", page);
        }
        if (limit != null) {
            params.put("limit", limit);
        }
        return http.get("/v1/wallet/transactions", params, new TypeReference<Page<WalletTransaction>>() {
        });
    }

    /**
     * Gets the user's wallet transaction records.
     *
     * <p>GET /v1/wallet/transactions</p>
     *
     * @param direction {@code "in"} or {@code "out"}. Pass {@code null} to return both directions.
     */
    public Page<WalletTransaction> getWalletTransactions(String direction) {
        return getWalletTransactions(direction, null, null);
    }

    /**
     * Gets the user's wallet transaction records.
     *
     * <p>GET /v1/wallet/transactions</p>
     */
    public Page<WalletTransaction> getWalletTransactions() {
        return getWalletTransactions(null, null, null);
    }

    /**
     * Gets the list of supported blockchain networks for deposits.
     *
     * <p>GET /v1/deposit/chains</p>
     */
    public List<Chain> getDepositChains() {
        return http.get("/v1/deposit/chains", null, new TypeReference<List<Chain>>() {
        });
    }

    /**
     * Gets available deposit options, grouped by the given key.
     *
     * <p>GET /v1/deposit/options</p>
     *
     * @param groupBy {@code "network"} or {@code "asset"}
     * @return deposit options keyed by network or asset name
     */
    public Map<String, List<DepositOption>> getDepositOptions(String groupBy) {
        Map<String, Object> params = new LinkedHashMap<>();
        if (groupBy != null) {
            params.put("groupBy", groupBy);
        }
        return http.get("/v1/deposit/options", params, new TypeReference<Map<String, List<DepositOption>>>() {
        });
    }

    /**
     * Gets available deposit options, grouped by the given key.
     *
     * <p>GET /v1/deposit/options</p>
     *
     * @return deposit options keyed by network
     */
    public Map<String, List<DepositOption>> getDepositOptions() {
        return getDepositOptions(null);
    }

    /**
     * Gets the deposit wallet address for the specified blockchain network.
     *
     * <p>GET /v1/deposit/address</p>
     *
     * @param chainId blockchain network chain ID, see {@link #getDepositChains()}
     */
    public DepositAddress getDepositAddress(long chainId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("chain_id", chainId);
        return http.get("/v1/deposit/address", params, DepositAddress.class);
    }

    /**
     * Gets the user's deposit transaction records.
     *
     * <p>GET /v1/deposit/transactions</p>
     *
     * @param chainId blockchain network chain ID. Pass {@code null} to include all networks.
     * @param page    page number, starting from 1
     * @param limit   number of records to return per page
     * @return paginated deposit transaction records
     */
    public Page<DepositTransaction> getDepositTransactions(Long chainId, Integer page, Integer limit) {
        Map<String, Object> params = new LinkedHashMap<>();
        if (chainId != null) {
            params.put("chain_id", chainId);
        }
        if (page != null) {
            params.put("page", page);
        }
        if (limit != null) {
            params.put("limit", limit);
        }
        return http.get("/v1/deposit/transactions", params, new TypeReference<Page<DepositTransaction>>() {
        });
    }

    /**
     * Gets the user's deposit transaction records.
     *
     * <p>GET /v1/deposit/transactions</p>
     *
     * @param chainId blockchain network chain ID. Pass {@code null} to include all networks.
     * @return paginated deposit transaction records
     */
    public Page<DepositTransaction> getDepositTransactions(Long chainId) {
        return getDepositTransactions(chainId, null, null);
    }

    /**
     * Gets the user's deposit transaction records.
     *
     * <p>GET /v1/deposit/transactions</p>
     *
     * @return paginated deposit transaction records
     */
    public Page<DepositTransaction> getDepositTransactions() {
        return getDepositTransactions(null, null, null);
    }
}
