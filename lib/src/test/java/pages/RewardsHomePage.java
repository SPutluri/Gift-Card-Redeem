package pages;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.LoggerUtil;

public class RewardsHomePage extends BasePage {

    @FindBy(id = "radix-:r3:-trigger-redeem-menu-group")
    private WebElement redeem_rewards;

    @FindBy(xpath = "//a[@href='/products/gift-cards']")
    private WebElement giftcard_click;

    @FindBy(xpath = "//li[text()=\"Privacy Policy\"]")
    private WebElement privacy_policy;

    @FindBy(className = "rounded-button-primary")
    private WebElement accept_btn;

    private By privacyPolicyLocator = By.xpath("//li[text()=\"Privacy Policy\"]");

    public RewardsHomePage(WebDriver driver) {
        super(driver);
    }

    public void checkAcceptTerms() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('terms-and-conditions-modal-accept-checkbox').click()");
    }

    public void clickAcceptBtn() {
        accept_btn.click();
    }

    public void onPage(String page) {
        // Use FluentWait to ensure the page is fully loaded
        FluentWait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.titleContains("Home | Rewards"));

        String page_title = driver.getTitle();
        Assert.assertTrue(page_title.contains(page), "ERROR: Not on Home Page");
    }

    public Boolean privacyPolicyDisplay() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(privacyPolicyLocator));
            return privacy_policy.isDisplayed();
        } catch (NoSuchElementException e) {
            // Handle or log the exception if needed
            LoggerUtil.log("Privacy policy not displayed: " + e.getMessage());
            return false;
        }
    }

    public void hoverRedeemRewards() {
        Actions act = new Actions(driver);
        act.moveToElement(redeem_rewards).build().perform();
    }

    public void clickGiftCard() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('a[href=\"/products/gift-cards\"]').click()");
    }

    // Getter method for privacyPolicyLocator
    public By getPrivacyPolicyLocator() {
        return privacyPolicyLocator;
        
    }
}
