package tests.favorites;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavoriteCoupons;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavorites;
import canvas.page.objects.account.drawer.wallet.gaminghistory.GamingHistory;
import canvas.page.objects.account.drawer.wallet.gaminghistory.Ticket;
import canvas.page.objects.games.MegaMillionsGamesManualPlay;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.*;
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
import java.util.Random;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class FavoriteTickets extends BaseTest {

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
    @DisplayName("QAAUT-1566 :: My Favorites | Favorite Playslips | Play a Favorite DC3-Straight/Box+Back Pair-number of days 2")
    public void QAAUT$1566$MyFavoritesFavoritePlayslipsPlayFavoriteDC3StraightBoxBackPairNumberOfDays2(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        BigDecimal accountBalance = homePage.getBalance();
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox()
                .clickQuickPicksButtonUnderNumbersSection()
                .clickToExpandGameOptionDropdownList()
                .clickAddPlayButton()
                .selectOptionBackPairDC3DC5BackThreeDC4()
                .clickQuickPicksButtonUnderNumbersSection()
//                .selectDrawTime("All")
                .setMultiDaysTo2();

        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("4"))
                .isTrue();

        BigDecimal numOfDaysToBetAsBigDecimal = new BigDecimal(numberOfDaysToBet);
        BigDecimal dc3GamesCostPerDay = new BigDecimal(dcGamesManualPlay.getDCGamesPrice("DC-3"));
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(dcGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(2);

        BigDecimal totalBetCost = dc3GamesCostPerDay.multiply(numOfDaysToBetAsBigDecimal);

        assertWithMessage("incorrect cost")
                .that(dcGamesManualPlay.getBetTotalPrice())
                .isEqualTo(totalBetCost.toString());


        dcGamesManualPlay.clickAddToFavoritesButton();

        SaveFavoritesModal saveFavoritesModal = new SaveFavoritesModal(webDriver);
        assertWithMessage("incorrect modal")
                .that(saveFavoritesModal.getModalText().equalsIgnoreCase("Enter favorite name"))
                .isTrue();
        Random random = new Random();
        String name = "Favorite" + random.nextInt(100);
        saveFavoritesModal.enterNameForFavoritePlaySlip(name)
                .clickButtonInModal("Save");

        SuccessAddToFavoritesModal successAddToFavoritesModal = new SuccessAddToFavoritesModal(webDriver);
        assertWithMessage("add to favorite is not successful")
                .that(successAddToFavoritesModal.getSuccessTitle().equalsIgnoreCase("Success"))
                .isTrue();
        successAddToFavoritesModal.clickCloseButton();

        //my Favorites
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickNextArrowButtonToFindPreferencesTab()
                .clickTabInAccountDrawer("PREFERENCES");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("MY FAVORITES");

        MyFavorites myFavorites = new MyFavorites(webDriver);
        myFavorites.clickMyFavoriteCouponsButton();

        assertWithMessage("not sorted by descending date")
                .that(myFavorites.areGamesSortedByDescendingDate())
                .isTrue();
        myFavorites.clickTheLatestFavorite();

        MyFavoriteCoupons myFavoriteCoupons = new MyFavoriteCoupons(webDriver);
        List<String> gamesInCoupon = myFavoriteCoupons.getListOfGamesInTheFavoriteCoupon();

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        assertWithMessage("favorite is incorrect")
                .that(gamesInCoupon.size())
                .isEqualTo(2);

        assertWithMessage("incorrect game details")
                .that(gamesInCoupon.get(0).contains(
                        "Play Type = \n" +
                                "Straight/Box"))
                .isTrue();

        assertWithMessage("incorrect game details")
                .that(gamesInCoupon.get(1).contains(
                        "Play Type = \n" +
                                "Back Pair"))
                .isTrue();


        myFavoriteCoupons.clickBuyNowButton();

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
                .isEqualTo(accountBalanceAfterBet.add(totalBetCost));

        //gaming history
        accountDrawer.clickBackToMyAccountButton()
                .clickPreviousArrowButtonToWalletTab()
                .clickTabInAccountDrawer("WALLET");

        tabs.clickSectionInTab("GAMING HISTORY");

        GamingHistory gamingHistory = new GamingHistory(webDriver);
        gamingHistory.isGameCorrect("dc3");
        gamingHistory.clickTheLatestFinancialTransaction();

        Ticket ticket = new Ticket(webDriver);
        assertWithMessage("incorrect gameId")
                .that(gameId)
                .isEqualTo(ticket.getTicketId());

        assertWithMessage("incorrect channel")
                .that(ticket.getTicketChannel().equalsIgnoreCase("Web"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(ticket.getTicketCost().equalsIgnoreCase("$" + totalBetCost.toString()))
                .isTrue();

        //delete favorite coupon
        accountDrawer.clickBackToMyAccountButton();
        accountDrawer.clickNextArrowButtonToFindPreferencesTab()
                .clickTabInAccountDrawer("PREFERENCES");

        tabs.clickSectionInTab("MY FAVORITES");

        myFavorites.clickTheLatestFavorite();

        deleteFavoriteCoupon(webDriver);

    }


    @Test
    @DisplayName("QAAUT-1706 :: My Favorites - Add to Cart Favorite Megamillions Manual Play+ Megaplier Game")
    public void QAAUT$1706$MyFavoritesAddToCartFavoriteMegamillionsManualPlayAndMegaplierGame(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);

        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        select5NumbersToPlay(webDriver, 11);
        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);
        megaMillionsGamesManualPlay.selectOneNumberToPlay("17");

        BigDecimal betAmount = new BigDecimal("2.00");

        //assert quick pick MegaMillions page elements
        int numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);

        megaMillionsGamesManualPlay.clickMegaplierToggle();

        betAmount = betAmount.add(new BigDecimal("1.00"));

        //assert quick pick MegaMillions page elements
        numberOfGames = megaMillionsGamesManualPlay.getYourPlaysList().size();
        assertWithMessage("games count is incorrect")
                .that(numberOfGames)
                .isEqualTo(1);
        BigDecimal totalBetCost = betAmount;
        //assert total price is correctly calculated
        assertWithMessage("total bet cost is incorrect")
                .that(megaMillionsGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(totalBetCost.toString()))
                .isTrue();
        megaMillionsGamesManualPlay.clickAddToFavoritesButton();

        SaveFavoritesModal saveFavoritesModal = new SaveFavoritesModal(webDriver);
        assertWithMessage("incorrect modal")
                .that(saveFavoritesModal.getModalText().equalsIgnoreCase("Enter favorite name"))
                .isTrue();
        Random random = new Random();
        String name = "Favorite" + random.nextInt(100);
        saveFavoritesModal.enterNameForFavoritePlaySlip(name)
                .clickButtonInModal("Save");

        SuccessAddToFavoritesModal successAddToFavoritesModal = new SuccessAddToFavoritesModal(webDriver);
        assertWithMessage("add to favorite is not successful")
                .that(successAddToFavoritesModal.getSuccessTitle().equalsIgnoreCase("Success"))
                .isTrue();
        successAddToFavoritesModal.clickCloseButton();

        //my Favorites
        homePage.clickUserIcon();
        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickNextArrowButtonToFindPreferencesTab()
                .clickTabInAccountDrawer("PREFERENCES");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("MY FAVORITES");

        MyFavorites myFavorites = new MyFavorites(webDriver);
        myFavorites.clickMyFavoriteCouponsButton();

        assertWithMessage("not sorted by descending date")
                .that(myFavorites.areGamesSortedByDescendingDate())
                .isTrue();
        myFavorites.clickTheLatestFavorite();

        MyFavoriteCoupons myFavoriteCoupons = new MyFavoriteCoupons(webDriver);
        List<String> gamesInCoupon = myFavoriteCoupons.getListOfGamesInTheFavoriteCoupon();

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        assertWithMessage("favorite is incorrect")
                .that(gamesInCoupon.size())
                .isEqualTo(1);

        assertWithMessage("incorrect game details")
                .that(gamesInCoupon.get(0).contains("11\n" +
                        "13\n" +
                        "15\n" +
                        "17\n" +
                        "19\n" +
                        "17"))
                .isTrue();

        myFavoriteCoupons.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");

        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("megamillions.id", 0))
                .isTrue();

        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Favorite"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + totalBetCost.toString());
        assertWithMessage("incorrect total")
                .that(cartDrawer.getPlayTotal())
                .isEqualTo("$" + totalBetCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains("11\n" +
                        "13\n" +
                        "15\n" +
                        "17\n" +
                        "19\n" +
                        "17"))
                .isTrue();

        //delete favorite game
        homePage.clickUserIcon();
        accountDrawer.clickNextArrowButtonToFindPreferencesTab()
                .clickTabInAccountDrawer("PREFERENCES");

        tabs.clickSectionInTab("MY FAVORITES");

        myFavorites.clickMyFavoriteCouponsButton();

        assertWithMessage("not sorted by descending date")
                .that(myFavorites.areGamesSortedByDescendingDate())
                .isTrue();
        myFavorites.clickTheLatestFavorite();

        deleteFavoriteCoupon(webDriver);

    }

}
