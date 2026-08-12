package cards.mpay.uapi.model.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The result status of a card operation (create/recharge/etc.), as
 * returned by {@code GET /v1/card/operation/status}.
 *
 * <p>Extends {@link CardOperation} with the resulting {@link CardInfo} once
 * the operation reaches a terminal state.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardOperationStatus extends CardOperation {

    private CardInfo card;

    public CardInfo getCard() {
        return card;
    }

    public void setCard(CardInfo card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "CardOperationStatus{operationId='" + getOperationId() + "', operationType='"
                + getOperationType() + "', status='" + getStatus() + "', message='" + getMessage()
                + "', card=" + card + '}';
    }
}
