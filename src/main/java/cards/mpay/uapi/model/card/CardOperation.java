package cards.mpay.uapi.model.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The result of an asynchronous card operation submission, as returned by
 * {@code POST /v1/card/create} and {@code POST /v1/card/recharge}.
 *
 * <p>When {@link #getStatus()} is {@code "PROCESSING"}, poll
 * {@code GET /v1/card/operation/status} with {@link #getOperationId()}
 * until it resolves to a terminal state.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardOperation {

    @JsonProperty("operation_id")
    private String operationId;

    @JsonProperty("operation_type")
    private String operationType;

    private String status;
    private String message;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CardOperation{operationId='" + operationId + "', operationType='" + operationType
                + "', status='" + status + "', message='" + message + "'}";
    }
}
