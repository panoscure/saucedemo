package apigatewayj;

import common.utils.Properties;
import io.restassured.response.Response;
import lottery.apigatewayj.models.pam.players.PlayerSignOn;

import java.util.HashMap;
import java.util.Map;

import static apigatewayj.Headers.getCommonHeaders;
import static io.restassured.http.ContentType.JSON;
import static lottery.apigatewayj.Authentication.grantAuthorizationTokenExtractToken;
import static lottery.apigatewayj.AuthenticationService.playerSignOn;

public class Tokens {

    public static String getBearerToken(){
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders.put("accept", JSON.toString());
        authHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        authHeaders.put("Authorization", Properties.getPropertyValue("Basic"));

        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        String bearerToken = grantAuthorizationTokenExtractToken(authHeaders, formParams);
        return bearerToken;
    }

    public static String getSessionToken(String bearerToken) {
        PlayerSignOn playerSignOn = ((Response) playerSignOn(getCommonHeaders(bearerToken),
                Properties.getPropertyValue("canvas.user"),
                Properties.getPropertyValue("canvas.password")))
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerSignOn.class);
        return playerSignOn.getSessionToken();
    }

}
