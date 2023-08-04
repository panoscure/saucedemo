package tests.dc4;

import apigatewayj.EpochTimeConverter;
import apigatewayj.Headers;
import apigatewayj.Tokens;
import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.TicketCheckerPage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.dc.DCGamesManualPlay;
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
public class DC4ManualPlay extends BaseTest {

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
    @DisplayName("QAAUT-1610 :: DC4 Game Client | Play DC4 game | Straight")
    public void QAAUT$1610$DC4GameClientPlayDC4GameStraight(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"));
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver,2, 4);
        dcGamesManualPlay.deselectDrawTime("Select All", Properties.getPropertyValue("dc4.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        dcGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String gameId = thankYouForPlayingModal.getTicketId();
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1481 :: DC4 Game Client | Play DC4 game | Box")
    public void QAAUT$1481$DC4GameClientPlayDC4GameBox(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver,2, 4);
        dcGamesManualPlay.deselectDrawTime("Select All", Properties.getPropertyValue("dc4.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        dcGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String gameId = thankYouForPlayingModal.getTicketId();
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1482 :: DC4 Game Client | Play DC4 game | Straight/Box-Day")
    public void QAAUT$1482$DC4GameClientPlayStraightDC4GameStraightBoxDay(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                        .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 3, 4);
        dcGamesManualPlay.deselectDrawTime("Day", Properties.getPropertyValue("dc4.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("1"))
                        .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                        .isTrue();
        dcGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        String gamePlayed = playGameConfirmationModal.getPlayedBetType();
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String gameId = thankYouForPlayingModal.getTicketId();
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("dc4");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains(gamePlayed + "\n" + "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + totalBetCost.toString()))
                .isTrue();
    }

    @Test
    @DisplayName("QAAUT-1483 :: DC4 Game Client | Play DC4 game | Front Three-Night-Multiplier $1.00")
    public void QAAUT$1483$DC4GameClientPlayDC4GameFrontThreeNightMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionFrontPairDC3DC5FrontThreeDC4()
                .clickToExpandMultiplierDropdown()
                .setMultiplierOptionTo$1();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.enterNumberToPlay(0, "3")
                .enterNumberToPlay(1, "9")
                .enterNumberToPlay(2, "9")
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc4.game.id"));

        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("1"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal frontPairTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal).multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-4")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(frontPairTotalCost);
        dcGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(frontPairTotalCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1484 :: DC4 Game Client | Play DC4 game | Back Three-Both-Multiplier $1.00")
    public void QAAUT$1484$DC4GameClientPlayDC4GameBackThreeBothMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBackPairDC3DC5BackThreeDC4()
                .clickToExpandMultiplierDropdown()
                .setMultiplierOptionTo$1();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.enterNumberToPlay(1, "9")
                .enterNumberToPlay(2, "9")
                .enterNumberToPlay(3, "9")
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc4.game.id"));

        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal backPairTotalCost = new BigDecimal("2.00");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal).multiply(numOfGamesAsBigDecimal);
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-4")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(backPairTotalCost);
        dcGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        String gamePlayed = playGameConfirmationModal.getPlayedBetType();
        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String gameId = thankYouForPlayingModal.getTicketId();
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(backPairTotalCost));

        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("dc4");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains( gamePlayed + "\n" + "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + totalBetCost.toString()))
                .isTrue();
    }

    @Test
    @DisplayName("QAAUT-1494 :: DC4 Game Client | Play DC4 game | Straight/Box-Day | Quick Pick")
    public void QAAUT$1494$DC4GameClientPlayStraightDC4GameStraightBoxDayQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection()
                .deselectDrawTime("Day", Properties.getPropertyValue("dc4.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("1"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        dcGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAU23-18 :: iLottery - Play DC-4")
    public void QAAU23_18$iLotteryPlayDC4(WebDriver webDriver) throws ParseException {

        String bearerToken = Tokens.getBearerToken();
        String sessionToken = Tokens.getSessionToken(bearerToken);
        HashMap<String, String> headers = Headers.getHeadersWithSessionToken(bearerToken, sessionToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, Properties.getPropertyValue("dc4.game.id")))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        int activeDraw = getTheActiveDraw.getDrawId();

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-4");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"));

        enterNumbersToPlayDCGamesIncremental(webDriver, 1, 4);
        dcGamesManualPlay.enableLuckySum();

        BigDecimal straightGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal().add(new BigDecimal("0.50"));

        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        dcGamesManualPlay.clickAddPlayButton();

        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBox()
                .clickToExpandMultiplierDropdownList()
                .setMultiplierOptionTo$1()
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc4.game.id"));
        enterNumbersToPlayDCGamesIncremental(webDriver, 2, 4);
        BigDecimal boxGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal();
        numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(2);

        dcGamesManualPlay.setMultiDaysTo2();
        int numOfDraws = 4;

        List<String> firstBoardNumbersPlayed = getNumbersPlayed(webDriver,0, 3);
        List<String> secondBoardNumbersPlayed = getNumbersPlayed(webDriver,4, 3);

        BigDecimal betAmount = straightGameCost.add(boxGameCost).multiply(new BigDecimal(numOfDraws));
        String totalBetCost = "8.00";

        assertWithMessage("total bet cost is incorrect")
                .that(betAmount)
                .isEqualTo(new BigDecimal(totalBetCost));

        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost))
                .isTrue();

        String drawingDateFrom = dcGamesManualPlay.getDrawingDateFrom();
        String drawingDateTo = dcGamesManualPlay.getDrawingDateTo();
        long drawingDateFromInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(drawingDateFrom);
        long drawingDateToInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(drawingDateTo);

        dcGamesManualPlay.clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        String modalTitle = playGameConfirmationModal.getModalTitle();
        assertWithMessage("confirmation window is not visible")
                .that(modalTitle.equalsIgnoreCase("CONFIRMATION"))
                .isTrue();

        List<String> firstBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 0, 3);
        List<String> secondBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 4, 3);
        assertWithMessage("incorrect numbers played")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInConfirmationModal);
        assertWithMessage("incorrect numbers played")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInConfirmationModal);

        String multiDrawingsInConfirmationModal = playGameConfirmationModal.getMultiDraws();
        assertWithMessage("draws in confirmation window is incorrect")
                .that(multiDrawingsInConfirmationModal)
                .isEqualTo("4");

        String costInConfirmationModal = playGameConfirmationModal.getGameCost();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInConfirmationModal)
                .isEqualTo(totalBetCost);

        assertWithMessage("lucky sum is not shown")
                .that(playGameConfirmationModal.isLuckySumEnabled())
                .isTrue();
        String sumInConfirmationModal = playGameConfirmationModal.getLuckySum();
        assertWithMessage("lucky sum in confirmation window is incorrect")
                .that(sumInConfirmationModal)
                .isEqualTo("10");

        assertThat(playGameConfirmationModal.getPlayTypeOfBoard(0)).isEqualTo("Straight");
        assertThat(playGameConfirmationModal.getPlayTypeOfBoard(1)).isEqualTo("Box - 24 Ways");

        int drawTo = activeDraw + 5;
        assertThat(playGameConfirmationModal.getDrawings()).isEqualTo(activeDraw + " - " + drawTo);

        String drawingDateFromInModal = playGameConfirmationModal.getDrawingDateFrom();
        String drawingDateToInModal = playGameConfirmationModal.getDrawingDateTo();
        long drawingDateFromInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateFromInModal);
        long drawingDateToInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateToInModal);

        assertThat(drawingDateFromInModalEpochTime).isEqualTo(drawingDateFromInDcGameScreenEpoch);
        assertThat(drawingDateToInModalEpochTime).isEqualTo(drawingDateToInDcGameScreenEpoch);

        playGameConfirmationModal.clickPurchaseButton();

        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String modalTitle1 = thankYouForPlayingModal.getModalTitle();
        assertWithMessage("bet is not placed")
                .that(modalTitle1.equalsIgnoreCase("Thank You"))
                .isTrue();

        List<String> firstBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 0, 3);
        List<String> secondBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 4, 3);
        assertWithMessage("incorrect numbers played")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInThankYouModal);
        assertWithMessage("incorrect numbers played")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInThankYouModal);

        String ticketId = thankYouForPlayingModal.getTicketId();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(ticketId)
                .isNotEmpty();

        String multiDrawingsInThankYouModal = thankYouForPlayingModal.getMultiDraws();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(multiDrawingsInThankYouModal)
                .isEqualTo("4");

        assertWithMessage("lucky sum is not shown")
                .that(thankYouForPlayingModal.isLuckySumEnabled())
                .isTrue();
        String sumInThankYouModal = thankYouForPlayingModal.getLuckySum();
        assertWithMessage("lucky sum in confirmation window is incorrect")
                .that(sumInThankYouModal)
                .isEqualTo("10");

        assertThat(thankYouForPlayingModal.getPlayTypeOfBoard(0)).isEqualTo("Straight");
        assertThat(thankYouForPlayingModal.getPlayTypeOfBoard(1)).isEqualTo("Box - 24 Ways");

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

        List<String> firstBoardNumbersPlayedInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 0, 3);
        List<String> secondBoardNumbersPlayedInInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 4, 3);
        assertWithMessage("incorrect played numbers")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInTicketCheckerModal);
        assertWithMessage("incorrect played numbers")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInInTicketCheckerModal);

        assertWithMessage("lucky sum is not shown")
                .that(ticketCheckerModal.isLuckySumEnabled())
                .isTrue();
        String sumInTicketCheckerModal = ticketCheckerModal.getLuckySum();
        assertWithMessage("lucky sum in confirmation window is incorrect")
                .that(sumInTicketCheckerModal)
                .isEqualTo("10");

        String firstGameType = ticketCheckerModal.getFirstGameBetType();
        assertWithMessage("incorrect game type")
                .that(firstGameType)
                .isEqualTo("Play Type = \n" +
                        "Straight\n" +
                        "+ Lucky Sum\n" +
                        "Standard");

        String secondGameType = ticketCheckerModal.getSecondGameBetType();
        assertWithMessage("incorrect game type")
                .that(secondGameType)
                .isEqualTo("Play Type = \n" +
                        "Box - 24 Ways\n" +
                        "Standard");

        List<Integer> drawsInCarousel = ticketCheckerPage.getAllDrawsInCarousel(numOfDraws);
        for (int drawNumber : drawsInCarousel) {
            assertThat(drawNumber).isAtLeast(activeDraw);
            assertThat(drawNumber).isAtMost(drawTo);
        }
        assertThat(drawsInCarousel.size()).isEqualTo(numOfDraws);
        assertThat(ticketCheckerPage.isArrowRightDisabled()).isTrue();

        String costInTicketCheckerModal = ticketCheckerModal.getGameCost();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInTicketCheckerModal)
                .isEqualTo(totalBetCost);

        ticketCheckerModal.clickCloseButton();
        homePage.clickUserIcon();
    }

}
