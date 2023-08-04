package tests.dc3;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavoriteCoupons;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavorites;
import canvas.page.objects.games.FavoritePlaysTab;
import canvas.page.objects.games.GamePageCommonElements;
import canvas.page.objects.games.dc.DCGamesFavorites;
import canvas.page.objects.games.dc.DCGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.*;
import common.utils.Properties;
import io.restassured.response.Response;
import lottery.apigatewayj.models.pam.players.PlayerSignOn;
import models.apigatewayj.myprofileservice.getmyfavoriteplayslips.Content;
import models.apigatewayj.myprofileservice.getmyfavoriteplayslips.GetMyFavoritePlayslips;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static apigatewayj.Headers.getCommonHeaders;
import static apigatewayj.Headers.getHeadersWithSessionToken;
import static apigatewayj.MyProfileService.deleteMyFavoritePlaySlipById;
import static apigatewayj.MyProfileService.getMyFavoritePlaySlips;
import static canvas.helpers.HelperMethods.*;
import static com.google.common.truth.Truth.assertWithMessage;
import static io.restassured.http.ContentType.JSON;
import static lottery.apigatewayj.Authentication.grantAuthorizationTokenExtractToken;
import static lottery.apigatewayj.AuthenticationService.playerSignOn;

@ExtendWith(WebDriverInit.class)
public class DC3MyFavorites extends BaseTest {

    public String username = Properties.getPropertyValue("canvas.user");
    public String password = Properties.getPropertyValue("canvas.password");

    @BeforeEach
    public void deleteExistingFavoritesAndLogin(WebDriver webDriver) {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders.put("accept", JSON.toString());
        authHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        authHeaders.put("Authorization", Properties.getPropertyValue("Basic"));

        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "client_credentials");
        String bearerToken = grantAuthorizationTokenExtractToken(authHeaders, formParams);

        PlayerSignOn playerSignOn = ((Response) playerSignOn(getCommonHeaders(bearerToken),
                Properties.getPropertyValue("canvas.user"),
                Properties.getPropertyValue("canvas.password")))
                .then()
                .statusCode(200)
                .extract()
                .as(PlayerSignOn.class);

        String sessionToken = playerSignOn.getSessionToken();

        List<Integer> favoritePlaySlipsIds;
        do {
            GetMyFavoritePlayslips getMyFavoritePlayslips = ((Response) getMyFavoritePlaySlips(getHeadersWithSessionToken(bearerToken, sessionToken)))
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(GetMyFavoritePlayslips.class);
            favoritePlaySlipsIds = getMyFavoritePlayslips.getContent().stream().map(Content::getId).collect(Collectors.toList());
            for (Integer favoritePlaySlipId : favoritePlaySlipsIds) {
                deleteMyFavoritePlaySlipById(getHeadersWithSessionToken(bearerToken, sessionToken), favoritePlaySlipId);
            }
        }
        while (favoritePlaySlipsIds.size()>0);

        logInCanvas(webDriver, canvasUrl, username, password);
    }

    @AfterEach
    public void tearDown(WebDriver webDriver) {
        logOutCanvas(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1561 :: My Favorites | Favorite Playslips | Add a DC3 Straight(000)&Multiplier $1.00 Game")
    public void QAAUT$1561$MyFavoritesFavoritePlayslipsAddADC3Straight000Multiplier1Game(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game type")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"))
                .isTrue();

        dcGamesManualPlay.setMultiplierOptionTo$1()
                .enterNumberToPlay(0, "0")
                .enterNumberToPlay(1, "0")
                .enterNumberToPlay(2, "0")
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc3.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                        .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                        .isTrue();

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
        for (String game : gamesInCoupon) {
            assertWithMessage("incorrect game details")
                    .that(game.equalsIgnoreCase("0\n" +
                            "0\n" +
                            "0\n" +
                            "Play Type = \n" +
                            "Straight"))
                    .isTrue();
        }

        assertWithMessage("incorrect game")
                .that(myFavoriteCoupons.getGameDetail("Game").equalsIgnoreCase("DC-3"))
                .isTrue();

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        assertWithMessage("incorrect number of boards")
                .that(myFavoriteCoupons.getGameDetail("Plays").equalsIgnoreCase("1"))
                .isTrue();

        assertWithMessage("incorrect number of draws")
                        .that(myFavoriteCoupons.getGameDetail("Drawing(s)").equalsIgnoreCase("2"))
                       .isTrue();

        assertWithMessage("incorrect cost")
                .that(myFavoriteCoupons.getGameDetail("Cost").equalsIgnoreCase("$1.00"))
                .isTrue();

        deleteFavoriteCoupon(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1562 :: My Favorites | Favorite Playslips | Edit a DC3 Straight Game | Add a Quick Play")
    public void QAAUT$1562$MyFavoritesFavoritePlayslipsEditADC3StraightGameAddAQuickPlay(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game type")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"))
                .isTrue();

        dcGamesManualPlay.setMultiplierOptionTo$1()
                .enterNumberToPlay(0, "0")
                .enterNumberToPlay(1, "0")
                .enterNumberToPlay(2, "0")
                .deselectDrawTime("Evening", Properties.getPropertyValue("dc3.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

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

        homePage.clickHeaderLogo();

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
        for (String game : gamesInCoupon) {
            assertWithMessage("incorrect game details")
                    .that(game.equalsIgnoreCase("0\n" +
                            "0\n" +
                            "0\n" +
                            "Play Type = \n" +
                            "Straight"))
                    .isTrue();
        }

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        myFavoriteCoupons.clickButton("Edit Favorite");

        DCGamesFavorites dcGamesFavorites = new DCGamesFavorites(webDriver);

        assertWithMessage("incorrect page")
                .that(dcGamesFavorites.getFavoriteGameName().equalsIgnoreCase(name))
                .isTrue();

        dcGamesManualPlay.clickAddQuickPlayButton();

        dcGamesFavorites.clickUpdateFavoriteButton();

        saveFavoritesModal.clickButtonInModal("Save");

        successAddToFavoritesModal.clickCloseButton();

        homePage.clickUserIcon();

        myFavorites.clickMyFavoriteCouponsButton();

        assertWithMessage("not sorted by descending date")
                .that(myFavorites.areGamesSortedByDescendingDate())
                .isTrue();
        myFavorites.clickTheLatestFavorite();

        gamesInCoupon = myFavoriteCoupons.getListOfGamesInTheFavoriteCoupon();
        assertWithMessage("favorite is not updated")
                .that(gamesInCoupon.size())
                .isEqualTo(2);

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        deleteFavoriteCoupon(webDriver);
    }

    @Test
    @DisplayName("QAAUT-1564 :: My Favorites | Favorite Playslips | Edit a DC3 Straight Game | Add a Play Straight/Box")
    public void QAAUT$1564$MyFavoritesFavoritePlayslipsEditADC3StraightGameAddAQuickPlayAddAPlayStraightBox(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game type")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"))
                .isTrue();

        dcGamesManualPlay.setMultiplierOptionTo$1()
                .enterNumberToPlay(0, "0")
                .enterNumberToPlay(1, "0")
                .enterNumberToPlay(2, "0")
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc3.game.id"));
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

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

        homePage.clickHeaderLogo();

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
        for (String game : gamesInCoupon) {
            assertWithMessage("incorrect game details")
                    .that(game.equalsIgnoreCase("0\n" +
                            "0\n" +
                            "0\n" +
                            "Play Type = \n" +
                            "Straight"))
                    .isTrue();
        }

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        myFavoriteCoupons.clickButton("Edit Favorite");

        DCGamesFavorites dcGamesFavorites = new DCGamesFavorites(webDriver);

        assertWithMessage("incorrect page")
                .that(dcGamesFavorites.getFavoriteGameName().equalsIgnoreCase(name))
                .isTrue();

        dcGamesManualPlay.clickAddPlayButton();

        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox();
        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();
        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();
        dcGamesFavorites.clickUpdateFavoriteButton();

        saveFavoritesModal.clickButtonInModal("Save");

        successAddToFavoritesModal.clickCloseButton();

        homePage.clickUserIcon();

        myFavorites.clickMyFavoriteCouponsButton();

        assertWithMessage("not sorted by descending date")
                .that(myFavorites.areGamesSortedByDescendingDate())
                .isTrue();
        myFavorites.clickTheLatestFavorite();

        gamesInCoupon = myFavoriteCoupons.getListOfGamesInTheFavoriteCoupon();
        assertWithMessage("favorite is not updated")
                .that(gamesInCoupon.size())
                .isEqualTo(2);

        assertWithMessage("incorrect game details")
                .that(gamesInCoupon.get(0).equalsIgnoreCase("0\n" +
                        "0\n" +
                        "0\n" +
                        "Play Type = \n" +
                        "Straight"))
                .isTrue();

        assertWithMessage("incorrect game details")
                .that(gamesInCoupon.get(1).contains(
                        "Play Type = \n" +
                                "Straight"))
                .isTrue();

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        deleteFavoriteCoupon(webDriver);

    }

    @Test
    @DisplayName("QAAUT-1565 :: My Favorites | Favorite Playslips | Edit | Update a DC3 type Straight to type Straight/Box")
    public void QAAUT$1565$MyFavoritesFavoritePlayslipsEditUpdateADC3TypeStraightToTypeStraightBox(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game type")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"))
                .isTrue();

        dcGamesManualPlay.setMultiplierOptionTo$1()
                .enterNumberToPlay(0, "0")
                .enterNumberToPlay(1, "0")
                .enterNumberToPlay(2, "0")
                .deselectDrawTime("Select All", Properties.getPropertyValue("dc3.game.id"));

        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

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

        homePage.clickHeaderLogo();

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
        for (String game : gamesInCoupon) {
            assertWithMessage("incorrect game details")
                    .that(game.equalsIgnoreCase("0\n" +
                            "0\n" +
                            "0\n" +
                            "Play Type = \n" +
                            "Straight"))
                    .isTrue();
        }

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        myFavoriteCoupons.clickButton("Edit Favorite");

        DCGamesFavorites dcGamesFavorites = new DCGamesFavorites(webDriver);

        assertWithMessage("incorrect page")
                .that(dcGamesFavorites.getFavoriteGameName().equalsIgnoreCase(name))
                .isTrue();

        dcGamesManualPlay.clickToExpandGameOptionDropdownList()
                .selectOptionStraightBox();
        ConfirmationModal confirmationModal = new ConfirmationModal(webDriver);
        confirmationModal.clickButtonInModal("Yes");

        String multiplierValue = dcGamesManualPlay.getSelectedOptionMultiplierValue();
        //assert multiplier per selection
        assertWithMessage("multiplier is incorrect")
                .that(multiplierValue.equalsIgnoreCase("1.00"))
                .isTrue();
        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection()
                .setMultiDaysTo2();
        dcGamesFavorites.clickUpdateFavoriteButton();

        saveFavoritesModal.clickButtonInModal("Save");

        successAddToFavoritesModal.clickCloseButton();

        homePage.clickUserIcon();

        myFavorites.clickMyFavoriteCouponsButton();

        assertWithMessage("not sorted by descending date")
                .that(myFavorites.areGamesSortedByDescendingDate())
                .isTrue();
        myFavorites.clickTheLatestFavorite();

        gamesInCoupon = myFavoriteCoupons.getListOfGamesInTheFavoriteCoupon();
        assertWithMessage("favorite is not updated")
                .that(gamesInCoupon.size())
                .isEqualTo(1);

        assertWithMessage("incorrect number of draws")
                .that(myFavoriteCoupons.getGameDetail("Drawing(s)").equalsIgnoreCase("4"))
                .isTrue();

        assertWithMessage("incorrect game details")
                .that(gamesInCoupon.get(0).contains(
                        "Play Type = \n" +
                                "Straight/Box"))
                .isTrue();

        assertWithMessage("incorrect Name")
                .that(myFavoriteCoupons.getGameDetail("Name").equalsIgnoreCase(name))
                .isTrue();

        deleteFavoriteCoupon(webDriver);

    }
    @Test
    @DisplayName("QAAUT-1576 :: My Favorites | Favorite Playslips | Max Favorite Number of DC3 Games")
    public void QAAUT$1576$MyFavoritesFavoritePlayslipsMaxFavoriteNumberOfDC3Games(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        assertWithMessage("incorrect game type")
                .that(dcGamesManualPlay.getSelectedOptionValue().equalsIgnoreCase("Straight"))
                .isTrue();

        dcGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();
        String numberOfDaysToBet = dcGamesManualPlay.getNumberOfDays().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDaysToBet.equalsIgnoreCase("2"))
                .isTrue();

        String favoriteName;
        Integer numOfGames = 10;
        SaveFavoritesModal saveFavoritesModal = new SaveFavoritesModal(webDriver);
        for (Integer i = 0; i < numOfGames; i++) {
            dcGamesManualPlay.clickAddToFavoritesButton();
            favoriteName = i.toString();

            assertWithMessage("incorrect modal")
                    .that(saveFavoritesModal.getModalText().equalsIgnoreCase("Enter favorite name"))
                    .isTrue();
            saveFavoritesModal.enterNameForFavoritePlaySlip(favoriteName)
                    .clickButtonInModal("Save");

            SuccessAddToFavoritesModal successAddToFavoritesModal = new SuccessAddToFavoritesModal(webDriver);
            assertWithMessage("add to favorite is not successful")
                    .that(successAddToFavoritesModal.getSuccessTitle().equalsIgnoreCase("Success"))
                    .isTrue();
            successAddToFavoritesModal.clickCloseButton();
        }

        dcGamesManualPlay.clickAddToFavoritesButton();

        assertWithMessage("incorrect modal")
                .that(saveFavoritesModal.getModalText().equalsIgnoreCase("Enter favorite name"))
                .isTrue();
        saveFavoritesModal.enterNameForFavoritePlaySlip("Error")
                .clickButtonInModal("Save");


        NumberOfFavoriteGamesOverTheMaxErrorModal numberOfFavoriteGamesOverTheMaxErrorModal = new NumberOfFavoriteGamesOverTheMaxErrorModal(webDriver);
        assertWithMessage("incorrect modal")
                .that(numberOfFavoriteGamesOverTheMaxErrorModal.getErrorMessage().contains("The number of Favorite Tickets is over the allowed limit."))
                .isTrue();
        numberOfFavoriteGamesOverTheMaxErrorModal.clickCloseButton();

        saveFavoritesModal.clickButtonInModal("Cancel");

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

        while (myFavorites.numberOfFavoriteGames()>1) {
            myFavorites.clickTheLatestFavorite();
            deleteFavoriteCoupon(webDriver);
        }

        myFavorites.clickTheLatestFavorite();
        deleteFavoriteCoupon(webDriver);

        assertWithMessage("message are not deleted")
                .that(myFavorites.getNoFavoritesMessage().contains("No favorite games found"));

    }

    @Disabled
    @DisplayName("new favorite coupons test")
    public void test(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);
        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("DC-3");

        DCGamesManualPlay dcGamesManualPlay = new DCGamesManualPlay(webDriver);
        enterNumbersToPlayDCGamesIncremental(webDriver, 3, 3);
        dcGamesManualPlay.clickAddToFavoritesButton();

        GamePageCommonElements gamePageCommonElements= new GamePageCommonElements(webDriver);
        gamePageCommonElements.selectGameEntryTypeTab("FAVORITE PLAYS");

        SaveFavoritesModal saveFavoritesModal = new SaveFavoritesModal(webDriver);
        String favoriteName = "new";
        saveFavoritesModal.enterNameForFavoritePlaySlip(favoriteName)
                .clickButtonInModal("Save");

        SuccessAddToFavoritesModal successAddToFavoritesModal = new SuccessAddToFavoritesModal(webDriver);

        successAddToFavoritesModal.clickCloseButton();

        FavoritePlaysTab favoritePlaysTab = new FavoritePlaysTab(webDriver);

        favoritePlaysTab.expandFavoriteCouponsList();

    }

}
