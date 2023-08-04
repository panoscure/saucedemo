package tests.dc5;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.games.dc.DCGamesManualPlay;
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
public class DC5ManualPlayLoggedOutUser extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");


    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }


    @Test
    @DisplayName("QAAUT-1701 :: DC5 Game Client | Play DC5 game | Front Pair-Night-Multiplier $1.00-Logged out User")
    public void QAAUT$1701$DC5GameClientPlayDC5GameFrontPairNightMultiplier$1LoggedOutUser(WebDriver webDriver) {

        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);

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
                .deselectDrawTime("Day", Properties.getPropertyValue("dc5.game.id"));

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
        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        assertWithMessage("bet is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(frontPairTotalCost));

        homePage.clickUserIcon();
    }

}
