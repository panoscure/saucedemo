package tests.shoppingcart;

import base.test.BaseTest;
import canvas.page.objects.CartDrawer;
import canvas.page.objects.HomePage;
import canvas.page.objects.games.Race2RichesGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
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
public class ShoppingCartContinuePlaying extends BaseTest {
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
    @DisplayName("QAAUT-1460 :: Shopping Cart | Continue Playing button functionality")
    public void QAAUT$1460$ShoppingCartContinuePlayingButtonFunctionality(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("RACE2RICHES");

        assertWithMessage("incorrect page")
                .that(webDriver.getCurrentUrl().equalsIgnoreCase("http://canvasweb.ilottery.dev.azure.l10.intralot.com/lh/lottery-game/race2riches"))
                .isTrue();
        Race2RichesGamesManualPlay race2RichesGamesManualPlay = new Race2RichesGamesManualPlay(webDriver);
        race2RichesGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionExactABox();
        String multiplierValue = race2RichesGamesManualPlay.getSelectedOptionMultiplierValue();

        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();

        race2RichesGamesManualPlay.clickNumberToSelectHorse(0, "12")
                .clickNumberToSelectHorse(0, "11")
                .clickNumberToSelectHorse(0, "1");


        BigDecimal race2RichesTotalCost = new BigDecimal("6.00");
        //assert total price is correctly calculated
        assertWithMessage("bet is not successful")
                .that(race2RichesGamesManualPlay.getYourPlaysList().size())
                .isEqualTo(1);

        BigDecimal gameCostPerDay = new BigDecimal("6.00");

        assertWithMessage("total bet cost is incorrect")
                .that(race2RichesGamesManualPlay.getBetTotalPrice().equalsIgnoreCase(race2RichesTotalCost.toString()))
                .isTrue();
        assertWithMessage("bet is not successful")
                .that(new BigDecimal(race2RichesGamesManualPlay.getGamesPrice("Race2Riches")))
                .isEqualTo(gameCostPerDay);

        race2RichesGamesManualPlay.clickAddToCartButton();

        assertWithMessage("incorrect number of games to cart")
                .that(homePage.numberOfGameInCart())
                .isEqualTo("1");
        homePage.clickCartButton();

        //cart assertions
        CartDrawer cartDrawer = new CartDrawer(webDriver);

        assertWithMessage("cart did not open")
                .that(cartDrawer.isGameCorrect("race2riches", 0))
                .isTrue();

        assertWithMessage("incorrect number of games in cart")
                .that(cartDrawer.getNumberOfPlaysInCart())
                .isEqualTo("1");
        assertWithMessage("incorrect type of game in cart")
                .that(cartDrawer.getPlayType(0).equalsIgnoreCase("Standard"))
                .isTrue();

        assertWithMessage("incorrect cost")
                .that(cartDrawer.getPlayPrice())
                .isEqualTo("$" + gameCostPerDay.toString());
        assertWithMessage("incorrect total")
                .that(cartDrawer.getPlayTotal())
                .isEqualTo("$" + race2RichesTotalCost.toString());

        cartDrawer.clickExpandTicketDetailsButton(0);

        String firstGameInCart = cartDrawer.getGameInCart(1);

        assertWithMessage("incorrect game")
                .that(firstGameInCart.contains("1\n" + "11\n" + "12\n" +
                        "Play Type = \n" +
                        "EXACTA BOX"))
                .isTrue();

        //CONTINUE PLAYING
        cartDrawer.clickCartButton("CONTINUE PLAYING");

        assertWithMessage("incorrect page")
                .that(webDriver.getCurrentUrl().equalsIgnoreCase("http://canvasweb.ilottery.dev.azure.l10.intralot.com/lh/lottery-lobby"))
                        .isTrue();

        homePage.clickUserIcon();
    }



}
