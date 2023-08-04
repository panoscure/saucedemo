package tests.shoppingcart;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavorites;
import canvas.page.objects.games.MegaMillionsGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.SaveFavoritesModal;
import common.utils.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.math.BigDecimal;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class ShoppingCartAddToFavorites extends BaseTest {
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
    @DisplayName("QAAUT-1459 :: Shopping Cart | Add game to Favorites")
    public void QAAUT$1459$ShoppingCartAddGameToFavorites(WebDriver webDriver) {

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
        megaMillionsGamesManualPlay.clickAddToCartButton();

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
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
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

        //add to favorites
        cartDrawer.clickCartTicketButton("Add to Favorites");

        SaveFavoritesModal saveFavoritesModal = new SaveFavoritesModal(webDriver);

        assertWithMessage("incorrect modal")
                .that(saveFavoritesModal.getModalText().equalsIgnoreCase("Enter favorite name"))
                .isTrue();
        saveFavoritesModal.enterNameForFavoritePlaySlip("From Cart")
                .clickButtonInModal("Save");

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
        deleteFavoriteCoupon(webDriver);

        assertWithMessage("message are not deleted")
                .that(myFavorites.getNoFavoritesMessage().contains("No favorite games found"));
    }



}
