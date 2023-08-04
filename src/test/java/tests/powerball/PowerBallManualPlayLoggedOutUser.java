package tests.powerball;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.games.PowerBallGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.PlayGameConfirmationModal;
import canvas.page.objects.modals.ThankYouForPlayingModal;
import common.utils.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class PowerBallManualPlayLoggedOutUser extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }


    @Test
    @DisplayName("QAAUT-1704 :: PowerBall Game Manual Play + Power Play - Logged out User")
    public void QAAUT$1704$PowerBallGameManualPlayPowerPlayLoggedOutUser(WebDriver webDriver) {

        webDriver.get(canvasUrl);
        HomePage homePage = new HomePage(webDriver);

        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("POWERBALL");

        select5NumbersToPlay(webDriver, 11);
        PowerBallGamesManualPlay powerBallGamesManualPlay = new PowerBallGamesManualPlay(webDriver);
        powerBallGamesManualPlay.selectOneNumberToPlay("17");

        BigDecimal betAmount = new BigDecimal("2.00");

        //assert quick pick PowerBall page elements
        int numberOfGames = powerBallGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        powerBallGamesManualPlay.clickPowerPlayToggle();

        betAmount = betAmount.add(new BigDecimal("1.00"));

        //assert quick pick PowerBall page elements
        numberOfGames = powerBallGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        BigDecimal totalBetCost = betAmount;
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(powerBallGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        powerBallGamesManualPlay.clickPlayNowButton();

        logIn(webDriver, username, password);
        BigDecimal accountBalance = homePage.getBalance();

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

}
