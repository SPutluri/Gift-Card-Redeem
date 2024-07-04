package pages;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.LoggerUtil;



public class GiftCardPage extends BasePage {
	
	public GiftCardPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}


	@FindBy(id = "giftCardSearch")
	private WebElement gift_card_search;
	
	@FindBy(xpath = "//*[@id='main-content']/div/div[4]/div[2]/a")
	private WebElement gift_card_click;
	
	@FindBy(xpath = "//h2[text()='No gift cards match your search']")
	private WebElement gift_card_no_search_result;
	
	
	public void onPage(String page) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.urlContains(page));
		String page_url = driver.getCurrentUrl();
		Assert.assertTrue(page_url.contains(page), "ERROR: Not on Gift card page");
		LoggerUtil.log("INFO: On Gift card page");
		
	}
	
	
	
	public void enterSearchKeyword(String keyword) {
		 WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        mywait.until(ExpectedConditions.visibilityOf(gift_card_search));
		gift_card_search.sendKeys(keyword);
	}
	
	public void clickGiftCard() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		Thread.sleep(4000);
        js.executeScript("document.getElementsByClassName('p-6')[0].click()");
        LoggerUtil.log("INFO: Gift Card clicked");
	}
	
//	public void clickGiftCard() {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//        
//        // Wait for the element to be present
//        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("p-6")));
//        
//        // Verify if the element is not null
//        if (element != null) {
//            // Use JavaScript to click the element
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
//        } else {
//            throw new RuntimeException("Element with class 'p-6' not found.");
//        }
//    }
	
	public void noGiftCardResults() {
		Assert.assertTrue(gift_card_no_search_result.isDisplayed(), "ERROR: Gift cards Found");
		LoggerUtil.log("INFO: Gift Card No Results Section is Displayed");
		
		}

	
	
}
