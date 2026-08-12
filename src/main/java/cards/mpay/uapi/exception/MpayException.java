package cards.mpay.uapi.exception;

/**
 * Base exception for all errors raised by this SDK.
 */
public class MpayException extends RuntimeException {

    public MpayException(String message) {
        super(message);
    }

    public MpayException(String message, Throwable cause) {
        super(message, cause);
    }
}
