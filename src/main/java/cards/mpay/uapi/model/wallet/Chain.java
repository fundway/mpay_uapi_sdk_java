package cards.mpay.uapi.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A supported blockchain network, as returned by {@code GET /v1/deposit/chains}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chain {

    @JsonProperty("chain_id")
    private long chainId;

    @JsonProperty("chain_name")
    private String chainName;

    public long getChainId() {
        return chainId;
    }

    public void setChainId(long chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    @Override
    public String toString() {
        return "Chain{chainId=" + chainId + ", chainName='" + chainName + "'}";
    }
}
