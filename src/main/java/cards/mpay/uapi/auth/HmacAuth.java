package cards.mpay.uapi.auth;

import cards.mpay.uapi.exception.MpayConfigException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Computes the auth headers required by the mPay uAPI for a request.
 */
public class HmacAuth {

    private final String accessKey;
    private final String secretKey;

    public HmacAuth(String accessKey, String secretKey) {
        if (accessKey == null || accessKey.isEmpty()) {
            throw new MpayConfigException("accessKey is required");
        }
        if (secretKey == null || secretKey.isEmpty()) {
            throw new MpayConfigException("secretKey is required");
        }
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Builds the full set of authentication headers for a request.
     *
     * @param method the HTTP method, e.g. {@code "GET"} or {@code "POST"}
     * @param path   the request path without query string, e.g. {@code "/v1/card/list"}
     * @param params the request's query parameters, used to compute the signature payload for GET requests
     * @param body   the request's JSON body, if any, used to compute the signature payload for non-GET requests
     * @return an ordered map of header name to header value
     */
    public Map<String, String> headers(String method, String path, Map<String, ?> params, Object body) {
        String timestamp = SignatureUtil.currentTimestamp();
        String nonce = SignatureUtil.generateNonce();
        String stringToSign = SignatureUtil.buildStringToSign(method, path, timestamp, nonce, params, body);
        String signature = SignatureUtil.sign(secretKey, stringToSign);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put(SignatureUtil.HEADER_ACCESS_KEY, accessKey);
        headers.put(SignatureUtil.HEADER_TIMESTAMP, timestamp);
        headers.put(SignatureUtil.HEADER_NONCE, nonce);
        headers.put(SignatureUtil.HEADER_SIGNATURE, signature);
        return headers;
    }
}
