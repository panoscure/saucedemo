package apigatewayj;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class MyProfileService {

    public static Object getMyFavoritePlaySlips(Map<String, String> headers) {
        headers.put("Guid", UUID.randomUUID().toString());
        return given()
                .headers(headers)
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .get("http://apigatewayj.ilottery.dev.azure.l10.intralot.com/api/v1.0/my-favorite-playslips");
    }

    public static Object deleteMyFavoritePlaySlipById(Map<String, String> headers, int playSlipId) {
        headers.put("Guid", UUID.randomUUID().toString());
        return given()
                .headers(headers)
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .delete("http://apigatewayj.ilottery.dev.azure.l10.intralot.com/api/v1.0/my-favorite-playslips/"+playSlipId);
    }

}
