package cards.mpay.uapi.exception;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Raised when the mPay server returns a business/API-level error.
 *
 * <p>Carries the error code and message returned by the server, the HTTP
 * status code of the response, and the raw parsed JSON response body (if
 * available) for callers that need additional context.</p>
 */
public class MpayApiException extends MpayException {

    private final String code;
    private final Integer httpStatus;
    private final JsonNode response;

    public MpayApiException(String message, String code, Integer httpStatus, JsonNode response) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.response = response;
    }

    /** The error code returned by the server, if any. */
    public String getCode() {
        return code;
    }

    /** The HTTP status code of the response. */
    public Integer getHttpStatus() {
        return httpStatus;
    }

    /** The raw, parsed JSON response body, if available. */
    public JsonNode getResponse() {
        return response;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage());
        if (code != null) {
            sb.append(" | code=").append(code);
        }
        if (httpStatus != null) {
            sb.append(" | http_status=").append(httpStatus);
        }
        if (response != null) {
            sb.append(" | response=").append(response);
        }
        return sb.toString();
    }
}
