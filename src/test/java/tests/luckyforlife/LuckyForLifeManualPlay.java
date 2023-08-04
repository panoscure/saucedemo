package tests.luckyforlife;

import apigatewayj.EpochTimeConverter;
import apigatewayj.Headers;
import apigatewayj.Tokens;
import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.TicketCheckerPage;
import canvas.page.objects.games.LuckyForLifeGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.PlayGameConfirmationModal;
import canvas.page.objects.modals.ThankYouForPlayingModal;
import canvas.page.objects.modals.TicketCheckerModal;
import common.utils.Properties;
import io.restassured.response.Response;
import lottery.apigatewayj.models.infostore.get.the.active.drawfor.a.game.GetTheActiveDrawForAGameModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static lottery.apigatewayj.DrawOperationsV3_1.getTheActiveDrawForAGame;

@ExtendWith(WebDriverInit.class)
public class LuckyForLifeManualPlay extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.username.automation02");
    public String password = Properties.getPropertyValue("canvas.password.automation02");

    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1612 :: LuckyForLife Game Manual Play")
    public void QAAUT$1612$LuckyForLifeGameManualPlay(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("LUCKY FOR LIFE");

        select5NumbersToPlay(webDriver, 6);
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);
        luckyForLifeGamesManualPlay.selectOneNumberToPlay("8");

        String betAmount = "2.00";
        //assert
        int numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        BigDecimal totalBetCost = new BigDecimal(betAmount);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(luckyForLifeGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        luckyForLifeGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAU23-25 :: iLottery - Play Lucky For Life")
    public void QAAU23_25$iLotteryPlayLuckyForLife(WebDriver webDriver) throws ParseException {

        String bearerToken = Tokens.getBearerToken();
        String sessionToken = Tokens.getSessionToken(bearerToken);
        HashMap<String, String> headers = Headers.getHeadersWithSessionToken(bearerToken, sessionToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, Properties.getPropertyValue("luckyforlife.game.id")))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        int activeDraw = getTheActiveDraw.getDrawId();

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("LUCKY FOR LIFE");
        LuckyForLifeGamesManualPlay luckyForLifeGamesManualPlay = new LuckyForLifeGamesManualPlay(webDriver);

        assertWithMessage("incorrect game")
                .that(luckyForLifeGamesManualPlay.isGamePageCorrect(""))
                .isTrue();

        select5SequentialNumbersToPlay(webDriver, 1);

        luckyForLifeGamesManualPlay.selectOneNumberToPlay("1");

        BigDecimal betAmount = new BigDecimal("2.00");

        int numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        luckyForLifeGamesManualPlay.clickAddPlayButton();

        numberOfGames = luckyForLifeGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(2);

        select5SequentialNumbersToPlay(webDriver, 44);

        luckyForLifeGamesManualPlay.selectOneNumberToPlay("18");
        List<String> firstBoardNumbersPlayed = getNumbersPlayedPerGame(webDriver, 0);
        List<String> secondBoardNumbersPlayed = getNumbersPlayedPerGame(webDriver, 6);

        luckyForLifeGamesManualPlay.setMultiDaysTo20();
        int multiDrawings = 20;

        String drawingDates = luckyForLifeGamesManualPlay.getDrawingDates();

        betAmount = betAmount.multiply(new BigDecimal(numberOfGames)).multiply(new BigDecimal(20));
        String totalBetCost = "80.00";

        assertWithMessage("total bet cost is incorrect")
                .that(betAmount)
                .isEqualTo(new BigDecimal(totalBetCost));

        assertWithMessage("total bet cost is incorrect")
                .that(luckyForLifeGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost))
                .isTrue();

        String drawingDateFrom = luckyForLifeGamesManualPlay.getDrawingDateFrom();
        String drawingDateTo = luckyForLifeGamesManualPlay.getDrawingDateTo();
        long drawingDateFromInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(drawingDateFrom);
        long drawingDateToInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(drawingDateTo);

        luckyForLifeGamesManualPlay.clickPlayNowButton();


        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        String modalTitle = playGameConfirmationModal.getModalTitle();
        assertWithMessage("confirmation window is not visible")
                .that(modalTitle.equalsIgnoreCase("CONFIRMATION"))
                .isTrue();

        List<String> firstBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 0, 5);
        List<String> secondBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 6, 5);
        assertWithMessage("")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInConfirmationModal);
        assertWithMessage("")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInConfirmationModal);

        int drawTo = activeDraw + (multiDrawings - 1);
        assertThat(playGameConfirmationModal.getDrawings()).isEqualTo(activeDraw + " - " + drawTo);

        String drawingDateFromInModal = playGameConfirmationModal.getDrawingDateFrom();
        String drawingDateToInModal = playGameConfirmationModal.getDrawingDateTo();
        long drawingDateFromInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateFromInModal);
        long drawingDateToInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateToInModal);
        assertThat(drawingDateFromInModalEpochTime).isEqualTo(drawingDateFromInDcGameScreenEpoch);
        assertThat(drawingDateToInModalEpochTime).isEqualTo(drawingDateToInDcGameScreenEpoch);

        String multiDrawingsInConfirmationModal = playGameConfirmationModal.getMultiDraws();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(multiDrawingsInConfirmationModal)
                .isEqualTo("20");

        String costInConfirmationModal = playGameConfirmationModal.getGameCost();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInConfirmationModal)
                .isEqualTo(totalBetCost);

        playGameConfirmationModal.clickPurchaseButton();


        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String modalTitle1 = thankYouForPlayingModal.getModalTitle();
        assertWithMessage("bet is not placed")
                .that(modalTitle1.equalsIgnoreCase("Thank You"))
                .isTrue();

        List<String> firstBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 0, 5);
        List<String> secondBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 6, 5);
        assertWithMessage("")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInThankYouModal);
        assertWithMessage("")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInThankYouModal);

        String ticketId = thankYouForPlayingModal.getTicketId();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(ticketId)
                .isNotEmpty();

        String multiDrawingsInThankYouModal = playGameConfirmationModal.getMultiDraws();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(multiDrawingsInThankYouModal)
                .isEqualTo("20");

        String drawingDateFromInThankYouModal = thankYouForPlayingModal.getDrawingDateFrom();
        String drawingDateToInThankYouModal = thankYouForPlayingModal.getDrawingDateTo();
        long drawingDateFromInThankYouModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateFromInThankYouModal);
        long drawingDateToInThankYouModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateToInThankYouModal);
        assertThat(drawingDateFromInThankYouModalEpochTime).isEqualTo(drawingDateFromInDcGameScreenEpoch);
        assertThat(drawingDateToInThankYouModalEpochTime).isEqualTo(drawingDateToInDcGameScreenEpoch);

        thankYouForPlayingModal.clickOkButton();


        BigDecimal accountBalanceAfterBet = homePage.getBalance();

        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(betAmount));

        homePage.clickTicketCheckerInTopMenu();

        TicketCheckerPage ticketCheckerPage = new TicketCheckerPage(webDriver);

        ticketCheckerPage.enterTicketId(ticketId);
        ticketCheckerPage.clickCheckMyNumbersButton();

        TicketCheckerModal ticketCheckerModal = new TicketCheckerModal(webDriver);

        String numberOfPlays = ticketCheckerModal.getTicketDetail("Plays");
        assertWithMessage("game is not successful")
                .that(numberOfPlays)
                .isEqualTo("2");

        List<String> firstBoardNumbersPlayedInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 0, 5);
        List<String> secondBoardNumbersPlayedInInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 6, 5);

        assertWithMessage("")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInTicketCheckerModal);
        assertWithMessage("")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInInTicketCheckerModal);

        List<Integer> drawsInCarousel = ticketCheckerPage.getAllDrawsInCarousel(multiDrawings);
        for (int drawNumber : drawsInCarousel) {
            assertThat(drawNumber).isAtLeast(activeDraw);
            assertThat(drawNumber).isAtMost(drawTo);
        }
        assertThat(drawsInCarousel.size()).isEqualTo(multiDrawings);
        assertThat(ticketCheckerPage.isArrowRightDisabled()).isTrue();

        String costInTicketCheckerModal = ticketCheckerModal.getGameCost();

        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInTicketCheckerModal)
                .isEqualTo(totalBetCost);

        ticketCheckerModal.clickCloseButton();

        homePage.clickUserIcon();
    }
}
