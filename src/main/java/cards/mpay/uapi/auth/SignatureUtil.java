package cards.mpay.uapi.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cards.mpay.uapi.exception.MpaySignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * HMAC-SHA256 request signing helpers for the mPay uAPI.
 *
 * <p>This mirrors the reference implementation exactly. Every request is
 * signed using HMAC-SHA256 with the account's {@code secretKey}. The
 * signature is transmitted via the {@code X-Signature} header, together
 * with three companion headers that are also part of the signed
 * payload:</p>
 *
 * <ul>
 *   <li>{@code X-Api-Key}: the caller's public API key / access key id.</li>
 *   <li>{@code X-Timestamp}: Unix timestamp in <b>milliseconds</b> at which
 *       the request was signed. The server rejects requests whose
 *       timestamp deviates from server time by more than a configurable
 *       tolerance (replay protection).</li>
 *   <li>{@code X-Nonce}: a random, per-request string used together with
 *       the timestamp to prevent replay attacks.</li>
 * </ul>
 *
 * <p>The string that gets signed ("string-to-sign") is built as:</p>
 *
 * <pre>
 * METHOD
 * PATH
 * TIMESTAMP
 * NONCE
 * PAYLOAD
 * </pre>
 *
 * <p>Where:</p>
 * <ul>
 *   <li>{@code METHOD} is the upper-cased HTTP method, e.g. {@code GET}.</li>
 *   <li>{@code PATH} is the request path <b>without</b> the query string,
 *       e.g. {@code /v1/wallet/balance}.</li>
 *   <li>{@code PAYLOAD} depends on the method:
 *     <ul>
 *       <li>For {@code GET} requests: the canonical query string -- every
 *           query parameter, sorted lexicographically by key, joined as
 *           {@code k=v} pairs with {@code &}, WITHOUT any URL-encoding.
 *           An empty query canonicalizes to {@code ""}.</li>
 *       <li>For all other methods ({@code POST}, {@code PUT}, ...): the
 *           canonical JSON body -- the body object with its keys sorted
 *           lexicographically at every level (array element order is
 *           preserved), serialized compactly (no extra whitespace). An
 *           empty/{@code null} body canonicalizes to {@code ""}.</li>
 *       <li>Query and body are never combined in the same signature: GET
 *           signs the query, everything else signs the body only.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>The resulting HMAC-SHA256 digest is hex-encoded (lowercase) to form
 * the final signature value.</p>
 */
public final class SignatureUtil {

    public static final String HEADER_ACCESS_KEY = "X-Api-Key";
    public static final String HEADER_TIMESTAMP = "X-Timestamp";
    public static final String HEADER_NONCE = "X-Nonce";
    public static final String HEADER_SIGNATURE = "X-Signature";

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private SignatureUtil() {
    }

    /**
     * Recursively converts double/float values with an integral value (e.g.
     * {@code 25.0}) into integer node types (e.g. {@code 25}).
     *
     * <p>JavaScript has a single numeric type, so {@code 25.0} and
     * {@code 25} serialize identically via {@code JSON.stringify} (both
     * become {@code "25"}). Java (like Python) distinguishes floating point
     * and integer types, so a Java {@code 25.0d} would otherwise serialize
     * as {@code "25.0"} -- a mismatch with what the (JS-based) server
     * recomputes after it round-trips the value through its own JSON
     * parser. This normalizes values to match JS semantics before they are
     * canonicalized/signed or sent on the wire.</p>
     */
    public static JsonNode normalizeNumbers(JsonNode node) {
        if (node == null || node.isNull()) {
            return node;
        }
        if (node.isDouble() || node.isFloat() || node.isBigDecimal()) {
            double value = node.doubleValue();
            if (!Double.isInfinite(value) && !Double.isNaN(value) && value == Math.floor(value)
                    && Math.abs(value) < 9.007199254740992E15) {
                return LongNode.valueOf((long) value);
            }
            return node;
        }
        if (node.isObject()) {
            ObjectNode result = MAPPER.createObjectNode();
            node.fields().forEachRemaining(entry -> result.set(entry.getKey(), normalizeNumbers(entry.getValue())));
            return result;
        }
        if (node.isArray()) {
            ArrayNode result = MAPPER.createArrayNode();
            for (JsonNode item : node) {
                result.add(normalizeNumbers(item));
            }
            return result;
        }
        return node;
    }

    /**
     * Recursively sorts object keys at every level; array element order is
     * preserved. Only object/map keys are sorted -- they carry no semantic
     * order. Array element order is left untouched, since position is
     * typically meaningful (e.g. batch operation order, transaction
     * sequence).
     */
    public static JsonNode sortKeysDeep(JsonNode node) {
        if (node == null || node.isNull()) {
            return node;
        }
        if (node.isObject()) {
            List<String> keys = new ArrayList<>();
            node.fieldNames().forEachRemaining(keys::add);
            Collections.sort(keys);
            ObjectNode result = MAPPER.createObjectNode();
            for (String key : keys) {
                result.set(key, sortKeysDeep(node.get(key)));
            }
            return result;
        }
        if (node.isArray()) {
            ArrayNode result = MAPPER.createArrayNode();
            for (JsonNode item : node) {
                result.add(sortKeysDeep(item));
            }
            return result;
        }
        return node;
    }

    /**
     * Builds the canonical query string used for signing GET requests.
     *
     * <p>Keys are sorted lexicographically. Values are NOT url-encoded --
     * this matches the reference implementation, which does plain
     * {@code ${k}=${v}} string interpolation.</p>
     */
    public static String canonicalizeQuery(Map<String, ?> query) {
        if (query == null || query.isEmpty()) {
            return "";
        }
        List<String> keys = new ArrayList<>(query.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : keys) {
            Object value = query.get(key);
            if (value == null) {
                continue;
            }
            if (!first) {
                sb.append('&');
            }
            sb.append(key).append('=').append(stringifyQueryValue(value));
            first = false;
        }
        return sb.toString();
    }

    private static String stringifyQueryValue(Object value) {
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "true" : "false";
        }
        if (value instanceof Float || value instanceof Double) {
            double d = ((Number) value).doubleValue();
            if (!Double.isInfinite(d) && !Double.isNaN(d) && d == Math.floor(d)
                    && Math.abs(d) < 9.007199254740992E15) {
                return String.valueOf((long) d);
            }
            return String.valueOf(d);
        }
        return String.valueOf(value);
    }

    /**
     * Builds the canonical body string used for signing non-GET requests.
     *
     * <p>Mirrors the reference implementation: an empty or {@code null}
     * body canonicalizes to {@code ""}; otherwise object keys are sorted at
     * every level and the result is serialized compactly (equivalent to
     * JS's default, whitespace-free {@code JSON.stringify}).</p>
     */
    public static String canonicalizeBody(Object body) {
        if (body == null) {
            return "";
        }
        JsonNode node = body instanceof JsonNode ? (JsonNode) body : MAPPER.valueToTree(body);
        if (node == null || node.isNull() || (node.isObject() && node.isEmpty())) {
            return "";
        }
        JsonNode normalized = normalizeNumbers(node);
        JsonNode sorted = sortKeysDeep(normalized);
        try {
            return MAPPER.writeValueAsString(sorted);
        } catch (JsonProcessingException e) {
            throw new MpaySignatureException("Failed to canonicalize request body", e);
        }
    }

    /** Generates a random, unique nonce string for a single request. */
    public static String generateNonce() {
        return UUID.randomUUID().toString();
    }

    /** Returns the current Unix timestamp in <b>milliseconds</b> as a string. */
    public static String currentTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Builds the string-to-sign.
     *
     * <p>For GET requests the payload segment is the canonical query
     * string; for every other method it is the canonical JSON body. Query
     * and body are never both included -- this matches the reference
     * implementation.</p>
     */
    public static String buildStringToSign(
            String method,
            String path,
            String timestamp,
            String nonce,
            Map<String, ?> query,
            Object body) {
        String upperMethod = method.toUpperCase(Locale.ROOT);
        String payload = "GET".equals(upperMethod) ? canonicalizeQuery(query) : canonicalizeBody(body);
        return String.join("\n", upperMethod, path, timestamp, nonce, payload);
    }

    /** Computes the hex-encoded HMAC-SHA256 signature for {@code stringToSign}. */
    public static String sign(String secret, String stringToSign) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return toHex(hash);
        } catch (GeneralSecurityException e) {
            throw new MpaySignatureException("Failed to compute HMAC-SHA256 signature", e);
        }
    }

    private static String toHex(byte[] bytes) {
        char[] out = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            out[i * 2] = HEX_DIGITS[v >>> 4];
            out[i * 2 + 1] = HEX_DIGITS[v & 0x0F];
        }
        return new String(out);
    }
}
