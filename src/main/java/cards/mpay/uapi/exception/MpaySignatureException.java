package cards.mpay.uapi.exception;

/**
 * Raised when request signing fails or the response signature is invalid.
 */
public class MpaySignatureException extends MpayException {

    public MpaySignatureException(String message) {
        super(message);
    }

    public MpaySignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
