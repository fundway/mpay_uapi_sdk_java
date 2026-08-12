package cards.mpay.uapi.exception;

/**
 * Raised when a network-level error occurs while calling the mPay API.
 */
public class MpayNetworkException extends MpayException {

    public MpayNetworkException(String message) {
        super(message);
    }

    public MpayNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
