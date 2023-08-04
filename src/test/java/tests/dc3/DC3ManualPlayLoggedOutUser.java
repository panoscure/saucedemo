package tests.dc3;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
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
import java.util.List;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class DC3ManualPlayLoggedOutUser extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");




    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }


    @Test
    @DisplayName("QAAUT-1699 :: DC3 Game Client | Play DC3 game | Back Pair-Both-Multiplier $1.00 | Quick Pick | Multi Days 7 - Logged out User")
    public void QAAUT$1699$DC3GameClientPlayDC3GameBackPairBothMultiplier1QuickPickMultiDays7LoggedOutUser(WebDriver webDriver) {

        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);

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
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc3.game.id"))
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

        logIn(webDriver, username, password);
        BigDecimal accountBalance = homePage.getBalance();

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

        for (String game: typeOfGamesInTicket) {
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


}
