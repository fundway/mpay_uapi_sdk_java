package cards.mpay.uapi.model.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single card transaction record, as returned by
 * {@code GET /v1/card/transactions}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardTransaction {

    private String id;

    @JsonProperty("card_id")
    private String cardId;

    private String pan;

    @JsonProperty("trade_no")
    private String tradeNo;

    private String type;
    private String status;

    @JsonProperty("trade_amount")
    private String tradeAmount;

    @JsonProperty("trade_currency")
    private String tradeCurrency;

    @JsonProperty("billing_amount")
    private String billingAmount;

    @JsonProperty("billing_currency")
    private String billingCurrency;

    @JsonProperty("service_fee")
    private double serviceFee;

    @JsonProperty("actual_transaction_amount")
    private double actualTransactionAmount;

    @JsonProperty("merchant_data")
    private Object merchantData;

    private String direction;
    private String timestamp;

    @JsonProperty("create_time")
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    public String getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(String billingAmount) {
        this.billingAmount = billingAmount;
    }

    public String getBillingCurrency() {
        return billingCurrency;
    }

    public void setBillingCurrency(String billingCurrency) {
        this.billingCurrency = billingCurrency;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getActualTransactionAmount() {
        return actualTransactionAmount;
    }

    public void setActualTransactionAmount(double actualTransactionAmount) {
        this.actualTransactionAmount = actualTransactionAmount;
    }

    public Object getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(Object merchantData) {
        this.merchantData = merchantData;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CardTransaction{id='" + id + "', cardId='" + cardId + "', tradeNo='" + tradeNo + "', type='"
                + type + "', status='" + status + "', tradeAmount='" + tradeAmount + "', tradeCurrency='"
                + tradeCurrency + "', direction='" + direction + "', timestamp='" + timestamp + "'}";
    }
}
