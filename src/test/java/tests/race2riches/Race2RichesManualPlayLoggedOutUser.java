package tests.race2riches;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.Race2RichesGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.PlayGameConfirmationModal;
import canvas.page.objects.modals.ThankYouForPlayingModal;
import common.utils.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
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
public class Race2RichesManualPlayLoggedOutUser extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }


    @Test
    @DisplayName("QAAUT-1705 :: Race2Riches Game Client | Play Race2Riches game | Superfecta Wheel 5 Horses + Bonus - Logged Out User")
    public void QAAUT$1705$Race2RichesGameClientPlayRace2RichesGameSuperfectaWheel5HorsesPlusBonusLoggedOutUser(WebDriver webDriver) {

        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);

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

        logIn(webDriver, username, password);
        BigDecimal accountBalance = homePage.getBalance();

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

    @Disabled
    @DisplayName("QAAUT-1709 :: Race2Riches Game Client | Play Trifecta Race2Riches game | Verify you can change your manual selection of horses by using Quick Pick button")
    public void QAAUT$1709$Race2RichesGameClientPlayTrifectaRace2RichesGameVerifyYouCanChangeYourManualSelectionOfHorsesByUsingQuickPickButton(WebDriver webDriver) {

        webDriver.get(canvasUrl);

        HomePage homePage = new HomePage(webDriver);
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

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "12")
                .clickNumberToSelectHorse(1, "12")
                .clickQuickPicksButtonUnderNumbersSection();

        assertWithMessage("quick pick button doesn't work")
                .that(race2RichesGamesManualPlay.getSelectedHorsesCount())
                .isEqualTo(3);

        assertWithMessage("quick pick button doesn't work")
                .that(race2RichesGamesManualPlay.getSelectedHorsesNumbers(0)!=race2RichesGamesManualPlay.getSelectedHorsesNumbers(1) &&
                        race2RichesGamesManualPlay.getSelectedHorsesNumbers(0)!=race2RichesGamesManualPlay.getSelectedHorsesNumbers(2) &&
                        race2RichesGamesManualPlay.getSelectedHorsesNumbers(1)!=race2RichesGamesManualPlay.getSelectedHorsesNumbers(2))
                .isTrue();

    }
}
