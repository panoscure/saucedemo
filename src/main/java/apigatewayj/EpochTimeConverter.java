package apigatewayj;

import common.utils.Properties;
import io.restassured.response.Response;
import lottery.apigatewayj.models.pam.players.PlayerSignOn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static apigatewayj.Headers.getCommonHeaders;
import static io.restassured.http.ContentType.JSON;
import static lottery.apigatewayj.Authentication.grantAuthorizationTokenExtractToken;
import static lottery.apigatewayj.AuthenticationService.playerSignOn;

public class EpochTimeConverter {

    public static String convertEpochToHumanReadable(long epochTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, HH:mm a");
        return sdf.format(new Date(epochTime));
    }

    public static long convertHumanReadableWithCommaToEpoch(String readableDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa");
        Date date = df.parse(readableDate);
        return date.getTime();
    }

    public static long convertHumanReadableWithoutCommaToEpoch(String readableDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        Date date = df.parse(readableDate);
        return date.getTime();
    }
}
