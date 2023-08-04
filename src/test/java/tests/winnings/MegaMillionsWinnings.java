package tests.winnings;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import com.google.gson.Gson;
import common.utils.Properties;
import io.restassured.response.Response;
import lottery.apigatewayj.Wagers;
import lottery.apigatewayj.models.infostore.get.the.active.drawfor.a.game.GetTheActiveDrawForAGameModel;
import lottery.apigatewayj.models.pam.players.PlayerSignOn;
import lottery.apigatewayj.models.wagers.it.is.used.to.play.a.wager.ItIsUsedToPlayAWagerModel;
import lottery.apigatewayj.requests.wagers.it.is.used.to.play.a.wager.Metadata;
import lottery.apigatewayj.requests.wagers.it.is.used.to.play.a.wager.PlayerInfo;
import lottery.apigatewayj.requests.wagers.it.is.used.to.play.a.wager.WagerBuilderV2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.text.SimpleDateFormat;
import java.util.*;

import static canvas.Headers.getCommonHeaders;
import static canvas.Headers.playMM;
import static canvas.helpers.HelperMethods.logInCanvas;
import static canvas.helpers.HelperMethods.logOutCanvas;
import static com.google.common.truth.Truth.assertWithMessage;
import static io.restassured.http.ContentType.JSON;
import static lottery.apigatewayj.Authentication.grantAuthorizationTokenExtractToken;
import static lottery.apigatewayj.AuthenticationService.playerSignOff;
import static lottery.apigatewayj.AuthenticationService.playerSignOn;
import static lottery.apigatewayj.DrawOperationsV3_1.getTheActiveDrawForAGame;
import static lottery.us.l5.utils.CustomDrawUtils.closeDrawWithReleasePayments;


@ExtendWith({WebDriverInit.class})
public class MegaMillionsWinnings extends BaseTest {

    public static String username = Properties.getPropertyValue("canvas.fin.user");
    public static String password = Properties.getPropertyValue("canvas.fin.password");

    public static String matchFivePlusOne = "";
    public static String matchFive = "";
    public static String matchFourPlusOne = "";
    public static String matchFour = "";
    public static String matchThreePlusOne = "";
    public static String matchThree = "";
    public static String matchTwoPlusOne = "";
    public static String matchOnePlusOne = "";
    public static String matchZeroPlusOne = "";
    public static String matchFivePlusOneMegaplier = "";
    public static String matchFiveMegaplier = "";
    public static String matchFourPlusOneMegaplier = "";
    public static String matchFourMegaplier = "";
    public static String matchThreePlusOneMegaplier = "";
    public static String matchThreeMegaplier = "";
    public static String matchTwoPlusOneMegaplier = "";
    public static String matchOnePlusOneMegaplier = "";
    public static String matchZeroPlusOneMegaplier = "";
    public static String bearerToken = "";
    public static String megamillionsDrawId = "";
    public static List<String> winningColumn = Arrays.asList("1", "2", "3", "4", "5", "1");

    @BeforeAll
    public static void setUp() throws Exception {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders.put("accept", JSON.toString());
        authHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        authHeaders.put("Authorization", Properties.getPropertyValue("Basic"));

        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        bearerToken = grantAuthorizationTokenExtractToken(authHeaders, formParams);
        Map<String, String> headers = getCommonHeaders(bearerToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, Properties.getPropertyValue("megamillions.id")))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        megamillionsDrawId = getTheActiveDraw.getDrawId().toString();
        playTicketsAndCloseDraw();
    }

    public static void playTicketsAndCloseDraw() throws Exception {

        Map<String, String> authHeaders = new HashMap<>();
        authHeaders.put("accept", JSON.toString());
        authHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        authHeaders.put("Authorization", Properties.getPropertyValue("Basic"));

        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        bearerToken = grantAuthorizationTokenExtractToken(authHeaders, formParams);

        PlayerSignOn playerSignOn = ((Response) playerSignOn(getCommonHeaders(bearerToken), username, password))
                .then().statusCode(200).extract().as(PlayerSignOn.class);

        String sessionToken = playerSignOn.getSessionToken();
        Integer playerId = playerSignOn.getPlayerId();

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", JSON.toString());


        WagerBuilderV2 mmSimple = playMM();
        PlayerInfo playerInfo = mmSimple.getMetadata().getPlayerInfo();
        playerInfo.setSessionToken(sessionToken);
        playerInfo.setPlayerId(playerId);

        Metadata metadata = mmSimple.getMetadata();
        metadata.setPlayerInfo(playerInfo);

        //ticket_1
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 10)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchFourPlusOneJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchFourPlusOneResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchFourPlusOneJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchFourPlusOne = matchFourPlusOneResp.getSerialNumbers().get(0);

        //ticket_2
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 10)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(10)));

        String matchFourJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchFourResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchFourJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchFour = matchFourResp.getSerialNumbers().get(0);

        //ticket_3
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 14, 10)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchThreePlusOneJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchThreePlusOneResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchThreePlusOneJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchThreePlusOne = matchThreePlusOneResp.getSerialNumbers().get(0);

        //ticket_4
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 14, 10)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(10)));

        String matchThreeJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchThreeResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchThreeJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchThree = matchThreeResp.getSerialNumbers().get(0);

        //ticket_5
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 13, 14, 10)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchTwoPlusOneJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchTwoPlusOneResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchTwoPlusOneJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchTwoPlusOne = matchTwoPlusOneResp.getSerialNumbers().get(0);

        //ticket_6
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 12, 13, 14, 10)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchOnePlusOneJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchOnePlusOneResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchOnePlusOneJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchOnePlusOne = matchOnePlusOneResp.getSerialNumbers().get(0);

        //ticket_7
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(11, 12, 13, 14, 20)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchZeroPlusOneJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchZeroPlusOneResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchZeroPlusOneJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchZeroPlusOne = matchZeroPlusOneResp.getSerialNumbers().get(0);

        //ticket_8
        mmSimple.getWager().getDbg().get(0).setOptions(new ArrayList<>(Arrays.asList("PowerPlay")));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchFivePlusOneMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchFivePlusOneMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchFivePlusOneMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchFivePlusOneMegaplier = matchFivePlusOneMegaplierResp.getSerialNumbers().get(0);

        //ticket_9
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(15)));

        String matchFiveJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchFiveResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchFiveJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchFive = matchFiveResp.getSerialNumbers().get(0);


        //ticket_10
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(10)));

        String matchFiveMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchFiveMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchFiveMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchFiveMegaplier = matchFiveMegaplierResp.getSerialNumbers().get(0);

        //ticket_10
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 50)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchFourPlusOneMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchFourPlusOneMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchFourPlusOneMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchFourPlusOneMegaplier = matchFourPlusOneMegaplierResp.getSerialNumbers().get(0);

        //ticket_11
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 50)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(10)));

        String matchFourMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchFourMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchFourMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchFourMegaplier = matchFourMegaplierResp.getSerialNumbers().get(0);

        //ticket_12
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 40, 50)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchThreePlusOneMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchThreePlusOneMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchThreePlusOneMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchThreePlusOneMegaplier = matchThreePlusOneMegaplierResp.getSerialNumbers().get(0);

        //ticket_13
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 3, 40, 50)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(10)));

        String matchThreeMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchThreeMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchThreeMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchThreeMegaplier = matchThreeMegaplierResp.getSerialNumbers().get(0);

        //ticket_15
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 2, 30, 40, 50)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchTwoPlusOneMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchTwoPlusOneMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchTwoPlusOneMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchTwoPlusOneMegaplier = matchTwoPlusOneMegaplierResp.getSerialNumbers().get(0);

        //ticket_16
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(1, 20, 30, 40, 50)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchOnePlusOneMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchOnePlusOneMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchOnePlusOneMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchOnePlusOneMegaplier = matchOnePlusOneMegaplierResp.getSerialNumbers().get(0);

        //ticket_17
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(0).setSelection(new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50)));
        mmSimple.getWager().getDbg().get(0).getBoards().get(0).getPanels().get(1).setSelection(new ArrayList<>(Arrays.asList(1)));

        String matchZeroPlusOneMegaplierJSON = new Gson().toJson(mmSimple);
        ItIsUsedToPlayAWagerModel matchZeroPlusOneMegaplierResp = ((Response) Wagers.itIsUsedToPlayAWager(getCommonHeaders(bearerToken), matchZeroPlusOneMegaplierJSON))
                .then()
                .statusCode(200)
                .extract()
                .as(ItIsUsedToPlayAWagerModel.class);

        matchZeroPlusOneMegaplier = matchZeroPlusOneMegaplierResp.getSerialNumbers().get(0);


        //close current draw
        playerSignOff(getCommonHeaders(bearerToken), sessionToken);

        String numOfWinningColum = "7";
        //5+1 numbers & Megaplier: x2
        String winningColumn = "1 2 3 4 5 1 2";
        closeDrawWithReleasePayments(Properties.getPropertyValue("megamillions.id"), "999999", numOfWinningColum, winningColumn, -4, 60);

    }



    @AfterEach
    public void tearDown(WebDriver webDriver) {

        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1592 :: Mobile - iLottery - Megamillions - Winnings Match Four Plus One - Megaplier 2")
    public void QAAUT$1592$MegamillionsWinningsMatchFourPlusOneMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton()
                .clickShowButton(8)
                .getTicketInfo(8);
        System.out.println(gamingHistory.getTicketInfo(8));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(8).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(8).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(8).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("2.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(8).get(4).substring(11);
        System.out.println(ticketWinnings);
        assertWithMessage("incorrect ticket winnings")
                .that(ticketWinnings.equalsIgnoreCase("10,000.00"))
                .isTrue();

        gamingHistory.clickTicketInGamingHistory(8);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchFourPlusOne);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchFourPlusOne)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "3\n" +
                            "4\n" +
                            "10\n" +
                            "1\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().substring(1).equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                .that(ticket.getWinnings().equalsIgnoreCase("10,000.00"))
                .isTrue();

        assertWithMessage("incorrect ticket winnings")
                .that(ticket.getTicketWinnings().equalsIgnoreCase("10,000.00"))
                .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

        assertWithMessage("incorrect response")
                .that(ticket.getResponseMessageForWinningTicket().equalsIgnoreCase("CLAIM YOUR WINNINGS AT DC LOTTERY!"))
                .isTrue();
    }

    @Test
    @DisplayName("QAAUT-1593 :: Mobile - iLottery - Megamillions - Winnings Match Four - Megaplier 2")
    public void QAAUT$1593$MegamillionsWinningsMatchFourMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        gamingHistory.getTicketInfo(7);
        System.out.println(gamingHistory.getTicketInfo(7));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(7).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(7).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(7).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("2.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(7).get(4).substring(11);
        System.out.println(ticketWinnings);
        assertWithMessage("incorrect ticket winnings")
                .that(ticketWinnings.equalsIgnoreCase("500.00"))
                .isTrue();

        gamingHistory.clickTicketInGamingHistory(7);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchFour);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchFour)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "3\n" +
                            "4\n" +
                            "10\n" +
                            "10\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                .that(ticket.getWinnings().equalsIgnoreCase("500.00"))
                .isTrue();

        assertWithMessage("incorrect ticket winnings")
                .that(ticket.getTicketWinnings().equalsIgnoreCase("500.00"))
                .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }


    @Test
    @DisplayName("QAAUT-1594 :: Mobile - iLottery - Megamillions - Winnings Match Three Plus One - Megaplier 2")
    public void QAAUT$1594$MegamillionsWinningsMatchThreePlusOneMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        gamingHistory.getTicketInfo(6);
        System.out.println(gamingHistory.getTicketInfo(6));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(6).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(6).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(6).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("2.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(6).get(4).substring(11);
        System.out.println(ticketWinnings);
        assertWithMessage("incorrect ticket winnings")
                .that(ticketWinnings.equalsIgnoreCase("200.00"))
                .isTrue();

        gamingHistory.clickTicketInGamingHistory(6);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchThreePlusOne);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchFourPlusOne)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "3\n" +
                            "10\n" +
                            "14\n" +
                            "1\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                .that(ticket.getWinnings().equalsIgnoreCase("200.00"))
                .isTrue();

        assertWithMessage("incorrect ticket winnings")
                .that(ticket.getTicketWinnings().equalsIgnoreCase("200.00"))
                .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1595 :: Mobile - iLottery - Megamillions - Winnings Match Three - Megaplier 2")
    public void QAAUT$1595$MegamillionsWinningsMatchThreeMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        gamingHistory.getTicketInfo(5);
        System.out.println(gamingHistory.getTicketInfo(5));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(5).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(5).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(5).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("2.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(5).get(4).substring(11);
        System.out.println(ticketWinnings);
        assertWithMessage("incorrect ticket winnings")
                .that(ticketWinnings.equalsIgnoreCase("10.00"))
                .isTrue();

        gamingHistory.clickTicketInGamingHistory(5);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchThree);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchThree)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "3\n" +
                            "10\n" +
                            "14\n" +
                            "10\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                .that(ticket.getWinnings().equalsIgnoreCase("10.00"))
                .isTrue();

        assertWithMessage("incorrect ticket winnings")
                .that(ticket.getTicketWinnings().equalsIgnoreCase("10.00"))
                .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1596 :: Mobile - iLottery - Megamillions - Winnings Match Two Plus One - Megaplier 2")
    public void QAAUT$1596$MegamillionsWinningsMatchTwoPlusOneMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        gamingHistory.getTicketInfo(4);
        System.out.println(gamingHistory.getTicketInfo(4));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(4).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(4).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(4).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("2.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(4).get(4).substring(11);
        System.out.println(ticketWinnings);
        assertWithMessage("incorrect ticket winnings")
                .that(ticketWinnings.equalsIgnoreCase("10.00"))
                .isTrue();

        gamingHistory.clickTicketInGamingHistory(14);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchTwoPlusOne);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchTwoPlusOne)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "10\n" +
                            "13\n" +
                            "14\n" +
                            "1\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                .that(ticket.getWinnings().equalsIgnoreCase("10.00"))
                .isTrue();

        assertWithMessage("incorrect ticket winnings")
                .that(ticket.getTicketWinnings().equalsIgnoreCase("10.00"))
                .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1597 :: Mobile - iLottery - Megamillions - Winnings Match One Plus One - Megaplier 2")
    public void QAAUT$1597$MegamillionsWinningsMatchOnePlusOneMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        gamingHistory.getTicketInfo(3);
        System.out.println(gamingHistory.getTicketInfo(3));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(3).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(3).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(3).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("2.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(3).get(4).substring(11);
        System.out.println(ticketWinnings);
        assertWithMessage("incorrect ticket winnings")
                .that(ticketWinnings.equalsIgnoreCase("4.00"))
                .isTrue();

        gamingHistory.clickTicketInGamingHistory(3);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchOnePlusOne);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchOnePlusOne)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "10\n" +
                            "12\n" +
                            "13\n" +
                            "14\n" +
                            "1\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                .that(ticket.getWinnings().equalsIgnoreCase("4.00"))
                .isTrue();

        assertWithMessage("incorrect ticket winnings")
                .that(ticket.getTicketWinnings().equalsIgnoreCase("4.00"))
                .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1598 :: Mobile - iLottery - Megamillions - Winnings Match Zero Plus One - Megaplier 2")
    public void QAAUT$1598$MegamillionsWinningsMatchZeroPlusOneMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        gamingHistory.getTicketInfo(2);
        System.out.println(gamingHistory.getTicketInfo(2));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(2).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(2).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(2).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("2.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(2).get(4).substring(11);
        System.out.println(ticketWinnings);
        assertWithMessage("incorrect ticket winnings")
                .that(ticketWinnings.equalsIgnoreCase("2.00"))
                .isTrue();

        gamingHistory.clickTicketInGamingHistory(2);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchZeroPlusOne);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchZeroPlusOne)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("11\n" +
                            "12\n" +
                            "13\n" +
                            "14\n" +
                            "20\n" +
                            "1\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                .that(ticket.getWinnings().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect ticket winnings")
                .that(ticket.getTicketWinnings().equalsIgnoreCase("2.00"))
                .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1599 :: Mobile - iLottery - Megamillions - Winnings Match Five Plus One - Megaplier - Megaplier 2")
    public void QAAUT$1599$MegamillionsWinningsMatchFivePlusOneMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        gamingHistory.getTicketInfo(1);
        System.out.println(gamingHistory.getTicketInfo(1));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(1).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(1).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(1).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("3.00"))
                .isTrue();

        //Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(1).get(4).substring(11);
                System.out.println(ticketWinnings);
                assertWithMessage("incorrect ticket winnings")
                        .that(ticketWinnings.equalsIgnoreCase(""))
                        .isTrue();

        gamingHistory.clickTicketInGamingHistory(1);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchFivePlusOneMegaplier);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchFivePlusOneMegaplier)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "3\n" +
                            "4\n" +
                            "5\n" +
                            "1\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("3.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                        .that(ticket.getWinnings().equalsIgnoreCase("2.00"))
                        .isTrue();

                assertWithMessage("incorrect ticket winnings")
                        .that(ticket.getTicketWinnings().equalsIgnoreCase("2.00"))
                        .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1600 :: Mobile - iLottery - Megamillions - Winnings Match Five - Megaplier 2")
    public void QAAUT$1600$MegamillionsWinningsMatchFiveMegaplier2(WebDriver webDriver){

        logInCanvas(webDriver, canvasUrl, username, password);
        HomePage homePage = new HomePage(webDriver);
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.clickLoadMoreButton();
        //gamingHistory.getTicketInfo(9);
        gamingHistory.getTicketInfo(0);
        System.out.println(gamingHistory.getTicketInfo(0));

        //WIN
        String ticketStatus = String.valueOf(gamingHistory.getTicketInfo(0).get(0));
        assertWithMessage("incorrect ticket status")
                .that(ticketStatus.equalsIgnoreCase("WIN"))
                .isTrue();

        //DATE
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String today = formatTime.format(date);
        System.out.println(today);
        String ticketDate = String.valueOf(gamingHistory.getTicketInfo(0).get(2));
        System.out.println(ticketDate);
        assertWithMessage("incorrect ticket date")
                .that(ticketDate.contains(today))
                .isTrue();

        //COST
        String ticketCost = gamingHistory.getTicketInfo(0).get(3).substring(7);
        System.out.println(ticketCost);
        assertWithMessage("incorrect ticket cost")
                .that(ticketCost.equalsIgnoreCase("3.00"))
                .isTrue();

       // Winnings
        String ticketWinnings = gamingHistory.getTicketInfo(0).get(4).substring(11);
                System.out.println(ticketWinnings);
                assertWithMessage("incorrect ticket winnings")
                        .that(ticketWinnings.equalsIgnoreCase(""))
                        .isTrue();


        gamingHistory.clickTicketInGamingHistory(0);

        Ticket ticket = new Ticket(webDriver);
        System.out.println(matchFive);
        System.out.println(ticket.getTicketId());
        assertWithMessage("incorrect gameId")
                        .that(matchFive)
                        .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            System.out.println(game);
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "3\n" +
                            "4\n" +
                            "5\n" +
                            "1\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect winnings details")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(today))
                .isTrue();

        System.out.println(ticket.getWinningNumbers());
        assertWithMessage("incorrect winning numbers")
                .that(ticket.getWinningNumbers())
                .isEqualTo("1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\n" +
                        "1\n" +
                        "Megaplier: x2");

        assertWithMessage("incorrect ticket cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("3.00"))
                .isTrue();

        assertWithMessage("incorrect winnings")
                        .that(ticket.getWinnings().equalsIgnoreCase("2.00"))
                        .isTrue();

                assertWithMessage("incorrect ticket winnings")
                        .that(ticket.getTicketWinnings().equalsIgnoreCase("2.00"))
                        .isTrue();

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("STANDARD"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("1");

        assertWithMessage("incorrect status")
                .that(ticket.getTicketStatus().equalsIgnoreCase("WIN"))
                .isTrue();

        assertWithMessage("incorrect drawingId")
                .that(ticket.getWinningsText(Properties.getPropertyValue("megamillions.id")).contains(ticket.getDrawingId()))
                .isTrue();

    }

}
