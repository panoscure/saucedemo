package apigatewayj;

import common.utils.Properties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

public class Headers {

    public static Map<String, String> getCommonHeaders(String bearerToken) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Guid", UUID.randomUUID().toString());
        headers.put(AUTHORIZATION, "Bearer " + bearerToken);
        headers.put("Content-Type", "application/json");
        headers.put("accept", "application/json");
        headers.put("Operator", Properties.getPropertyValue("operator"));
        headers.put("Channel", Properties.getPropertyValue("web.channel"));
        return headers;
    }

    public static HashMap<String, String> getHeadersWithSessionToken(String bearerToken, String sessionToken) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, "Bearer " + bearerToken);
        headers.put("Content-Type", "application/json");
        headers.put("accept", "application/json");
        headers.put("Operator", Properties.getPropertyValue("operator"));
        headers.put("Channel", Properties.getPropertyValue("web.channel"));
        headers.put("sessionToken", sessionToken);
        return headers;
    }

}