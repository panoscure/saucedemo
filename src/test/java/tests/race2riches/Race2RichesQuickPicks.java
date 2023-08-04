package tests.race2riches;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.GamePageCommonElements;
import canvas.page.objects.games.QuickPicksTab;
import canvas.page.objects.games.Race2RichesGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
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
public class Race2RichesQuickPicks extends BaseTest {

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
    @DisplayName("QAAUT-1529 :: RACE2RICHES Game Client | Quick Pick tab | Win 1 race $1.00")
    public void QAAUT$1529$Race2RichesClientQuickPickTabWin1RaceMult10$100(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$1.00").get(0).click();

        //assert quick pick RACE2RICHES page elements
        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);

        int numberOfGames = race2RichesGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = race2RichesGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("RACE2RICHES game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Win"))
                .isTrue();

        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));
        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1530 :: RACE2RICHES Game Client | Quick Pick tab | Show 1 race $1.00")
    public void QAAUT$1530$Race2RichesClientQuickPickTabShow1RaceMult10$100(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$1.00").get(1).click();

        //assert quick pick RACE2RICHES page elements
        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);

        int numberOfGames = race2RichesGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = race2RichesGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("RACE2RICHES game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Show"))
                .isTrue();

        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1531 :: RACE2RICHES Game Client | Quick Pick tab | Win 1 race $2.00")
    public void QAAUT$1531$Race2RichesClientQuickPickTabWin1RaceMult20$200(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$2.00").get(0).click();

        //assert quick pick RACE2RICHES page elements
        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);

        int numberOfGames = race2RichesGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = race2RichesGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("RACE2RICHES game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Win"))
                .isTrue();

        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("2.00"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
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
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOfRaceToRichesGamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {
            assertWithMessage("incorrect game")
                    .that(game.contains("Play Type = \n" +
                            "WIN\n" +
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
    @DisplayName("QAAUT-1532 :: RACE2RICHES Game Client | Quick Pick tab | Win 1 race $5.00")
    public void QAAUT$1532$Race2RichesClientQuickPickTabWin1RaceMult50$500(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$5.00").get(0).click();

        //assert quick pick RACE2RICHES page elements
        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);

        int numberOfGames = race2RichesGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = race2RichesGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("RACE2RICHES game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Win"))
                .isTrue();

        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("5.00"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();

    }

    @Test
    @DisplayName("QAAUT-1533 :: RACE2RICHES Game Client | Quick Pick tab | Show 1 race $2.00")
    public void QAAUT$1533$Race2RichesClientQuickPickTabShow1RaceMult20$200(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$2.00").get(1).click();

        //assert quick pick RACE2RICHES page elements
        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);

        int numberOfGames = race2RichesGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = race2RichesGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("RACE2RICHES game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Show"))
                .isTrue();

        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("2.00"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
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
        assertWithMessage("game is not successful")
                .that(accountBalance)
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        homePage.clickUserIcon();
    }

    @Test
    @DisplayName("QAAUT-1534 :: RACE2RICHES Game Client | Quick Pick tab | Show 1 race $5.00")
    public void QAAUT$1534$Race2RichesClientQuickPickTabShow1RaceMult50$500(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        GamePageCommonElements gamePageCommonElements = new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("Quick Picks");

        QuickPicksTab quickPicksTab = new QuickPicksTab(webDriver);
        quickPicksTab.quickPicksButton("$5.00").get(1).click();

        //assert quick pick RACE2RICHES page elements
        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);

        int numberOfGames = race2RichesGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        String dc3GameOption = race2RichesGamesManualPlay.getSelectedOptionValue();
        assertWithMessage("RACE2RICHES game option is incorrect")
                .that(dc3GameOption.equalsIgnoreCase("Show"))
                .isTrue();

        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("5.00"))
                .isTrue();

        BigDecimal multiplier = new BigDecimal(race2RichesGamesManualPlay.getSelectedOptionMultiplierValue());
        BigDecimal totalBetCost = multiplier.multiply(new BigDecimal(numberOfGames));
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
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
        gamingHistory.isGameCorrect("race2riches");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        List<String> typeOfGamesInTicket = ticket.getListOfRaceToRichesGamesInTheTicket();
        assertWithMessage("incorrect count of bets in ticket")
                .that(ticket.getListOfRaceToRichesGamesInTheTicket().size())
                .isEqualTo(1);

        for (String game: typeOfGamesInTicket) {

            assertWithMessage("incorrect game")
                    .that(game.contains("Play Type = \n" +
                                    "SHOW\n" +
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
