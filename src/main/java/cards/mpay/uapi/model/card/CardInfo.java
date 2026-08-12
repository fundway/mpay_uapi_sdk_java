package cards.mpay.uapi.model.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Card information excluding sensitive data, as returned by
 * {@code GET /v1/card/list}, {@code GET /v1/card/info}, and
 * {@code POST /v1/card/remark}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfo {

    private long id;

    @JsonProperty("card_color")
    private String cardColor;

    @JsonProperty("card_id")
    private String cardId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("holder_id")
    private String holderId;

    private String pan;
    private String currency;
    private double balance;

    @JsonProperty("card_type")
    private String cardType;

    @JsonProperty("card_status")
    private String cardStatus;

    private String firstname;
    private String lastname;
    private String email;

    @JsonProperty("user_remark")
    private String userRemark;

    @JsonProperty("created_at")
    private String createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CardInfo{id=" + id + ", cardId='" + cardId + "', pan='" + pan + "', currency='" + currency
                + "', balance=" + balance + ", cardType='" + cardType + "', cardStatus='" + cardStatus
                + "', firstname='" + firstname + "', lastname='" + lastname + "', email='" + email
                + "', userRemark='" + userRemark + "', createdAt='" + createdAt + "'}";
    }
}
