package tests.race2riches;

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
import canvas.page.objects.games.Race2RichesGamesManualPlay;
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
public class Race2RichesManualPlay extends BaseTest {

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
    @DisplayName("QAAUT-1613 :: Race2Riches Game Client | Play Race2Riches game | Win | Quick Pick")
    public void QAAUT$1613$Race2RichesGameClientPlayRace2RichesGameWinQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        assertWithMessage("incorrect game option")
                .that(race2RichesGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Win"));
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();


        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        homePage.clickUserIcon();

    }
    @Test
    @DisplayName("QAAUT-1497 :: Race2Riches Game Client | Play Race2Riches game | Show | Quick Pick")
    public void QAAUT$1497$Race2RichesGameClientPlayRace2RichesGameShowQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionShow();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();


        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1498 :: Race2Riches Game Client | Play Race2Riches game | Exacta")
    public void QAAUT$1498$Race2RichesGameClientPlayRace2RichesGameExacta(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionExactA();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "12")
                .clickNumberToSelectHorse(1, "3");


        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("12\n" +
                            "3\n" +
                            "Play Type = \n" +
                            "EXACTA\n" +
                            "Standard"))
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
    @DisplayName("QAAUT-1499 :: Race2Riches Game Client | Play Race2Riches game | Exacta Box | 2 Horses")
    public void QAAUT$1499$Race2RichesGameClientPlayRace2RichesGameExactaBox2Horses(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionExactABox();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "10")
                .clickNumberToSelectHorse(0, "6");

        BigDecimal race2RichesTotalCost = new BigDecimal("2.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("2.00");
        BigDecimal totalBetCost = new BigDecimal("2.00");
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        homePage.clickUserIcon();

    }


    @Test
    @DisplayName("QAAUT-1500 :: Race2Riches Game Client | Play Race2Riches game | Exacta Box | 3 Horses")
    public void QAAUT$1500$Race2RichesGameClientPlayRace2RichesGameExactaBox3Horses(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionExactABox();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "12")
                .clickNumberToSelectHorse(0, "11")
                .clickNumberToSelectHorse(0, "1");


        BigDecimal race2RichesTotalCost = new BigDecimal("6.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("6.00");

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(race2RichesTotalCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();
        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "11\n" +
                            "12\n" +
                            "Play Type = \n" +
                            "EXACTA BOX\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + race2RichesTotalCost.toString()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1501 :: ace2Riches Game Client | Play Race2Riches game | Show | 4 Horses")
    public void QAAUT$1501$Race2RichesGameClientPlayRace2RichesGameShow4Horses(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionShow();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "12")
                .clickNumberToSelectHorse(0, "3")
                .clickNumberToSelectHorse(0, "11")
                .clickNumberToSelectHorse(0, "1");


        BigDecimal race2RichesTotalCost = (new BigDecimal(multiplierValue)).multiply(new BigDecimal("4"));
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = (new BigDecimal(multiplierValue)).multiply(new BigDecimal("4"));

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(race2RichesTotalCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "3\n" +
                            "11\n" +
                            "12\n" +
                            "Play Type = \n" +
                            "SHOW\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + race2RichesTotalCost.toString()))
                .isTrue();


    }

    @Test
    @DisplayName("QAAUT-1502 :: Race2Riches Game Client | Play Race2Riches game | Quinella | Quick Pick")
    public void QAAUT$1502$Race2RichesGameClientPlayRace2RichesGameQuinellaQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionQuinella();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();


        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();
        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains("Play Type = \n" +
                            "QUINELLA\n" + "Quick Pick"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + race2RichesTotalCost.toString()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1503 :: Race2Riches Game Client | Play Race2Riches game | Trifecta | Quick Pick")
    public void QAAUT$1503$Race2RichesGameClientPlayRace2RichesGameTrifectaQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionTrifecta();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();


        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1504 :: Race2Riches Game Client | Play Race2Riches game | Trifecta Box | Quick Pick")
    public void QAAUT$1504$Race2RichesGameClientPlayRace2RichesGameTrifectaBoxQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionTrifectaBox();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        race2RichesGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();


        BigDecimal race2RichesTotalCost = new BigDecimal("3.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("3.00");

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(race2RichesTotalCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains("Play Type = \n" +
                            "TRIFECTA BOX\n" +
                            "Quick Pick"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + race2RichesTotalCost.toString()))
                .isTrue();
    }

    @Test
    @DisplayName("QAAUT-1505 :: Race2Riches Game Client | Play Race2Riches game | Trifecta Wheel | 4 Horses")
    public void QAAUT$1505$Race2RichesGameClientPlayRace2RichesGameTrifectaWheel4Horses(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionTrifectaWheel();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "2")
                .clickNumberToSelectHorse(1, "5")
                .clickNumberToSelectHorse(2, "11")
                .clickNumberToSelectHorse(0, "1");


        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("1.00");

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(race2RichesTotalCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickPlayNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1506 :: Race2Riches Game Client | Play Race2Riches game | Daily Double | 2 Horses")
    public void QAAUT$1506$Race2RichesGameClientPlayRace2RichesGameDailyDouble2Horses(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionDailyDouble();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "1")
                .clickNumberToSelectHorse(1, "6");


        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("1.00");
        BigDecimal totalBetCost = new BigDecimal("1.00");
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1507 :: Race2Riches Game Client | Play Race2Riches game | Superfecta | Quick Pick")
    public void QAAUT$1507$Race2RichesGameClientPlayRace2RichesGameSuperfectaQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionSuperfecta();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();


        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal);
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(race2RichesTotalCost);
        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains("Play Type = \n" +
                            "SUPERFECTA\n" +
                            "Quick Pick"))
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
    @DisplayName("QAAUT-1508 :: Race2Riches Game Client | Play Race2Riches game | Superfecta Box | Quick Pick")
    public void QAAUT$1508$Race2RichesGameClientPlayRace2RichesGameSuperfectaBoxQuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionSuperfectaBox();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.10"))
                .isTrue();

        race2RichesGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();


        BigDecimal race2RichesTotalCost = new BigDecimal("2.40");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("2.40");

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(race2RichesTotalCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickPlayNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1509 :: Race2Riches Game Client | Play Race2Riches game | Superfecta Wheel | 5 Horses")
    public void QAAUT$1509$Race2RichesGameClientPlayRace2RichesGameSuperfectaWheel5Horses(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionSuperfectaWheel();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "2")
                .clickNumberToSelectHorse(1, "5")
                .clickNumberToSelectHorse(2, "11")
                .clickNumberToSelectHorse(0, "1")
                .clickNumberToSelectHorse(3, "7");


        BigDecimal race2RichesTotalCost = new BigDecimal("1.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("1.00");

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(race2RichesTotalCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(race2RichesTotalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "5\n" +
                            "11\n" +
                            "7\n" +
                            "Play Type = \n" +
                            "SUPERFECTA WHEEL\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + race2RichesTotalCost.toString()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAUT-1665 :: Race2Riches Game Client | Play Race2Riches game | Superfecta Wheel 5 Horses + Bonus")
    public void QAAUT$1665$Race2RichesGameClientPlayRace2RichesGameSuperfectaWheel5HorsesPlusBonus(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionSuperfectaWheel();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "2")
                .clickNumberToSelectHorse(1, "5")
                .clickNumberToSelectHorse(2, "11")
                .clickNumberToSelectHorse(0, "1")
                .clickNumberToSelectHorse(3, "7")
                .clickBonusToggle();

        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal bonus = new BigDecimal("1.00");
        BigDecimal gameCostPerDay = new BigDecimal("1.00");
        BigDecimal totalCost = gameCostPerDay.add(bonus);

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalCost.toString()))
                .isTrue();
        assertWithMessage("bet cost is incorrect")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bonus is incorrect")
                .that(new BigDecimal(race2RichesGamesManualPlay.getBonusCost()))
                .isEqualTo(bonus);


        race2RichesGamesManualPlay.clickPlayNowButton();

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalCost));


        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase("1\n" +
                            "2\n" +
                            "5\n" +
                            "11\n" +
                            "7\n" +
                            "Play Type = \n" +
                            "SUPERFECTA WHEEL\n" +
                            "Standard"))
                    .isTrue();
        }

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect type")
                .that(ticket.getTicketType().equalsIgnoreCase("Standard"))
                .isTrue();


        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + totalCost.toString()))
                .isTrue();

    }

    @Test
    @DisplayName("QAAU23-26 :: iLottery - Play Race2Riches")
    public void QAAU23_26$iLotteryPlayRace2Riches(WebDriver webDriver) throws ParseException {

        String bearerToken = Tokens.getBearerToken();
        String sessionToken = Tokens.getSessionToken(bearerToken);
        HashMap<String, String> headers = Headers.getHeadersWithSessionToken(bearerToken, sessionToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, Properties.getPropertyValue("race2riches.game.id")))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        int activeDraw = getTheActiveDraw.getDrawId();

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        assertWithMessage("incorrect game option")
                .that(race2RichesGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Win"));
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "1");

        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());

        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);
        race2RichesGamesManualPlay.setMultiDays(20)
                .clickBonusToggle();

        String numOfDrawsString = "20";
        int numOfDraws = 20;

        String numberPlayed = race2RichesGamesManualPlay.getNumbersPlayed().get(0);

        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(race2RichesGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(numOfGamesAsBigDecimal);

        BigDecimal totalBetCost = multiplier.multiply(numOfGamesAsBigDecimal).multiply(new BigDecimal(numOfDrawsString)).multiply(new BigDecimal(2));
        String totalBetAsString = "40.00";
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(new BigDecimal(totalBetAsString));

        String raceDateFrom = race2RichesGamesManualPlay.getDrawingDateFrom();
        String raceDateTo = race2RichesGamesManualPlay.getDrawingDateTo();
        long drawingDateFromInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(raceDateFrom);
        long drawingDateToInDcGameScreenEpoch = EpochTimeConverter.convertHumanReadableWithoutCommaToEpoch(raceDateTo);

        race2RichesGamesManualPlay.clickPlayNowButton();


        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);
        String modalTitle = playGameConfirmationModal.getModalTitle();
        assertWithMessage("confirmation window is not visible")
                .that(modalTitle.equalsIgnoreCase("CONFIRMATION"))
                .isTrue();

        String numberPlayedInConfirmationModal = playGameConfirmationModal.getListOfNumbersPlayed().get(0);

        assertWithMessage("incorrect numbers played")
                .that(numberPlayed)
                .isEqualTo(numberPlayedInConfirmationModal);

        assertThat(playGameConfirmationModal.getPlayTypeOfBoard(0)).isEqualTo("WIN");

        int drawTo = activeDraw + (numOfDraws - 1);
        assertThat(playGameConfirmationModal.getRaces()).isEqualTo(activeDraw + " - " + drawTo);

        String raceDateFromInModal = playGameConfirmationModal.getRaceDateFrom();
        String raceDateToInModal = playGameConfirmationModal.getRaceDateTo();
        long raceDateFromInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(raceDateFromInModal);
        long raceDateToInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(raceDateToInModal);
        assertThat(raceDateFromInModalEpochTime).isEqualTo(drawingDateFromInDcGameScreenEpoch);
        assertThat(raceDateToInModalEpochTime).isEqualTo(drawingDateToInDcGameScreenEpoch);

        String multiDrawingsInConfirmationModal = playGameConfirmationModal.getMultiRaces();
        assertWithMessage("draws in confirmation window is incorrect")
                .that(multiDrawingsInConfirmationModal)
                .isEqualTo(numOfDrawsString);

        assertWithMessage("bonus value is incorrect")
                .that(playGameConfirmationModal.isGameWithBonus())
                .isTrue();

        String costInConfirmationModal = playGameConfirmationModal.getGameCost();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInConfirmationModal)
                .isEqualTo(totalBetCost.toString());

        playGameConfirmationModal.clickPurchaseButton();


        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String modalTitle1 = thankYouForPlayingModal.getModalTitle();
        assertWithMessage("bet is not placed")
                .that(modalTitle1.equalsIgnoreCase("Thank You"))
                .isTrue();

        String numberPlayedInThankYouModal = thankYouForPlayingModal.getListOfNumbersPlayed().get(0);

        assertWithMessage("incorrect numbers played")
                .that(numberPlayed)
                .isEqualTo(numberPlayedInThankYouModal);

        assertThat(thankYouForPlayingModal.getPlayTypeOfBoard(0)).isEqualTo("WIN");

        String ticketId = thankYouForPlayingModal.getTicketId();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(ticketId)
                .isNotEmpty();

        String multiDrawingsInThankYouModal = thankYouForPlayingModal.getMultiRaces();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(multiDrawingsInThankYouModal)
                .isEqualTo(numOfDrawsString);

        String raceDateFromInThankYouModal = thankYouForPlayingModal.getRaceDateFrom();
        String raceDateToInThankYouModal = thankYouForPlayingModal.getRaceDateTo();
        long raceDateFromInThankYouModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(raceDateFromInThankYouModal);
        long raceDateToInThankYouModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(raceDateToInThankYouModal);
        assertThat(raceDateFromInThankYouModalEpochTime).isEqualTo(drawingDateFromInDcGameScreenEpoch);
        assertThat(raceDateToInThankYouModalEpochTime).isEqualTo(drawingDateToInDcGameScreenEpoch);

        assertWithMessage("bonus value is incorrect")
                .that(thankYouForPlayingModal.isGameWithBonus())
                .isTrue();

        thankYouForPlayingModal.clickOkButton();


        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickTicketCheckerInTopMenu();

        TicketCheckerPage ticketCheckerPage = new TicketCheckerPage(webDriver);

        ticketCheckerPage.enterTicketId(ticketId);
        ticketCheckerPage.clickCheckMyNumbersButton();

        TicketCheckerModal ticketCheckerModal = new TicketCheckerModal(webDriver);

        String numberOfPlays = ticketCheckerModal.getTicketDetail("Plays");
        assertWithMessage("game is not successful")
                .that(numberOfPlays)
                .isEqualTo("1");

        Boolean isGameWithPlusBonus = ticketCheckerModal.isRace2RichesGamePlusBonus();
        assertThat(isGameWithPlusBonus).isTrue();

        String numberPlayedInTicketCheckerModal = ticketCheckerModal.getListOfNumbersPlayed().get(0);
        assertWithMessage("incorrect numbers played")
                .that(numberPlayed)
                .isEqualTo(numberPlayedInTicketCheckerModal);

        String firstGameType = ticketCheckerModal.getFirstGameBetType();
        assertWithMessage("incorrect game type")
                .that(firstGameType)
                .isEqualTo("Play Type = \n" +
                        "WIN\n" +
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
                .isEqualTo(totalBetCost.toString());

        ticketCheckerModal.clickCloseButton();
        homePage.clickUserIcon();

    }

}
