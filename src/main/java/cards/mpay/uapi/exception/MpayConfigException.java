package cards.mpay.uapi.exception;

/**
 * Raised when the client is misconfigured (missing/invalid credentials, etc.).
 */
public class MpayConfigException extends MpayException {

    public MpayConfigException(String message) {
        super(message);
    }
}
