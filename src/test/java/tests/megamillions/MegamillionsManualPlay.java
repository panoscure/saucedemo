package tests.megamillions;

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
import canvas.page.objects.games.MegaMillionsGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.JustTheJackpotConfirmationModal;
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
public class MegamillionsManualPlay extends BaseTest {

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
    @DisplayName("QAAUT-1614 :: MegaMillions Game Manual Play")
    public void QAAUT$1614$MegaMillionsGameManualPlay(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        select5NumbersToPlay(webDriver, 6);
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        megaMillionsGamesManualPlay.selectOneNumberToPlay("8");

        BigDecimal betAmount = new BigDecimal("2.00");

        //assert quick pick MegaMillions page elements
        int numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        BigDecimal totalBetCost = betAmount;
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        megaMillionsGamesManualPlay.clickPlayNowButton();

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
    @DisplayName("QAAUT-1616 :: MegaMillions Game Manual Play + Megaplier")
    public void QAAUT$1616$MegaMillionsGameManualPlayMegaplier(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        select5NumbersToPlay(webDriver, 11);
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        megaMillionsGamesManualPlay.selectOneNumberToPlay("17");

        BigDecimal betAmount = new BigDecimal("2.00");

        //assert quick pick MegaMillions page elements
        int numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        megaMillionsGamesManualPlay.clickMegaplierToggle();

        betAmount = betAmount.add(new BigDecimal("1.00"));

        //assert quick pick MegaMillions page elements
        numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        BigDecimal totalBetCost = betAmount;
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        megaMillionsGamesManualPlay.clickPlayNowButton();

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
    @DisplayName("QAAUT-1655 :: MegaMillions Game Just the Jackpot-Multi Days 2")
    public void QAAUT$1655$MegaMillionsGameJustTheJackpotMultiDays2(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        select5NumbersToPlay(webDriver, 13);
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        megaMillionsGamesManualPlay.setMultiDaysTo2()
                .selectJustTheJackpot();
        JustTheJackpotConfirmationModal justTheJackpotConfirmationModal = new JustTheJackpotConfirmationModal(webDriver);
        justTheJackpotConfirmationModal.clickButtonInModal("Yes");

        BigDecimal betAmount = new BigDecimal("3.00");

        //assert quick pick MegaMillions page elements
        int numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String numberOfDaysToBet = megaMillionsGamesManualPlay.getNumberOfDraws().replace("x ", "");
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(megaMillionsGamesManualPlay.getYourPlaysList().size());
        BigDecimal numOfDaysAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = betAmount.multiply(numOfGamesAsBigDecimal).multiply(numOfDaysAsBigDecimal);
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        megaMillionsGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("megamillions.id");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOf5Plus1GamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOf5Plus1GamesInTheTicket().size())
                .isEqualTo(2);

        assertWithMessage("incorrect game")
                .that(ticket.isMegaMillionsJustTheJackpotGame())
                .isTrue();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains(
                            "Quick Pick"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("Quick Pick"))
                .isTrue();

        assertWithMessage("incorrect count of boards")
                .that(ticket.getNumberOfBoardsInGame())
                .isEqualTo("2");

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + totalBetCost.toString()))
                .isTrue();


    }
    

    @Test
    @DisplayName("QAAU23-24 :: iLottery - Play Mega Millions")
    public void QAAU23_24$iLotteryPlayMegaMillions(WebDriver webDriver) throws ParseException {

        String bearerToken = Tokens.getBearerToken();
        String sessionToken = Tokens.getSessionToken(bearerToken);
        HashMap<String, String> headers = Headers.getHeadersWithSessionToken(bearerToken, sessionToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, Properties.getPropertyValue("megamillions.game.id")))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        int activeDraw = getTheActiveDraw.getDrawId();

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);

        assertWithMessage("incorrect game")
                .that(megaMillionsGamesManualPlay.isGamePageCorrect("megamillions"))
                .isTrue();

        select5SequentialNumbersToPlay(webDriver, 1);

        megaMillionsGamesManualPlay.selectOneNumberToPlay("1");

        BigDecimal betAmount = new BigDecimal("2.00");

        int numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        megaMillionsGamesManualPlay.clickAddPlayButton();

        numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(2);

        select5SequentialNumbersToPlay(webDriver, 66);

        megaMillionsGamesManualPlay.selectOneNumberToPlay("25");
        List<String> firstBoardNumbersPlayed = getNumbersPlayedPerGame(webDriver, 0);
        List<String> secondBoardNumbersPlayed = getNumbersPlayedPerGame(webDriver, 6);

        megaMillionsGamesManualPlay.setMultiDaysTo4();
        int multiDrawings = 4;
        megaMillionsGamesManualPlay.clickMegaplierToggle();

        betAmount = betAmount.add(new BigDecimal(1)).multiply(new BigDecimal(numberOfGames)).multiply(new BigDecimal(4));
        String totalBetCost = "24.00";

        assertWithMessage("total bet cost is incorrect")
                .that(betAmount)
                .isEqualTo(new BigDecimal(totalBetCost));

        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost))
                .isTrue();

        String drawingDateFrom = megaMillionsGamesManualPlay.getDrawingDateFrom();
        String drawingDateTo = megaMillionsGamesManualPlay.getDrawingDateTo();
        long drawingDateFromInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(drawingDateFrom);
        long drawingDateToInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(drawingDateTo);

        megaMillionsGamesManualPlay.clickPlayNowButton();


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
                .isEqualTo("4");

        String isMegaplierEnabledInConfirmationModal = playGameConfirmationModal.gameContainsMegaplier();
        assertWithMessage("megaplier value is incorrect")
                .that(isMegaplierEnabledInConfirmationModal.equalsIgnoreCase("YES"))
                .isTrue();

        String isJustTheJackPotInConfirmationModal = playGameConfirmationModal.megamillionsContainsJustTheJackPot();
        assertWithMessage("just the jackpot value is incorrect")
                .that(isJustTheJackPotInConfirmationModal.equalsIgnoreCase("NO"))
                .isTrue();

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

        String multiDrawingsInThankYouModal = thankYouForPlayingModal.getMultiDraws();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(multiDrawingsInThankYouModal)
                .isEqualTo("4");

        String drawingDateFromInThankYouModal = thankYouForPlayingModal.getDrawingDateFrom();
        String drawingDateToInThankYouModal = thankYouForPlayingModal.getDrawingDateTo();
        long drawingDateFromInThankYouModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateFromInThankYouModal);
        long drawingDateToInThankYouModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateToInThankYouModal);
        assertThat(drawingDateFromInThankYouModalEpochTime).isEqualTo(drawingDateFromInDcGameScreenEpoch);
        assertThat(drawingDateToInThankYouModalEpochTime).isEqualTo(drawingDateToInDcGameScreenEpoch);

        String isMegaplierEnabledInThankYouModal = thankYouForPlayingModal.gameContainsMegaplier();
        assertWithMessage("megaplier value is incorrect")
                .that(isMegaplierEnabledInThankYouModal.equalsIgnoreCase("YES"))
                .isTrue();

        String isJustTheJackPotInThankYouModal = thankYouForPlayingModal.megamillionsContainsJustTheJackPot();
        assertWithMessage("just the jackpot value is incorrect")
                .that(isJustTheJackPotInThankYouModal.equalsIgnoreCase("NO"))
                .isTrue();

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

        Boolean isGameWithMegaplier = ticketCheckerModal.isMegamillionsGamePlusMegaplier();
        assertWithMessage("megaplier is not visible")
                .that(isGameWithMegaplier)
                .isTrue();

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
