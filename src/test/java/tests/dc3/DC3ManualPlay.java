package tests.dc3;

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
import org.openqa.selenium.WebElement;
import selenium.WebDriverInit;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static lottery.apigatewayj.DrawOperationsV3_1.getTheActiveDrawForAGame;

@ExtendWith(WebDriverInit.class)
public class DC3ManualPlay extends BaseTest {

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
    @DisplayName("QAAUT-1609 :: DC3 Game Client | Play DC3 game | Straight")
    public void QAAUT$1609$DC3GameClientPlayDC3GameStraight(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"));
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 2, 3);
        dcGamesManualPlay.deselectDrawTime("Evening", Properties.getPropertyValue("dc3.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal);

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
        gamingHistory.isGameCorrect("dc3");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOfDCGamesInTheTicket().size())
                .isEqualTo(1);

        for (String game : typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase(gamePlayed + "\n" + "Standard"))
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
    @DisplayName("QAAUT-1474 :: DC3 Game Client | Play DC3 game | Box")
    public void QAAUT$1474$DC3GameClientPlayDC3GameBox(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 2, 3);
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
        gamingHistory.isGameCorrect("dc3");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOfDCGamesInTheTicket().size())
                .isEqualTo(1);

        for (String game : typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.equalsIgnoreCase(gamePlayed + "\n" + "Standard"))
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
    @DisplayName("QAAUT-1476 :: DC3 Game Client | Play DC3 game | Straight/Box-Day")
    public void QAAUT$1476$DC3GameClientPlayStraightDC3GameStraightBoxDay(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.enterNumberToPlay(0, "3")
                .enterNumberToPlay(1, "3")
                .enterNumberToPlay(2, "8");
        dcGamesManualPlay.deselectDrawTime("Day", Properties.getPropertyValue("dc3.game.id"));
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
        gamingHistory.isGameCorrect("dc3");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        for (String game : typeOfGamesInTicket) {
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
    @DisplayName("QAAUT-1477 :: DC3 Game Client | Play DC3 game | Front Pair-Night-Multiplier $1.00")
    public void QAAUT$1477$DC3GameClientPlayDC3GameFrontPairNightMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

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

        dcGamesManualPlay.enterNumberToPlay(0, "8")
                .enterNumberToPlay(1, "9")
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc3.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3")))
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
    @DisplayName("QAAUT-1478 :: DC3 Game Client | Play DC3 game | Back Pair-Both-Multiplier $1.00")
    public void QAAUT$1478$DC3GameClientPlayDC3GameBackPairBothMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

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
                .enterNumberToPlay(2, "9");

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(backPairTotalCost);
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
                .isEqualTo(accountBalanceAfterBet.add(backPairTotalCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1479 :: DC3 Game Client | Play DC3 game | Combo 6 Ways -Both-Multiplier $0.50")
    public void QAAUT$1479$DC3GameClientPlayDC3GameCombo6WaysBothMultiplier$05(WebDriver webDriver) {
        //combo 6-ways
        BigDecimal comboThreeDifferentNumbersFactor = new BigDecimal(6);

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionComboDC3FrontThreeDC5();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 5, 3);
        //        dcGamesManualPlay.selectDrawTime("All");
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");

        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        BigDecimal comboTotalCost = new BigDecimal("6.00");
        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(comboThreeDifferentNumbersFactor).multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal).multiply(comboThreeDifferentNumbersFactor).multiply(numOfGamesAsBigDecimal);
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();

        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(comboTotalCost);

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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(comboTotalCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1480 :: DC3 Game Client | Play DC3 game | Combo 3 Ways -Both-Multiplier $0.50")
    public void QAAUT$1480$DC3GameClientPlayDC3GameCombo3WaysBothMultiplier$05(WebDriver webDriver) {
        //combo 3-ways
        BigDecimal comboFactor = new BigDecimal(3);

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionComboDC3FrontThreeDC5();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        dcGamesManualPlay.enterNumberToPlay(0, "3")
                .enterNumberToPlay(1, "3")
                .enterNumberToPlay(2, "8")
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc3.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        BigDecimal comboTotalCost = new BigDecimal("3.00");
        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal numOfGamesAsBigDecimal = new BigDecimal(dcGamesManualPlay.getYourPlaysList().size());
        BigDecimal gameCostPerDay = multiplier.multiply(comboFactor).multiply(numOfGamesAsBigDecimal);
        BigDecimal totalBetCost = multiplier.multiply(numOfDaysToBetAsBigDecimal).multiply(comboFactor).multiply(numOfGamesAsBigDecimal);
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        assertWithMessage("total bet cost is incorrect")
                .that(dcGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();

        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(comboTotalCost);

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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(comboTotalCost));

        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("dc3");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        for (String game : typeOfGamesInTicket) {
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
    @DisplayName("QAAUT-1495 :: DC3 Game Client | Play DC3 game | Back Pair-Both-Multiplier $1.00 | Quick Pick")
    public void QAAUT$1495$DC3GameClientPlayDC3GameBackPairBothMultiplier$1QuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

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

        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();

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

        assertThat(dcGamesManualPlay.getBetTotalPrice()).isEqualTo(totalBetCost.toString());

        assertWithMessage("bet is not successful")
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3")))
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
        gamingHistory.isGameCorrect("dc3");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();

        for (String game : typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains(gamePlayed + "\n" + "Quick Pick"))
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
    @DisplayName("QAAUT-1620 :: DC3 Game Client | Play DC3 game | Back Pair-Both-Multiplier $1.00 | Quick Pick | Multi Days 7")
    public void QAAUT$1620$DC3GameClientPlayDC3GameBackPairBothMultiplier1QuickPickMultiDays7(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

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

        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection()
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc3.game.id"))
                .setMultiDaysTo7();

        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("14"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal backPairTotalCost = new BigDecimal("14.00");
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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3")))
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
        gamingHistory.isGameCorrect("dc3");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();

        for (String game : typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains(gamePlayed + "\n" + "Quick Pick"))
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
    @DisplayName("QAAU23-13 :: iLottery - Play DC-3")
    public void QAAU23_13$iLotteryPlayDC3(WebDriver webDriver) throws ParseException {

        String bearerToken = Tokens.getBearerToken();
        String sessionToken = Tokens.getSessionToken(bearerToken);
        HashMap<String, String> headers = Headers.getHeadersWithSessionToken(bearerToken, sessionToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, Properties.getPropertyValue("dc3.game.id")))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        int activeDraw = getTheActiveDraw.getDrawId();

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"));

        enterNumbersToPlayDCGamesIncremental(webDriver, 1, 3);
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
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc3.game.id"));
        enterNumbersToPlayDCGamesIncremental(webDriver, 2, 3);
        BigDecimal boxGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal();
        numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(2);

        dcGamesManualPlay.setMultiDaysTo2();
        int numOfDrawsParticipating = 4;
        int numOfDrawsOverall = 6;

        List<String> firstBoardNumbersPlayed = getNumbersPlayed(webDriver, 0, 2);
        List<String> secondBoardNumbersPlayed = getNumbersPlayed(webDriver, 3, 2);

        BigDecimal betAmount = straightGameCost.add(boxGameCost).multiply(new BigDecimal(numOfDrawsParticipating));
        String totalBetCost = "8.00";

        assertWithMessage("total bet cost is incorrect")
                .that(betAmount)
                .isEqualTo(new BigDecimal(totalBetCost));

        assertThat(dcGamesManualPlay.getBetTotalPrice()).isEqualTo(totalBetCost);

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

        List<String> firstBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 0, 2);
        List<String> secondBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 3, 2);

        assertWithMessage("incorrect numbers played")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInConfirmationModal);
        assertWithMessage("incorrect numbers played")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInConfirmationModal);

        assertThat(playGameConfirmationModal.getPlayTypeOfBoard(0)).isEqualTo("Straight");
        assertThat(playGameConfirmationModal.getPlayTypeOfBoard(1)).isEqualTo("Box - 6 Ways");

        int drawTo = activeDraw + (numOfDrawsOverall - 1);
        assertThat(playGameConfirmationModal.getDrawings()).isEqualTo(activeDraw + " - " + drawTo);

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
                .isEqualTo("6");

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

        List<String> firstBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 0, 2);
        List<String> secondBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 3, 2);

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
                .isEqualTo("6");

        assertThat(thankYouForPlayingModal.getPlayTypeOfBoard(0)).isEqualTo("Straight");
        assertThat(thankYouForPlayingModal.getPlayTypeOfBoard(1)).isEqualTo("Box - 6 Ways");

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

        List<String> firstBoardNumbersPlayedInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 0, 2);
        List<String> secondBoardNumbersPlayedInInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 3, 2);

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
                .isEqualTo("6");

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
                        "Box - 6 Ways\n" +
                        "Standard");

        List<Integer> drawsInCarousel = ticketCheckerPage.getAllDrawsInCarousel(numOfDrawsParticipating);
        for (int drawNumber : drawsInCarousel) {
            assertThat(drawNumber).isAtLeast(activeDraw);
            assertThat(drawNumber).isAtMost(drawTo);
        }
        assertThat(drawsInCarousel.size()).isEqualTo(numOfDrawsParticipating);
        assertThat(ticketCheckerPage.isArrowRightDisabled()).isTrue();

        String costInTicketCheckerModal = ticketCheckerModal.getGameCost();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInTicketCheckerModal)
                .isEqualTo(totalBetCost);

        ticketCheckerModal.clickCloseButton();
        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAU23-344 :: iLottery - Play DC-3 - Variations")
    public void QAAU23$344$iLotteryPlayDC3Variations(WebDriver webDriver) {
        List<String> straightSelectionNumbers = Arrays.asList(new String[]{"1", "2", "3"});
        List<String> frontPairSelectionNumbers = Arrays.asList(new String[]{"1", "2", "-"});
        List<String> backPairSelectionNumbers = Arrays.asList(new String[]{"-", "3", "4"});
        List<String> comboSelectionNumbers = Arrays.asList(new String[]{"4", "5", "6"});

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickMultiplierSelectBox()
                .selectMultiplier("$1.00");
        //Bet selections (Play 1):
        //- Play type: Straight
        //- Play amount: $1.00
        //- Numbers selections: 1, 2, 3
        //- Lucky Sum option: Enabled LS=6
        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"));

        enterNumbersToPlayDCGamesIncremental(webDriver, 1, 3);
        dcGamesManualPlay.enableLuckySum();
        //multiplier (1.00) + lucky sum (1.00)
        BigDecimal straightGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal().add(new BigDecimal("2.00"));
        dcGamesManualPlay.clickAddPlayButton();

        //Bet selections (Play 2):
        //- Play type: Front Pair
        //- Play amount: $1.00
        //- Numbers selections: 1, 2
        //- Lucky Sum option: Disabled

        dcGamesManualPlay.clickPlayStyleSelectBox()
                .selectPlayStyle("Front Pair")
                .clickMultiplierSelectBox()
                .selectMultiplier("$1.00");
        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Front Pair"));
        enterNumbersToPlayDCGamesIncremental(webDriver, 1, 2);
        //multiplier (1.00)
        BigDecimal frontPairGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal().add(new BigDecimal("1.00"));
        dcGamesManualPlay.clickAddPlayButton();

        //Bet selections (Play 3):
        //- Play type: Back Pair
        //- Play amount: $0.50
        //- Numbers selections: 3, 4
        //- Lucky Sum option: Disabled
        dcGamesManualPlay.clickPlayStyleSelectBox()
                .selectPlayStyle("Back Pair")
                .clickMultiplierSelectBox()
                .selectMultiplier("$0.50");
        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Back Pair"));
        dcGamesManualPlay.enterNumberToPlay(1, "3");
        dcGamesManualPlay.enterNumberToPlay(2, "4");
        //multiplier (0.50)
        BigDecimal backPairGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal().add(new BigDecimal("0.50"));
        dcGamesManualPlay.clickAddPlayButton();

        //Bet selections (Play 3):
        //- Play type: Back Pair
        //- Play amount: $0.50
        //- Numbers selections: 3, 4
        //- Lucky Sum option: Disabled
        dcGamesManualPlay.clickPlayStyleSelectBox()
                .selectPlayStyle("Combo")
                .clickMultiplierSelectBox()
                .selectMultiplier("$0.50");
        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Combo"));
        enterNumbersToPlayDCGamesIncremental(webDriver, 4, 3);
        //multiplier (0.50)
        BigDecimal comboGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal().add(new BigDecimal("0.50"));

        BigDecimal totalGameCost = comboGameCost.add(backPairGameCost).add(frontPairGameCost).add(straightGameCost);

        dcGamesManualPlay.selectAdvancePlay("Today")
                .deselectDrawTime("Night", Properties.getPropertyValue("dc3.game.id"))
                .setMultiDaysTo2()
                .clickPlayNowButton();

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);

        List<WebElement> getPlayedBoards = playGameConfirmationModal.getSelectedBoards();
        assertWithMessage("Played draws should have been 4 but found: " + getPlayedBoards.size())
                .that(getPlayedBoards.size())
                .isEqualTo(4);

        /** Board One Assertion **/
        WebElement straightBetBoard = getPlayedBoards.get(0);
        List<String> straightNumbersPlayedOnBoard = playGameConfirmationModal.getPlayedNumberAccordingToBoard(straightBetBoard);
        assertWithMessage("Straight selection numbers are incorrect")
                .that(straightNumbersPlayedOnBoard).containsAtLeastElementsIn(straightSelectionNumbers);

        Boolean isLuckySumEnabledOnBoard = playGameConfirmationModal.isLuckySumEnabledAccordingToBoard(straightBetBoard);
        assertWithMessage("Lucky Sum Is Not True")
                .that(isLuckySumEnabledOnBoard).isTrue();

        String getLuckySumOnBoard = playGameConfirmationModal.getLuckySumAccordingToBoard(straightBetBoard);
        assertWithMessage("lucky sum in confirmation window is incorrect")
                .that(getLuckySumOnBoard)
                .isEqualTo("6");

        String getPlayedStyleOnBoard = playGameConfirmationModal.getPlayedStyleAccordingToBoard(straightBetBoard);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(getPlayedStyleOnBoard)
                .isEqualTo("Straight");

        /** Board One Assertion **/

        /** Board Two Assertion **/
        WebElement frontPairBetBoard = getPlayedBoards.get(1);
        List<String> frontPairNumbersPlayedOnBoard = playGameConfirmationModal.getPlayedNumberAccordingToBoard(frontPairBetBoard);
        assertWithMessage("Straight selection numbers are incorrect")
                .that(frontPairNumbersPlayedOnBoard).containsAtLeastElementsIn(frontPairSelectionNumbers);

        Boolean frontPairIsLuckySumEnabledOnBoard = playGameConfirmationModal.isLuckySumEnabledAccordingToBoard(frontPairBetBoard);
        assertWithMessage("Straight selection numbers are incorrect")
                .that(frontPairIsLuckySumEnabledOnBoard).isFalse();

        String frontPairGetPlayedStyleOnBoard = playGameConfirmationModal.getPlayedStyleAccordingToBoard(frontPairBetBoard);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(frontPairGetPlayedStyleOnBoard)
                .isEqualTo("Front Pair");
        /** Board Two Assertion **/

        /** Board Three Assertion **/
        WebElement backPairBetBoard = getPlayedBoards.get(2);
        List<String> backPairNumbersPlayedOnBoard = playGameConfirmationModal.getPlayedNumberAccordingToBoard(backPairBetBoard);
        assertWithMessage("Back Pair selection numbers are incorrect")
                .that(backPairNumbersPlayedOnBoard).containsAtLeastElementsIn(backPairSelectionNumbers);

        Boolean backPairIsLuckySumEnabledOnBoard = playGameConfirmationModal.isLuckySumEnabledAccordingToBoard(backPairBetBoard);
        assertWithMessage("Back Pair selection numbers are incorrect")
                .that(backPairIsLuckySumEnabledOnBoard).isFalse();

        String backPairGetPlayedStyleOnBoard = playGameConfirmationModal.getPlayedStyleAccordingToBoard(backPairBetBoard);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(backPairGetPlayedStyleOnBoard)
                .isEqualTo("Back Pair");
        /** Board Three Assertion **/

        /** Board Four Assertion **/
        WebElement comboBetBoard = getPlayedBoards.get(3);
        List<String> comboNumbersPlayedOnBoard = playGameConfirmationModal.getPlayedNumberAccordingToBoard(comboBetBoard);
        assertWithMessage("Combo selection numbers are incorrect")
                .that(comboNumbersPlayedOnBoard).containsAtLeastElementsIn(comboSelectionNumbers);

        Boolean comboIsLuckySumEnabledOnBoard = playGameConfirmationModal.isLuckySumEnabledAccordingToBoard(comboBetBoard);
        assertWithMessage("Combo selection numbers are incorrect")
                .that(comboIsLuckySumEnabledOnBoard).isFalse();

        String comboGetPlayedStyleOnBoard = playGameConfirmationModal.getPlayedStyleAccordingToBoard(comboBetBoard);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(comboGetPlayedStyleOnBoard)
                .isEqualTo("Combo");
        /** Board Four Assertion **/

        //- Multi drawings
        String multiDrawingsInConfirmationModal = playGameConfirmationModal.getMultiDraws();
        assertWithMessage("draws in confirmation window is incorrect")
                .that(multiDrawingsInConfirmationModal)
                .isEqualTo("2");

        playGameConfirmationModal.clickPurchaseButton();

        System.out.println("End");
    }
}
