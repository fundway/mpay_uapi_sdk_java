package cards.mpay.uapi.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A deposit wallet address for a specific blockchain network, as returned
 * by {@code GET /v1/deposit/address}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositAddress {

    private long id;

    @JsonProperty("chain_id")
    private String chainId;

    @JsonProperty("chain_name")
    private String chainName;

    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "DepositAddress{id=" + id + ", chainId='" + chainId + "', chainName='" + chainName
                + "', address='" + address + "'}";
    }
}
