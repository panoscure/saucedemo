package tests.megamillions;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.games.MegaMillionsGamesManualPlay;
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
public class MegamillionsManualPlayLoggedOutUser extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1703 :: MegaMillions Game Manual Play - Logged Out User")
    public void QAAUT$1703$MegaMillionsGameManualPlayLoggedOutUser(WebDriver webDriver) {

        webDriver.get(canvasUrl);
        HomePage homePage = new HomePage(webDriver);

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