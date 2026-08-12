package cards.mpay.uapi.api;

import cards.mpay.uapi.http.BaseHttpClient;
import cards.mpay.uapi.model.holder.HolderInfo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Client for {@code /v1/holder/*} endpoints.
 */
public class HolderApi {

    private final BaseHttpClient http;

    public HolderApi(BaseHttpClient http) {
        this.http = http;
    }

    /**
     * Retrieves cardholder information.
     *
     * <p>GET /v1/holder/info</p>
     */
    public HolderInfo getHolderInfo() {
        return http.get("/v1/holder/info", null, HolderInfo.class);
    }

    /**
     * Updates the cardholder information.
     *
     * <p>POST /v1/holder/set</p>
     *
     * @param firstName cardholder's first name, required
     * @param lastName  cardholder's last name, required
     * @return the latest cardholder information
     */
    public HolderInfo setHolderInfo(String firstName, String lastName) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("first_name", firstName);
        body.put("last_name", lastName);
        return http.post("/v1/holder/set", body, HolderInfo.class);
    }
}
