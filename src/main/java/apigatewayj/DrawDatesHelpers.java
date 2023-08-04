package apigatewayj;

import common.utils.Properties;
import io.restassured.response.Response;
import lottery.apigatewayj.models.draw.operations.v3_1.get.drawv3_1.GetDrawV3_1Model;
import lottery.apigatewayj.models.infostore.get.the.active.drawfor.a.game.GetTheActiveDrawForAGameModel;
import objects.draw.DrawInfoObject;
import objects.draw.WagerDrawInfo;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static apigatewayj.Headers.getCommonHeaders;
import static apigatewayj.Tokens.getBearerToken;
import static lottery.apigatewayj.DrawOperationsV3_1.getDrawV3_1;
import static lottery.apigatewayj.DrawOperationsV3_1.getTheActiveDrawForAGame;

public class DrawDatesHelpers {

    public static DrawInfoObject getActiveDrawIdAndDate(String gameId) {
        String bearerToken = getBearerToken();
        Map<String, String> headers = getCommonHeaders(bearerToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, gameId))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        Integer activeDraw = getTheActiveDraw.getDrawId();
        BigInteger drawTime = getTheActiveDraw.getDrawTime();

        DrawInfoObject drawInfoObject = new DrawInfoObject();
        drawInfoObject.setDrawId(activeDraw);
        drawInfoObject.setDrawDate(drawTime);

        return drawInfoObject;
    }

    public static DrawInfoObject getDrawDateById(String gameId, int drawId) {
        String bearerToken = getBearerToken();
        Map<String, String> headers = getCommonHeaders(bearerToken);
        GetDrawV3_1Model getDraw = ((Response) getDrawV3_1(headers, gameId, drawId))
                .then().statusCode(200).extract().as(GetDrawV3_1Model.class);
        Integer activeDraw = getDraw.getDrawId();
        BigInteger drawTime = getDraw.getDrawTime();

        DrawInfoObject drawInfoObject = new DrawInfoObject();
        drawInfoObject.setDrawId(activeDraw);
        drawInfoObject.setDrawDate(drawTime);

        return drawInfoObject;
    }

    public static String drawDateInHumanForm(BigInteger drawDate) {
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("MM/dd/YYYY, hh:mm a");
        Long drawTime = Long.valueOf(String.valueOf(drawDate));
        LocalDateTime drawDateAsLocalDateTime = Instant.ofEpochMilli(drawTime).atZone(ZoneId.of("US/Eastern")).toLocalDateTime();
        String drawDateHumanForm = datePattern.format(drawDateAsLocalDateTime);
        return drawDateHumanForm;
    }

    public static WagerDrawInfo calculateStartEndDrawOfDC5Wager(DrawInfoObject activeDrawObj, String wagerTime, Integer multiDraws, Integer numOfDrawsPerDay) {
        String activeDrawTime = "";
        Integer startingPlayingDraw = 0;
        Integer endPlayDraw = 0;
        String activeDrawId = activeDrawObj.getDrawId().toString();
        Long drawTime = Long.valueOf(String.valueOf(activeDrawObj.getDrawDate()));

        LocalDateTime drawDateAsLocalDateTime = Instant.ofEpochMilli(drawTime).atZone(ZoneId.of("US/Eastern")).toLocalDateTime();
        int drawHour = drawDateAsLocalDateTime.getHour();

        if (drawHour == 13) {
            activeDrawTime = "Day";
        } else {
            activeDrawTime = "Evening";
        }

        /**
         * if wager is both then if activeDraw is DAY or NIGHT then wager is played on the active draw (draw+1,draw+2,....)
         * if wager is night then if activeDraw is NIGHT then wager is played on the active draw
         * if wager is night then if activeDraw is DAY then wager is played on the active draw+1 (draw+3, draw+5,...)
         * if wager is day then if activeDraw is DAY then wager is played on the active draw
         * if wager is day then if activeDraw is NIGHT then wager is played on the active draw+1 (draw+3, draw+5,...)
         **/


        if (wagerTime.equals("Both")) {
            startingPlayingDraw = Integer.valueOf(activeDrawId);
            endPlayDraw = startingPlayingDraw + (multiDraws - 1);

        } else if (wagerTime.equals("Day")) {
            if (wagerTime.equals(activeDrawTime)) {
                startingPlayingDraw = Integer.valueOf(activeDrawId);
                endPlayDraw = startingPlayingDraw + ((multiDraws - 1) * numOfDrawsPerDay);
            } else {
                startingPlayingDraw = Integer.valueOf(activeDrawId) + 1;
                endPlayDraw = startingPlayingDraw + ((multiDraws - 1) * numOfDrawsPerDay);
            }
        } else if (wagerTime.equals("Evening")) {
            if (wagerTime.equals(activeDrawTime)) {
                startingPlayingDraw = Integer.valueOf(activeDrawId);
                endPlayDraw = startingPlayingDraw + ((multiDraws - 1) * numOfDrawsPerDay);
            } else {
                startingPlayingDraw = Integer.valueOf(activeDrawId) + 1;
                endPlayDraw = startingPlayingDraw + ((multiDraws - 1) * numOfDrawsPerDay);
            }
        }
        DrawInfoObject startDrawInfoObject = getDrawDateById(Properties.getPropertyValue("dc5.id"), startingPlayingDraw);
        String startDrawDateHumanForm = drawDateInHumanForm(startDrawInfoObject.getDrawDate());
        DrawInfoObject endDrawInfoObject = getDrawDateById(Properties.getPropertyValue("dc5.id"), endPlayDraw);
        String endDrawDateHumanForm = drawDateInHumanForm(endDrawInfoObject.getDrawDate());

        WagerDrawInfo wagerDrawInfo = new WagerDrawInfo();
        wagerDrawInfo.setWagerStartDrawId(startingPlayingDraw);
        wagerDrawInfo.setWagerStartDrawDate(startDrawDateHumanForm);
        wagerDrawInfo.setWagerEndDrawId(endPlayDraw);
        wagerDrawInfo.setWagerEndDrawDate(endDrawDateHumanForm);

        return wagerDrawInfo;

    }

}
