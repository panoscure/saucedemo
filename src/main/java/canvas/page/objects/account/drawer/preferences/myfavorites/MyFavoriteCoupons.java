package canvas.page.objects.account.drawer.preferences.myfavorites;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.SeleniumWaits;

import java.util.List;
import java.util.stream.Collectors;

public class MyFavoriteCoupons {

    WebDriver webDriver;

    public MyFavoriteCoupons(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public List<String> getListOfGamesInTheFavoriteCoupon() {
        List<WebElement> listOfGamesInTheTicket = SeleniumWaits.visibilityOfAllElementsLocatedBy(webDriver,
                By.xpath("//div[@class='favorites']//div[contains(@class, 'ticket-details')]//div[contains(@class, 'elevation')]"));
        return listOfGamesInTheTicket.stream().map(x->x.getText()).collect(Collectors.toList());
    }

    //game, name, type, boards, draw(s), cost
    public String getGameDetail(String detail) {
        WebElement gameDetail = SeleniumWaits.visibilityOfElementLocated(webDriver,
                By.xpath("//p[text() = '"+detail+"']/following-sibling::div"));
        return gameDetail.getText();
    }

    //My Favorite Coupons, My Favorite Numbers, Edit Favorite, Delete Favorite, Add to cart, Buy Now
    public MyFavoriteCoupons clickButton(String btnName) {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='favorites']//button[text()='"+btnName+"']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        //btn.click();
        return this;
    }

    public MyFavoriteCoupons clickReturnToListButton() {
        WebElement returnToListBtn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class= 'return-msg']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", returnToListBtn);
                // returnToListBtn.click();
        return this;
    }

    public MyFavoriteCoupons clickBuyNowButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='favorites']//button[text()='BUY NOW']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        //btn.click();
        return this;
    }

    public MyFavoriteCoupons clickAddToCartButton() {
        WebElement btn = SeleniumWaits.elementToBeClickable(webDriver,
                By.xpath("//div[@class='favorites']//button[text()='Add to cart']"));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].click();", btn);
        //btn.click();
        return this;
    }


}
