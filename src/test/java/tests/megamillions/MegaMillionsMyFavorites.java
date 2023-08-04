package tests.megamillions;

import base.test.BaseTest;
import canvas.page.objects.HomePage;
import canvas.page.objects.account.drawer.AccountDrawer;
import canvas.page.objects.account.drawer.Tabs;
import canvas.page.objects.account.drawer.preferences.myfavorites.MyFavorites;
import canvas.page.objects.games.MegaMillionsGamesManualPlay;
import canvas.page.objects.games.drawer.GamesDrawer;
import canvas.page.objects.modals.NumberOfFavoriteGamesOverTheMaxErrorModal;
import canvas.page.objects.modals.SaveFavoritesModal;
import canvas.page.objects.modals.SuccessAddToFavoritesModal;
import common.utils.Properties;
import io.restassured.response.Response;
import lottery.apigatewayj.models.pam.players.PlayerSignOn;
import models.apigatewayj.myprofileservice.getmyfavoriteplayslips.Content;
import models.apigatewayj.myprofileservice.getmyfavoriteplayslips.GetMyFavoritePlayslips;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import selenium.WebDriverInit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class MegaMillionsMyFavorites extends BaseTest {

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
    @DisplayName("QAAUT-1580 :: My Favorites | Favorite Playslips | Max Favorite Number of MegaMillions Games")
    public void QAAUT$1580$MyFavoritesFavoritePlayslipsMaxFavoriteNumberOfMegaMillionsGames(WebDriver webDriver) {

        HomePage homePage = new HomePage(webDriver);

        homePage.clickLinkInTopMenu("GAMES");

        GamesDrawer gamesDrawer = new GamesDrawer(webDriver);
        gamesDrawer.clickBuyNowButtonInGamesDrawer("MEGA MILLIONS");

        MegaMillionsGamesManualPlay megaMillionsGamesManualPlay = new MegaMillionsGamesManualPlay(webDriver);


        megaMillionsGamesManualPlay.clickQuickPicksButtonUnderNumbersSection();
        String numberOfDraws = megaMillionsGamesManualPlay.getNumberOfDraws().replace("x ", "");
        //assert number of days per draw time selection
        assertWithMessage("number of days to play game is incorrect")
                .that(numberOfDraws.equalsIgnoreCase("1"))
                .isTrue();

        String favoriteName;
        Integer numOfGames = 10;
        SaveFavoritesModal saveFavoritesModal = new SaveFavoritesModal(webDriver);
        for (Integer i = 0; i < numOfGames; i++) {
            megaMillionsGamesManualPlay.clickAddToFavoritesButton();
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


        megaMillionsGamesManualPlay.clickAddToFavoritesButton();


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

}
