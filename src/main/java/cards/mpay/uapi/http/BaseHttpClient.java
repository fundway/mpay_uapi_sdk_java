package cards.mpay.uapi.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cards.mpay.uapi.auth.HmacAuth;
import cards.mpay.uapi.auth.SignatureUtil;
import cards.mpay.uapi.exception.MpayApiException;
import cards.mpay.uapi.exception.MpayNetworkException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Thin wrapper around {@link HttpClient} that signs and sends requests to
 * the mPay uAPI.
 */
public class BaseHttpClient {

    public static final String DEFAULT_BASE_URL = "https://uapi.mpay.cards";
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    public static final String DEFAULT_USER_AGENT = "mpay-uapi-sdk-java";

    private final String baseUrl;
    private final Duration timeout;
    private final HmacAuth auth;
    private final HttpClient httpClient;
    private final String userAgent;
    private final ObjectMapper mapper;

    public BaseHttpClient(
            String accessKey,
            String secretKey,
            String baseUrl,
            Duration timeout,
            HttpClient httpClient,
            String userAgent) {
        this.baseUrl = trimTrailingSlash(baseUrl == null || baseUrl.isEmpty() ? DEFAULT_BASE_URL : baseUrl);
        this.timeout = timeout == null ? DEFAULT_TIMEOUT : timeout;
        this.auth = new HmacAuth(accessKey, secretKey);
        this.userAgent = userAgent == null || userAgent.isEmpty() ? DEFAULT_USER_AGENT : userAgent;
        this.httpClient = httpClient == null
                ? HttpClient.newBuilder().connectTimeout(this.timeout).build()
                : httpClient;
        this.mapper = new ObjectMapper();
    }

    private static String trimTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public <T> T get(String path, Map<String, Object> params, Class<T> type) {
        return convert(request("GET", path, params, null), type);
    }

    public <T> T get(String path, Map<String, Object> params, TypeReference<T> typeRef) {
        return convert(request("GET", path, params, null), typeRef);
    }

    public <T> T post(String path, Object body, Class<T> type) {
        return convert(request("POST", path, null, body), type);
    }

    public <T> T post(String path, Object body, TypeReference<T> typeRef) {
        return convert(request("POST", path, null, body), typeRef);
    }

    /**
     * Sends a request and returns the parsed {@code data} payload of the
     * response.
     *
     * @param method the HTTP method
     * @param path   the request path, e.g. {@code "/v1/card/create"}
     * @param params optional query-string parameters. NOTE: for non-GET
     *               requests, query params are NOT included in the HMAC
     *               signature -- only the JSON body is signed (matching the
     *               reference implementation). Avoid relying on query
     *               params for POST endpoints unless you've confirmed the
     *               server does the same.
     * @param body   optional JSON request body. Its keys are sorted at
     *               every level and it is included in the HMAC signature.
     */
    public JsonNode request(String method, String path, Map<String, Object> params, Object body) {
        Map<String, Object> cleanParams = cleanParams(params);
        URI uri = buildUri(path, cleanParams);

        // GET requests sign the query params; every other method signs the
        // JSON body instead (never both at once) -- see SignatureUtil.
        Map<String, String> authHeaders = auth.headers(method, path, cleanParams, body);

        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("Accept", "application/json")
                .header("User-Agent", userAgent);
        authHeaders.forEach(builder::header);

        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.noBody();
        if (body != null) {
            // The body is sent in its original field order -- only the
            // *signature* computation sorts keys, not the payload on the
            // wire. Numeric values are still normalized (25.0 -> 25) to
            // match the JS server's own JSON round-trip semantics.
            JsonNode node = mapper.valueToTree(body);
            JsonNode normalized = SignatureUtil.normalizeNumbers(node);
            String jsonBody = writeCompact(normalized);
            builder.header("Content-Type", "application/json");
            publisher = HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8);
        }
        builder.method(method, publisher);

        HttpResponse<String> response;
        try {
            response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new MpayNetworkException("Network error while calling " + method + " " + path, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MpayNetworkException("Network error while calling " + method + " " + path, e);
        }
        return handleResponse(response);
    }

    private String writeCompact(JsonNode node) {
        try {
            return mapper.writeValueAsString(node);
        } catch (IOException e) {
            throw new MpayNetworkException("Failed to serialize request body", e);
        }
    }

    private Map<String, Object> cleanParams(Map<String, Object> params) {
        Map<String, Object> clean = new LinkedHashMap<>();
        if (params != null) {
            params.forEach((k, v) -> {
                if (v != null) {
                    clean.put(k, v);
                }
            });
        }
        return clean;
    }

    private URI buildUri(String path, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append(path.startsWith("/") ? path : "/" + path);
        if (!params.isEmpty()) {
            sb.append('?');
            boolean first = true;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (!first) {
                    sb.append('&');
                }
                sb.append(urlEncode(entry.getKey())).append('=').append(urlEncode(String.valueOf(entry.getValue())));
                first = false;
            }
        }
        return URI.create(sb.toString());
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private JsonNode handleResponse(HttpResponse<String> response) {
        JsonNode body = null;
        String rawBody = response.body();
        if (rawBody != null && !rawBody.isEmpty()) {
            try {
                body = mapper.readTree(rawBody);
            } catch (IOException ignored) {
                body = null;
            }
        }

        int status = response.statusCode();
        boolean ok = status >= 200 && status < 300;

        if (ok) {
            // Successful responses may either wrap the payload in an
            // envelope, e.g. {"code": 0, "message": "ok", "data": ...}, or
            // return the payload directly. Both shapes are supported.
            if (body != null && body.isObject() && body.has("data")
                    && (body.has("code") || body.has("message"))) {
                JsonNode codeNode = body.get("code");
                boolean isSuccessCode = codeNode == null || codeNode.isNull()
                        || (codeNode.isNumber() && codeNode.asInt() == 0)
                        || (codeNode.isTextual() && "0".equals(codeNode.asText()));
                if (!isSuccessCode) {
                    String message = body.has("message") ? body.get("message").asText() : "Unknown API error";
                    throw new MpayApiException(message, codeNode == null ? null : codeNode.asText(), status, body);
                }
                return body.get("data");
            }
            return body;
        }

        String message = "Request failed";
        String code = null;
        if (body != null && body.isObject()) {
            JsonNode error = body.get("error");
            if (error != null && error.isObject()) {
                message = error.has("message") ? error.get("message").asText() : message;
                code = error.has("code") ? error.get("code").asText() : null;
            } else {
                if (body.has("message")) {
                    message = body.get("message").asText();
                } else if (error != null) {
                    message = error.asText();
                }
                code = body.has("code") ? body.get("code").asText() : null;
            }
        }
        throw new MpayApiException(message, code, status, body);
    }

    private <T> T convert(JsonNode data, Class<T> type) {
        if (data == null || data.isNull()) {
            return null;
        }
        return mapper.convertValue(data, type);
    }

    private <T> T convert(JsonNode data, TypeReference<T> typeRef) {
        if (data == null || data.isNull()) {
            return null;
        }
        return mapper.convertValue(data, typeRef);
    }
}
