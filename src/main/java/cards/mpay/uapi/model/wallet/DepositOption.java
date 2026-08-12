package cards.mpay.uapi.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single deposit option, as returned by {@code GET /v1/deposit/options}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositOption {

    @JsonProperty("chain_id")
    private String chainId;

    @JsonProperty("chain_name")
    private String chainName;

    @JsonProperty("chain_icon")
    private String chainIcon;

    @JsonProperty("token_id")
    private String tokenId;

    @JsonProperty("token_name")
    private String tokenName;

    @JsonProperty("min_deposit_amount")
    private double minDepositAmount;

    @JsonProperty("estimated_arrival_time")
    private String estimatedArrivalTime;

    private double fee;

    @JsonProperty("token_standard")
    private String tokenStandard;

    @JsonProperty("token_symbol")
    private String tokenSymbol;

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getChainIcon() {
        return chainIcon;
    }

    public void setChainIcon(String chainIcon) {
        this.chainIcon = chainIcon;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public double getMinDepositAmount() {
        return minDepositAmount;
    }

    public void setMinDepositAmount(double minDepositAmount) {
        this.minDepositAmount = minDepositAmount;
    }

    public String getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(String estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getTokenStandard() {
        return tokenStandard;
    }

    public void setTokenStandard(String tokenStandard) {
        this.tokenStandard = tokenStandard;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    @Override
    public String toString() {
        return "DepositOption{chainId='" + chainId + "', chainName='" + chainName + "', tokenSymbol='"
                + tokenSymbol + "', tokenStandard='" + tokenStandard + "', minDepositAmount=" + minDepositAmount
                + ", fee=" + fee + '}';
    }
}
