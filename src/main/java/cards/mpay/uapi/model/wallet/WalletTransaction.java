package cards.mpay.uapi.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single wallet transaction record, as returned by
 * {@code GET /v1/wallet/transactions}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletTransaction {

    private long id;
    private String currency;
    private String direction;
    private double amount;
    private double balance;
    private String reason;

    @JsonProperty("created_at")
    private String createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "WalletTransaction{id=" + id + ", currency='" + currency + "', direction='" + direction
                + "', amount=" + amount + ", balance=" + balance + ", reason='" + reason
                + "', createdAt='" + createdAt + "'}";
    }
}
