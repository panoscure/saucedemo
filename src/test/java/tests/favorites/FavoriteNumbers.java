package tests.favorites;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavoriteNumbers;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavorites;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.ConfirmationModal;
import canvas.page.objects.modals.FavoriteNumbersModal;
import canvas.page.objects.modals.MaxFavoriteNumbersErrorModal;
import common.utils.Properties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;

@ExtendWith(WebDriverInit.class)
public class FavoriteNumbers extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.usr111.username");
    public String password = Properties.getPropertyValue("canvas.usr111.password");

    @BeforeEach
    public void setUp(WebDriver webDriver) {
        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Disabled
    @DisplayName("QAAUT-1707 :: My Favorites - Favorite Numbers - Add Favorite Number")
    public void QAAUT$1707$MyFavoritesFavoriteNumbersAddFavoriteNumber(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);

        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickNextArrowButtonToFindPreferencesTab()
                .clickTabInAccountDrawer("PREFERENCES");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("MY FAVORITES");

        MyFavorites myFavorites = new MyFavorites(webDriver);
        myFavorites.clickMyFavoriteNumbersButton();

        MyFavoriteNumbers myFavoriteNumbers = new MyFavoriteNumbers(webDriver);

        //add 31 favorite numbers
        addFavoriteNumbers(webDriver, 30 );

        MaxFavoriteNumbersErrorModal maxFavoriteNumbersErrorModal = new MaxFavoriteNumbersErrorModal(webDriver);
        assertWithMessage("error modal is not displayed")
                .that(maxFavoriteNumbersErrorModal.getModalHeader().equalsIgnoreCase("Error"))
                .isTrue();

        assertWithMessage("error message is not displayed")
                .that(maxFavoriteNumbersErrorModal.getModalMessage().contains("Max favorite numbers limit 30 exceeded"))
                .isTrue();

        assertWithMessage("incorrect count of favorite numbers")
                .that(myFavoriteNumbers.getFavoriteNumbersCount())
                .isEqualTo(30);

        System.out.println(myFavoriteNumbers.getAllFavoriteNumbers());


        deleteFavoriteNumbers(webDriver, 31);

    }

    @Disabled
    @DisplayName("QAAUT-1708 :: My Favorites - Favorite Numbers - Play Favorite Numbers")
    public void QAAUT$1708$MyFavoritesFavoriteNumbersPlayFavoriteNumbers(WebDriver webDriver) {
        HomePage homePage = new HomePage(webDriver);

        homePage.clickUserIcon();

        AccountDrawer accountDrawer = new AccountDrawer(webDriver);
        accountDrawer.clickNextArrowButtonToFindPreferencesTab()
                .clickTabInAccountDrawer("PREFERENCES");

        Tabs tabs = new Tabs(webDriver);
        tabs.clickSectionInTab("MY FAVORITES");

        MyFavorites myFavorites = new MyFavorites(webDriver);
        myFavorites.clickMyFavoriteNumbersButton();

        MyFavoriteNumbers myFavoriteNumbers = new MyFavoriteNumbers(webDriver);

        addFavoriteNumbers(webDriver, 15);
        System.out.println(myFavoriteNumbers.getAllFavoriteNumbers());

        homePage.clickUserIcon()
                .clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        dcGamesManualPlay.clickFavoriteNumbersButton();

        FavoriteNumbersModal favoriteNumbersModal = new FavoriteNumbersModal(webDriver);


        myFavoriteNumbers.clickCloseModalButton();

        homePage.clickUserIcon();

        accountDrawer.clickBackToMyAccountButton()
                .clickTabInAccountDrawer("PREFERENCES");

        tabs.clickSectionInTab("MY FAVORITES");

        myFavorites.clickMyFavoriteNumbersButton();

        myFavoriteNumbers.deleteFavoriteNumber();

        ConfirmationModal confirmationModal = new ConfirmationModal(webDriver);
        confirmationModal.clickButtonInModal("Yes");

    }


}
