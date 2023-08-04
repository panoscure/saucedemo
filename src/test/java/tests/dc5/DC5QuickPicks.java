package tests.dc5;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.GamePageCommonElements;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.games.QuickPicksTab;
import canvas.page.objects.modals.PlayGameConfirmationModal;
import canvas.page.objects.modals.ThankYouForPlayingModal;
import common.utils.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;
import java.util.List;

import static canvas.helpers.HelperMethods.logInCanvas;
import static canvas.helpers.HelperMethods.logOutCanvas;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class DC5QuickPicks extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");


    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1524 :: DC5 Game Client | Quick Pick tab | 1 Play Straight $0.50")
    public void QAAUT$1524$DC5GameClientQuickPickTab1PlayStraight$050(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$0.50").get(0).click();

        //assert quick pick DC5 page elements
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = dcGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("DC5 game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Straight"))
                .isTrue();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1525 :: DC5 Game Client | Quick Pick tab | 2 Play Straight $2.00")
    public void QAAUT$1525$DC5GameClientQuickPickTab2PlayStraight$200(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$2.00").get(0).click();

        //assert quick pick DC5 page elements
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(2);

        String dc3GameOption = dcGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("DC5 game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Straight"))
                .isTrue();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();


        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1526 :: DC5 Game Client | Quick Pick tab | 5 Play Straight $2.50")
    public void QAAUT$1526$DC5GameClientQuickPickTab5PlayStraight$250(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$2.50").get(0).click();

        //assert quick pick DC5 page elements
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();

        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(5);

        String dc3GameOption = dcGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("DC5 game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Straight"))
                .isTrue();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();


        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1521 :: DC5 Game Client | Quick Pick tab | 2 Play Straight $1.00")
    public void QAAUT$1521$DC5GameClientQuickPickTab2PlayStraight$100(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$1.00").get(1).click();

        //assert quick pick DC5 page elements
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(2);

        String dc3GameOption = dcGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("DC5 game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Straight"))
                .isTrue();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("0.50"))
                .isTrue();


        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1527 :: DC5 Game Client | Quick Pick tab | 1 Play Straight $1.00")
    public void QAAUT$1527$DC5GameClientQuickPickTab1PlayStraight$100(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$1.00").get(0).click();

        //assert quick pick DC5 page elements
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);

        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = dcGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("DC5 game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Straight"))
                .isTrue();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1528 :: DC5 Game Client | Quick Pick tab | 5 Play Straight $5.00")
    public void QAAUT$1528$DC5GameClientQuickPickTab5PlayStraight$500(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-5");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$5.00").get(0).click();

        //assert quick pick DC5 page elements
        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        int numberOfGames = dcGamesManualPlay.getYourPlaysList().size();

        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(5);

        String dc3GameOption = dcGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("DC5 game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Straight"))
                .isTrue();

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();


        BigDecimal multiplier = new BigDecimal(dcGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
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
        gamingHistory.isGameCorrect("dc5");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfDCGamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOfDCGamesInTheTicket().size())
                .isEqualTo(5);

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains("Play Type = \n" + "Straight\n" +
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
}
