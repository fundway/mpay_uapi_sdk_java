package cards.mpay.uapi.model.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An available card product, as returned by {@code GET /v1/card/products}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardProduct {

    private long id;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("mode_type")
    private String modeType;

    @JsonProperty("card_currency")
    private String cardCurrency;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getCardCurrency() {
        return cardCurrency;
    }

    public void setCardCurrency(String cardCurrency) {
        this.cardCurrency = cardCurrency;
    }

    @Override
    public String toString() {
        return "CardProduct{id=" + id + ", productId='" + productId + "', modeType='" + modeType
                + "', cardCurrency='" + cardCurrency + "'}";
    }
}
