package canvas.page.objects;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.SeleniumWaits;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class HomePage {

    WebDriver webDriver;

    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getPageTitle() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.className("app_logo")));
        WebElement titleText =  webDriver.findElement(By.className("app_logo"));

        String titleTextAsString = titleText.getText();
        return titleTextAsString;
    }

    public String getBasketProductName() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='item_1_title_link']/div")));

        WebElement productName =  webDriver.findElement(By.xpath("//*[@id='item_1_title_link']/div"));
        String productNameString = productName.getText();
        return productNameString;
    }

    public String getProductName() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id=\"inventory_item_container\"]/div/div/div[2]/div[1]")));

        WebElement productName =  webDriver.findElement(By.xpath("//*[@id=\"inventory_item_container\"]/div/div/div[2]/div[1]"));
        String productNameString = productName.getText();
        return productNameString;
    }

    public String getBasketProductAmount() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='cart_contents_container']/div/div[1]/div[3]/div[2]/div[2]/div")));

        WebElement productAmount =  webDriver.findElement(By.xpath("//*[@id='cart_contents_container']/div/div[1]/div[3]/div[2]/div[2]/div"));
        String productAmountString = productAmount.getText();
        return productAmountString;
    }

    public String getProductAmount() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='inventory_container']/div/div[3]/div[2]/div[2]/div")));

        WebElement productAmount =  webDriver.findElement(By.xpath("//*[@id='inventory_container']/div/div[3]/div[2]/div[2]/div"));
        String productAmountString = productAmount.getText();
        return productAmountString;
    }
    public String getInnerPageProductAmount() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='inventory_item_container']/div/div/div[2]/div[3]")));
        WebElement productAmount =  webDriver.findElement(By.xpath("//*[@id='inventory_item_container']/div/div/div[2]/div[3]"));
        String productAmountString = productAmount.getText();
        return productAmountString;
    }

    public HomePage cookiesAbout() {
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='onetrust-accept-btn-handler']")));
        WebElement cookieButton =  webDriver.findElement(By.xpath("//*[@id='onetrust-accept-btn-handler']"));
        cookieButton.click();
        return this;
    }

    public HomePage clickLogout() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='logout_sidebar_link']")));
        WebElement logoutButton =  webDriver.findElement(By.xpath("//*[@id='logout_sidebar_link']"));
        logoutButton.click();
        return this;
    }

    public HomePage clickCheckout() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='checkout']")));
        WebElement logoutButton =  webDriver.findElement(By.xpath("//*[@id='checkout']"));
        logoutButton.click();
        return this;
    }

    public HomePage clickAbout() {
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='about_sidebar_link']")));
        WebElement aboutButton =  webDriver.findElement(By.xpath("//*[@id='about_sidebar_link']"));
        aboutButton.click();
        return this;
    }

    public HomePage clickMenu() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='react-burger-menu-btn']")));
        WebElement menuButton =  webDriver.findElement(By.xpath("//*[@id='react-burger-menu-btn']"));
        menuButton.click();
        return this;
    }

    public HomePage removeFromCart() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='remove-sauce-labs-bolt-t-shirt']")));
        WebElement removeButton =  webDriver.findElement(By.xpath("//*[@id='remove-sauce-labs-bolt-t-shirt']"));
        removeButton.click();
        return this;
    }

    public HomePage openBasket() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id=\"shopping_cart_container\"]/a")));
        WebElement titleText =  webDriver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
        titleText.click();
        return this;
    }
    public HomePage clickAddToCart() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='add-to-cart-sauce-labs-bolt-t-shirt']")));
        WebElement titleText =  webDriver.findElement(By.xpath("//*[@id='add-to-cart-sauce-labs-bolt-t-shirt']"));
        titleText.click();
        return this;
    }
    public HomePage clickproduct(String link) {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='"+link+"']")));
        WebElement titleText =  webDriver.findElement(By.xpath("//*[@id=\"item_1_title_link\"]"));
        titleText.click();
        return this;
    }

    //*[@id="continue"]
    public HomePage clickContinue() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id=\"continue\"]")));
        WebElement cont =  webDriver.findElement(By.xpath("//*[@id=\"continue\"]"));
        cont.click();
        return this;
    }

    public HomePage clickFinish() {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='finish']")));
        WebElement cont =  webDriver.findElement(By.xpath("//*[@id='finish']"));
        cont.click();
        return this;
    }

    public HomePage fillFirstName(String firstName) {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='first-name']")));
        WebElement fname =  webDriver.findElement(By.xpath("//*[@id='first-name']"));
        fname.sendKeys(firstName);
        return this;
    }

    public HomePage fillLastName(String lastName) {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='last-name']")));
        WebElement lname =  webDriver.findElement(By.xpath("//*[@id='last-name']"));
        lname.sendKeys(lastName);
        return this;
    }

    public HomePage fillZipCode(String zipCode) {
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//*[@id='postal-code']")));
        WebElement pcode =  webDriver.findElement(By.xpath("//*[@id='postal-code']"));
        pcode.sendKeys(zipCode);
        return this;
    }

    public String getCheckoutProductName() {
        String path="//*[@id='item_1_title_link']/div";
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath(path)));

        WebElement productName =  webDriver.findElement(By.xpath(path));
        String productNameString = productName.getText();
        return productNameString;
    }
    public String getCheckoutProductAmount() {
        String path="//*[@id=\"checkout_summary_container\"]/div/div[1]/div[3]/div[2]/div[2]/div";
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath(path)));

        WebElement productAmount =  webDriver.findElement(By.xpath(path));
        String productAmountString = productAmount.getText();
        return productAmountString;
    }

    public String getProductTax() {
        String path="//*[@id=\"checkout_summary_container\"]/div/div[2]/div[7]";
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath(path)));

        WebElement productAmount =  webDriver.findElement(By.xpath(path));
        String taxString = productAmount.getText();
        return taxString;
    }

    public String getTotalAmount() {
        String path="//*[@id=\"checkout_summary_container\"]/div/div[2]/div[8]";
        new WebDriverWait(webDriver, 10).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath(path)));

        WebElement totalAmount =  webDriver.findElement(By.xpath(path));
        String totalAmountString = totalAmount.getText();
        return totalAmountString;
    }




}
