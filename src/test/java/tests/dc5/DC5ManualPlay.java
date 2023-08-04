package tests.dc5;

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
public class DC5ManualPlay extends BaseTest {

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
    @DisplayName("QAAUT-1611 :: DC5 Game Client | Play DC5 game | Straight")
    public void QAAUT$1611$DC5GameClientPlayDC5GameStraight(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"));
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 2, 5);
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
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();
        String eventId = thankYouForPlayingModal.getTicketId();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1485 :: DC5 Game Client | Play DC5 game | Box")
    public void QAAUT$1485$DC5GameClientPlayDC5GameBox(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 2, 5);
        dcGamesManualPlay.deselectDrawTime("Select All", Properties.getPropertyValue("dc5.game.id"));
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
        String messageTitle = thankYouForPlayingModal.getModalTitle();

        assertWithMessage("bet is not placed")
                .that(messageTitle.equalsIgnoreCase("Thank You"))
                .isTrue();
        String eventId = thankYouForPlayingModal.getTicketId();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1486 :: DC5 Game Client | Play DC5 game | Straight/Box-Day")
    public void QAAUT$1486$DC5GameClientPlayStraightDC5GameStraightBoxDay(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        enterNumbersToPlayDCGamesIncremental(webDriver, 3, 5);
        dcGamesManualPlay.deselectDrawTime("Day", Properties.getPropertyValue("dc5.game.id"));
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
        String eventId = thankYouForPlayingModal.getTicketId();

        thankYouForPlayingModal.clickOkButton();

        BigDecimal accountBalanceAfterBet = homePage.getBalance();
        //assert balance after bet
        assertWithMessage("withdraw is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1487 :: DC5 Game Client | Play DC5 game | Front Pair-Night-Multiplier $1.00")
    public void QAAUT$1487$DC5GameClientPlayDC5GameFrontPairNightMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

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
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc5.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
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
    @DisplayName("QAAUT-1488 :: DC5 Game Client | Play DC5 game | Back Pair-Both-Multiplier $1.00")
    public void QAAUT$1488$DC5GameClientPlayDC5GameBackPairBothMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

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

        dcGamesManualPlay.enterNumberToPlay(3, "9")
                .enterNumberToPlay(4, "9")
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc5.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
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
    @DisplayName("QAAUT-1489 :: DC5 Game Client | Play DC5 game | Front Three-Night-Multiplier $1.00")
    public void QAAUT$1489$DC5GameClientPlayDC5GameFrontThreeNightMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionComboDC3FrontThreeDC5()
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
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc5.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
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
    @DisplayName("QAAUT-1490 :: DC5 Game Client | Play DC5 game | Back Pair-Both-Multiplier $1.00")
    public void QAAUT$1490$DC5GameClientPlayDC5GameBackThreeBothMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBackThreeDC5()
                .clickToExpandMultiplierDropdown()
                .setMultiplierOptionTo$1();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.enterNumberToPlay(2, "9")
                .enterNumberToPlay(3, "9")
                .enterNumberToPlay(4, "9")
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc5.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
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
    @DisplayName("QAAUT-1491 :: DC5 Game Client | Play DC5 game | Front Four-Night-Multiplier $1.00")
    public void QAAUT$1491$DC5GameClientPlayDC5GameFrontFourNightMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionFrontFourDC5()
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
                .enterNumberToPlay(3, "9")
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc5.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
                .isEqualTo(gameCostPerDay);

        assertWithMessage("bet is not successful")
                .that(totalBetCost)
                .isEqualTo(frontPairTotalCost);
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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(frontPairTotalCost));

        //gaming history
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickTabInAccountDrawer("WALLET");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("dc5");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        for (String game : typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains("Play Type = \n" +
                            "Front Four\n" +
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
    @DisplayName("QAAUT-1492 :: DC5 Game Client | Play DC5 game | Back Pair-Both-Multiplier $1.00")
    public void QAAUT$1492$DC5GameClientPlayDC5GameBackFourBothMultiplier$1(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBackFourDC5()
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
                .enterNumberToPlay(4, "9")

                .deselectDrawTime("Select All", Properties.getPropertyValue("dc5.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
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
    @DisplayName("QAAUT-1493 :: DC5 Game Client | Play DC5 game | Back Pair-Both-Multiplier $1.00 | Quick Pick")
    public void QAAUT$1493$DC5GameClientPlayDC5GameBackFourBothMultiplier$1QuickPick(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBackFourDC5()
                .clickToExpandMultiplierDropdown()
                .setMultiplierOptionTo$1();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection()
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc5.game.id"));

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
                .that(new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-5")))
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
    @DisplayName("QAAU23-20 :: iLottery - Play DC-5")
    public void QAAU23_20$iLotteryPlayDC5(WebDriver webDriver) throws ParseException {

        String bearerToken = Tokens.getBearerToken();
        String sessionToken = Tokens.getSessionToken(bearerToken);
        HashMap<String, String> headers = Headers.getHeadersWithSessionToken(bearerToken, sessionToken);
        GetTheActiveDrawForAGameModel getTheActiveDraw = ((Response) getTheActiveDrawForAGame(headers, Properties.getPropertyValue("dc5.game.id")))
                .then().statusCode(200).extract().as(GetTheActiveDrawForAGameModel.class);
        int activeDraw = getTheActiveDraw.getDrawId();

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"));

        List<Integer> numbersToPlayForFirstBoard = Arrays.asList(9,8,1,2,3);
        dcGamesManualPlay.enterNumberToPlayCustom(0, numbersToPlayForFirstBoard);

        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        dcGamesManualPlay.clickAddPlayButton();

        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionBox()
                .clickToExpandMultiplierDropdownList()
                .setMultiplierOptionTo$1();

        List<Integer> numbersToPlayForSecondBoard = Arrays.asList(1,6,2,5,3);
        dcGamesManualPlay.enterNumberToPlayCustom(1, numbersToPlayForSecondBoard);

        BigDecimal boxGameCost = dcGamesManualPlay.getSelectedOptionMultiplierValueAsBigDecimal();
        numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(2);

        dcGamesManualPlay.setMultiDaysTo2();
        int multiDays = 2;
        int numOfDraws = 4;

        List<String> firstBoardNumbersPlayed = getNumbersPlayed(webDriver, 0, 4);
        List<String> secondBoardNumbersPlayed = getNumbersPlayed(webDriver, 5, 4);

        BigDecimal totalBetCostBigDecimal = new BigDecimal(6);
        String totalBetCostString = "6.00";
        assertThat(dcGamesManualPlay.getBetTotalPrice()).isEqualTo(totalBetCostString);

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

        List<String> firstBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 0, 4);
        List<String> secondBoardNumbersPlayedInConfirmationModal = getNumbersPlayedPerGameInConfirmationModal(webDriver, 5, 4);
        assertWithMessage("incorrect numbers played")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInConfirmationModal);
        assertWithMessage("incorrect numbers played")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInConfirmationModal);

        assertThat(playGameConfirmationModal.getPlayTypeOfBoard(0)).isEqualTo("Straight");
        assertThat(playGameConfirmationModal.getPlayTypeOfBoard(1)).isEqualTo("Box - 120 Ways");

        int drawTo = activeDraw + (multiDays + 1);
        assertThat(playGameConfirmationModal.getDrawings()).isEqualTo(activeDraw + " - " + drawTo);

        String drawingDateFromInModal = playGameConfirmationModal.getDrawingDateFrom();
        String drawingDateToInModal = playGameConfirmationModal.getDrawingDateTo();
        long drawingDateFromInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateFromInModal);
        long drawingDateToInModalEpochTime = EpochTimeConverter.convertHumanReadableWithCommaToEpoch(drawingDateToInModal);
        assertThat(drawingDateFromInModalEpochTime).isEqualTo(drawingDateFromInDcGameScreenEpoch);
        assertThat(drawingDateToInModalEpochTime).isEqualTo(drawingDateToInDcGameScreenEpoch);

        String multiDrawingsInConfirmationModal = playGameConfirmationModal.getMultiDraws();
        assertWithMessage("draws in confirmation window is incorrect")
                .that(multiDrawingsInConfirmationModal)
                .isEqualTo("4");

        String costInConfirmationModal = playGameConfirmationModal.getGameCost();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInConfirmationModal)
                .isEqualTo(totalBetCostString);

        playGameConfirmationModal.clickPurchaseButton();


        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String modalTitle1 = thankYouForPlayingModal.getModalTitle();
        assertWithMessage("bet is not placed")
                .that(modalTitle1.equalsIgnoreCase("Thank You"))
                .isTrue();

        List<String> firstBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 0, 4);
        List<String> secondBoardNumbersPlayedInThankYouModal = getNumbersPlayedPerGameInThankYouModal(webDriver, 5, 4);
        assertWithMessage("incorrect numbers played")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInThankYouModal);
        assertWithMessage("incorrect numbers played")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInThankYouModal);

        assertThat(thankYouForPlayingModal.getPlayTypeOfBoard(0)).isEqualTo("Straight");
        assertThat(thankYouForPlayingModal.getPlayTypeOfBoard(1)).isEqualTo("Box - 120 Ways");

        String ticketId = thankYouForPlayingModal.getTicketId();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(ticketId)
                .isNotEmpty();

        String multiDrawingsInThankYouModal = thankYouForPlayingModal.getMultiDraws();
        assertWithMessage("draws in confirmation window is incorrect")
                .that(multiDrawingsInThankYouModal)
                .isEqualTo("4");

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
                .isEqualTo(accountBalanceAfterBet.add(totalBetCostBigDecimal));

        homePage.clickTicketCheckerInTopMenu();

        TicketCheckerPage ticketCheckerPage = new TicketCheckerPage(webDriver);

        ticketCheckerPage.enterTicketId(ticketId);
        ticketCheckerPage.clickCheckMyNumbersButton();

        TicketCheckerModal ticketCheckerModal = new TicketCheckerModal(webDriver);

        String numberOfPlays = ticketCheckerModal.getTicketDetail("Plays");
        assertWithMessage("game is not successful")
                .that(numberOfPlays)
                .isEqualTo("2");

        List<String> firstBoardNumbersPlayedInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 0, 4);
        List<String> secondBoardNumbersPlayedInInTicketCheckerModal = getNumbersPlayedPerGameInTicketCheckerModal(webDriver, 5, 4);
        assertWithMessage("incorrect played numbers")
                .that(firstBoardNumbersPlayed)
                .isEqualTo(firstBoardNumbersPlayedInTicketCheckerModal);
        assertWithMessage("incorrect played numbers")
                .that(secondBoardNumbersPlayed)
                .isEqualTo(secondBoardNumbersPlayedInInTicketCheckerModal);

        String firstGameType = ticketCheckerModal.getFirstGameBetType();
        assertWithMessage("incorrect game type")
                .that(firstGameType)
                .isEqualTo("Play Type = \n" +
                        "Straight\n" +
                        "Standard");

        String secondGameType = ticketCheckerModal.getSecondGameBetType();
        assertWithMessage("incorrect game type")
                .that(secondGameType)
                .isEqualTo("Play Type = \n" +
                        "Box - 120 Ways\n" +
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
                .isEqualTo(totalBetCostString);

        ticketCheckerModal.clickCloseButton();
        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAU23-327 :: iLottery - Play DC-5 - Variations")
    public void QAAU23$327$iLotteryPlayDC5Variations(WebDriver webDriver) {
        List<String> straightBoxSelectionNumbers = Arrays.asList(new String[]{"2", "1", "3", "4", "5"});
        List<String> frontPairSelectionNumbers = Arrays.asList(new String[]{"1", "2", "-", "-", "-"});
        List<String> backThreeSelectionNumbers = Arrays.asList(new String[]{"-", "-", "3", "4", "5"});
        List<String> frontFourSelectionNumbers = Arrays.asList(new String[]{"1", "2", "3", "4", "-"});

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        //Bet selections (Play 1):
        //- Play type: Straight/Box
        //- Play amount: $1.00
        //- Numbers selections: 1, 2, 3, 4, 5
        dcGamesManualPlay.clickPlayStyleSelectBox()
                .selectPlayStyle("Straight/Box")
                .clickMultiplierSelectBox()
                .selectMultiplier("$1.00");

        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight/Box"));

        for (int i = 0; i < straightBoxSelectionNumbers.size(); i++) {
            dcGamesManualPlay.enterNumberToPlay(i, straightBoxSelectionNumbers.get(i));
        }

        BigDecimal straightBoxGameCost = new BigDecimal("1.00");
        dcGamesManualPlay.clickAddPlayButton();

        //Bet selections (Play 2):
        //- Play type: Front Pair
        //- Play amount: $1.00
        //- Numbers selections: 1, 2
        dcGamesManualPlay.clickPlayStyleSelectBox()
                .selectPlayStyle("Front Pair")
                .clickMultiplierSelectBox()
                .selectMultiplier("$1.00");

        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Front Pair"));
        dcGamesManualPlay.enterNumberToPlay(0, "1");
        dcGamesManualPlay.enterNumberToPlay(1, "2");

        BigDecimal frontPairGameCost = new BigDecimal("1.00");
        dcGamesManualPlay.clickAddPlayButton();

        //Bet selections (Play 3):
        //- Play type: Back Three
        //- Play amount: $0.50
        //- Numbers selections: 3, 4, 5
        dcGamesManualPlay.clickPlayStyleSelectBox()
                .selectPlayStyle("Back Three")
                .clickMultiplierSelectBox()
                .selectMultiplier("$0.50");

        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Back Three"));
        dcGamesManualPlay.enterNumberToPlay(2, "3");
        dcGamesManualPlay.enterNumberToPlay(3, "4");
        dcGamesManualPlay.enterNumberToPlay(4, "5");

        BigDecimal backThreeGameCost = new BigDecimal("0.50");
        dcGamesManualPlay.clickAddPlayButton();

        //Bet selections (Play 3):
        //- Play type: Front Four
        //- Play amount: $0.50
        //- Numbers selections: 1, 2, 3, 4,5
        dcGamesManualPlay.clickPlayStyleSelectBox()
                .selectPlayStyle("Front Four")
                .clickMultiplierSelectBox()
                .selectMultiplier("$0.50");

        assertWithMessage("incorrect game option")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Front Four"));
        dcGamesManualPlay.enterNumberToPlay(0, "1");
        dcGamesManualPlay.enterNumberToPlay(1, "2");
        dcGamesManualPlay.enterNumberToPlay(2, "3");
        dcGamesManualPlay.enterNumberToPlay(3, "4");

        BigDecimal frontFourGameCost = new BigDecimal("0.50");

        dcGamesManualPlay.selectAdvancePlay("Today")
                .deselectDrawTime("Day", Properties.getPropertyValue("dc5.game.id"))
                .setMultiDaysTo3()
                .clickPlayNowButton();

        BigDecimal totalGameCost = frontFourGameCost.add(backThreeGameCost).add(frontPairGameCost).add(straightBoxGameCost);
        BigDecimal totalBetCostAsBigDecimal = totalGameCost.multiply(new BigDecimal(3)); //multidays

        PlayGameConfirmationModal playGameConfirmationModal = new PlayGameConfirmationModal(webDriver);

        List<WebElement> getPlayedBoards = playGameConfirmationModal.getSelectedBoards();
        assertWithMessage("Played draws should have been 4 but found: " + getPlayedBoards.size())
                .that(getPlayedBoards.size())
                .isEqualTo(4);

        /** Board One Assertion **/
        WebElement straightBoxBetBoard = getPlayedBoards.get(0);
        List<String> straightBoxNumbersPlayedOnBoard = playGameConfirmationModal.getPlayedNumberAccordingToBoard(straightBoxBetBoard);
        assertWithMessage("Straight Box selection numbers are incorrect")
                .that(straightBoxNumbersPlayedOnBoard).containsAtLeastElementsIn(straightBoxSelectionNumbers);

        String getPlayedStyleOnBoard = playGameConfirmationModal.getPlayedStyleAccordingToBoard(straightBoxBetBoard);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(getPlayedStyleOnBoard)
                .isEqualTo("Straight/Box - 120 Ways");

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
        WebElement backThreeBetBoard = getPlayedBoards.get(2);
        List<String> backThreeNumbersPlayedOnBoard = playGameConfirmationModal.getPlayedNumberAccordingToBoard(backThreeBetBoard);
        assertWithMessage("Back Pair selection numbers are incorrect")
                .that(backThreeNumbersPlayedOnBoard).containsAtLeastElementsIn(backThreeSelectionNumbers);

        String backThreeGetPlayedStyleOnBoard = playGameConfirmationModal.getPlayedStyleAccordingToBoard(backThreeBetBoard);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(backThreeGetPlayedStyleOnBoard)
                .isEqualTo("Back Three");
        /** Board Three Assertion **/

        /** Board Four Assertion **/
        WebElement frontFourBetBoard = getPlayedBoards.get(3);
        List<String> frontFourNumbersPlayedOnBoard = playGameConfirmationModal.getPlayedNumberAccordingToBoard(frontFourBetBoard);
        assertWithMessage("Combo selection numbers are incorrect")
                .that(frontFourNumbersPlayedOnBoard).containsAtLeastElementsIn(frontFourSelectionNumbers);

        String frontFourGetPlayedStyleOnBoard = playGameConfirmationModal.getPlayedStyleAccordingToBoard(frontFourBetBoard);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(frontFourGetPlayedStyleOnBoard)
                .isEqualTo("Front Four");
        /** Board Four Assertion **/

        //- Multi drawings
        String multiDrawingsInConfirmationModal = playGameConfirmationModal.getMultiDraws();
        assertWithMessage("draws in confirmation window is incorrect")
                .that(multiDrawingsInConfirmationModal)
                .isEqualTo("3");

        playGameConfirmationModal.clickPurchaseButton();

        //thank you modal
        ThankYouForPlayingModal thankYouForPlayingModal = new ThankYouForPlayingModal(webDriver);
        String modalTitle1 = thankYouForPlayingModal.getModalTitle();
        assertWithMessage("bet is not placed")
                .that(modalTitle1.equalsIgnoreCase("Thank You"))
                .isTrue();

        List<WebElement> getPlayedBoardsThankYou = thankYouForPlayingModal.getSelectedBoards();

        /** Board One Assertion **/
        WebElement straightBoxBetBoardThankYou = getPlayedBoardsThankYou.get(0);
        List<String> straightBoxNumbersPlayedOnBoardThankYou = thankYouForPlayingModal.getPlayedNumberAccordingToBoard(straightBoxBetBoardThankYou);
        assertWithMessage("Straight Box selection numbers are incorrect")
                .that(straightBoxNumbersPlayedOnBoardThankYou).containsAtLeastElementsIn(straightBoxSelectionNumbers);

        String getPlayedStyleOnBoardThankYou = thankYouForPlayingModal.getPlayedStyleAccordingToBoard(straightBoxBetBoardThankYou);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(getPlayedStyleOnBoardThankYou)
                .isEqualTo("Straight/Box - 120 Ways");

        /** Board One Assertion **/

        /** Board Two Assertion **/
        WebElement frontPairBetBoardThankYou = getPlayedBoardsThankYou.get(1);
        List<String> frontPairNumbersPlayedOnBoardThankYou = thankYouForPlayingModal.getPlayedNumberAccordingToBoard(frontPairBetBoardThankYou);
        assertWithMessage("Straight selection numbers are incorrect")
                .that(frontPairNumbersPlayedOnBoardThankYou).containsAtLeastElementsIn(frontPairSelectionNumbers);

        Boolean frontPairIsLuckySumEnabledOnBoardThankYou = thankYouForPlayingModal.isLuckySumEnabledAccordingToBoard(frontPairBetBoardThankYou);
        assertWithMessage("Straight selection numbers are incorrect")
                .that(frontPairIsLuckySumEnabledOnBoardThankYou).isFalse();

        String frontPairGetPlayedStyleOnBoardThankYou = thankYouForPlayingModal.getPlayedStyleAccordingToBoard(frontPairBetBoardThankYou);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(frontPairGetPlayedStyleOnBoardThankYou)
                .isEqualTo("Front Pair");
        /** Board Two Assertion **/

        /** Board Three Assertion **/
        WebElement backThreeBetBoardThankYou = getPlayedBoardsThankYou.get(2);
        List<String> backThreeNumbersPlayedOnBoardThankYou = thankYouForPlayingModal.getPlayedNumberAccordingToBoard(backThreeBetBoardThankYou);
        assertWithMessage("Back Pair selection numbers are incorrect")
                .that(backThreeNumbersPlayedOnBoardThankYou).containsAtLeastElementsIn(backThreeSelectionNumbers);

        String backThreeGetPlayedStyleOnBoardThankYou = thankYouForPlayingModal.getPlayedStyleAccordingToBoard(backThreeBetBoardThankYou);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(backThreeGetPlayedStyleOnBoardThankYou)
                .isEqualTo("Back Three");
        /** Board Three Assertion **/

        /** Board Four Assertion **/
        WebElement frontFourBetBoardThankYou = getPlayedBoardsThankYou.get(3);
        List<String> frontFourNumbersPlayedOnBoardThankYou = thankYouForPlayingModal.getPlayedNumberAccordingToBoard(frontFourBetBoardThankYou);
        assertWithMessage("Combo selection numbers are incorrect")
                .that(frontFourNumbersPlayedOnBoardThankYou).containsAtLeastElementsIn(frontFourSelectionNumbers);

        String frontFourGetPlayedStyleOnBoardThankYou = thankYouForPlayingModal.getPlayedStyleAccordingToBoard(frontFourBetBoardThankYou);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(frontFourGetPlayedStyleOnBoardThankYou)
                .isEqualTo("Front Four");
        /** Board Four Assertion **/


        //*Ticket ID* has been created
        String ticketId = thankYouForPlayingModal.getTicketId();

        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(ticketId)
                .isNotEmpty();

        thankYouForPlayingModal.clickOkButton();
        homePage.clickTicketCheckerInTopMenu();

        TicketCheckerPage ticketCheckerPage = new TicketCheckerPage(webDriver);

        ticketCheckerPage.enterTicketId(ticketId);
        ticketCheckerPage.clickCheckMyNumbersButton();

        TicketCheckerModal ticketCheckerModal = new TicketCheckerModal(webDriver);

        String numberOfPlays = ticketCheckerModal.getTicketDetail("Plays");
        assertWithMessage("game is not successful")
                .that(numberOfPlays)
                .isEqualTo("4");

        List<WebElement> getPlayedBoardsTicketChecker = ticketCheckerModal.getSelectedBoards();

        /** Board One Assertion **/
        WebElement straightBoxBetBoardTickerChecker = getPlayedBoardsTicketChecker.get(0);
        List<String> straightBoxNumbersPlayedOnBoardTickerChecker = ticketCheckerModal.getPlayedNumberAccordingToBoard(straightBoxBetBoardTickerChecker);
        assertWithMessage("Straight Box selection numbers are incorrect")
                .that(straightBoxNumbersPlayedOnBoardTickerChecker).containsAtLeastElementsIn(straightBoxSelectionNumbers);

        String getPlayedStyleOnBoardTickerChecker = ticketCheckerModal.getPlayedStyleAccordingToBoard(straightBoxBetBoardTickerChecker);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(getPlayedStyleOnBoardTickerChecker)
                .isEqualTo("Straight/Box - 120 Ways");

        /** Board One Assertion **/

        /** Board Two Assertion **/
        WebElement frontPairBetBoardTickerChecker = getPlayedBoardsTicketChecker.get(1);
        List<String> frontPairNumbersPlayedOnBoardTickerChecker = ticketCheckerModal.getPlayedNumberAccordingToBoard(frontPairBetBoardTickerChecker);
        assertWithMessage("Straight selection numbers are incorrect")
                .that(frontPairNumbersPlayedOnBoardTickerChecker).containsAtLeastElementsIn(frontPairSelectionNumbers);

        Boolean frontPairIsLuckySumEnabledOnBoardTickerChecker = ticketCheckerModal.isLuckySumEnabledAccordingToBoard(frontPairBetBoardTickerChecker);
        assertWithMessage("Straight selection numbers are incorrect")
                .that(frontPairIsLuckySumEnabledOnBoardTickerChecker).isFalse();

        String frontPairGetPlayedStyleOnBoardTickerChecker = ticketCheckerModal.getPlayedStyleAccordingToBoard(frontPairBetBoardTickerChecker);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(frontPairGetPlayedStyleOnBoardTickerChecker)
                .isEqualTo("Front Pair");
        /** Board Two Assertion **/

        /** Board Three Assertion **/
        WebElement backThreeBetBoardTickerChecker = getPlayedBoardsTicketChecker.get(2);
        List<String> backThreeNumbersPlayedOnBoardTickerChecker = ticketCheckerModal.getPlayedNumberAccordingToBoard(backThreeBetBoardTickerChecker);
        assertWithMessage("Back Pair selection numbers are incorrect")
                .that(backThreeNumbersPlayedOnBoardTickerChecker).containsAtLeastElementsIn(backThreeSelectionNumbers);

        String backThreeGetPlayedStyleOnBoardTickerChecker = ticketCheckerModal.getPlayedStyleAccordingToBoard(backThreeBetBoardTickerChecker);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(backThreeGetPlayedStyleOnBoardTickerChecker)
                .isEqualTo("Back Three");
        /** Board Three Assertion **/

        /** Board Four Assertion **/
        WebElement frontFourBetBoardTickerChecker = getPlayedBoardsTicketChecker.get(3);
        List<String> frontFourNumbersPlayedOnBoardTickerChecker = ticketCheckerModal.getPlayedNumberAccordingToBoard(frontFourBetBoardTickerChecker);
        assertWithMessage("Combo selection numbers are incorrect")
                .that(frontFourNumbersPlayedOnBoardTickerChecker).containsAtLeastElementsIn(frontFourSelectionNumbers);

        String frontFourGetPlayedStyleOnBoardTickerChecker = ticketCheckerModal.getPlayedStyleAccordingToBoard(frontFourBetBoardTickerChecker);
        assertWithMessage("Play Style in confirmation window is incorrect")
                .that(frontFourGetPlayedStyleOnBoardTickerChecker)
                .isEqualTo("Front Four");
        /** Board Four Assertion **/

        //- Cost: $9.00
        String costInTicketCheckerModal = ticketCheckerModal.getGameCost();
        assertWithMessage("bet cost in confirmation window is incorrect")
                .that(costInTicketCheckerModal)
                .isEqualTo("9.00");

        ticketCheckerModal.clickCloseButton();
        homePage.clickUserIcon();
    }

}