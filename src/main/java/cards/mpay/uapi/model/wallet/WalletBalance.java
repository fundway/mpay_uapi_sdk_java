package cards.mpay.uapi.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The user's wallet balance, as returned by {@code GET /v1/wallet/balance}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletBalance {

    private double balance;

    @JsonProperty("avail_balance")
    private double availBalance;

    @JsonProperty("lock_balance")
    private double lockBalance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(double availBalance) {
        this.availBalance = availBalance;
    }

    public double getLockBalance() {
        return lockBalance;
    }

    public void setLockBalance(double lockBalance) {
        this.lockBalance = lockBalance;
    }

    @Override
    public String toString() {
        return "WalletBalance{balance=" + balance + ", availBalance=" + availBalance
                + ", lockBalance=" + lockBalance + '}';
    }
}
