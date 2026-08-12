package cards.mpay.uapi.model.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Sensitive card information (PAN, CVV, expiry, PIN), as returned by
 * {@code GET /v1/card/sensitive}.
 *
 * <p><b>Note:</b> the returned data is highly sensitive. Handle it
 * according to PCI-DSS requirements: never log it, and avoid persisting it
 * unless strictly necessary.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardSensitive {

    private String pan;
    private String pin;
    private String cvv;
    private String expire;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        // Deliberately omits pan/cvv/pin from the string representation to
        // avoid accidentally leaking sensitive data into logs.
        return "CardSensitive{expire='" + expire + "', pan=****, cvv=***, pin=****}";
    }
}
