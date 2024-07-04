package pages;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import utils.LoggerUtil;



public class InfoReviewDetailsPage extends BasePage {
	

	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[1]/div[3]/form/div[4]/button[2]")
	private WebElement review_btn;
	
	
	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[1]/div[3]/form/div[4]/button[1]")
	private WebElement back_to_reward_btn;
	
	@FindBy(name = "firstName")
	private WebElement first_name_review;
	
	@FindBy(name = "lastName")
	private WebElement last_name_review;
	
	@FindBy(name = "email")
	private WebElement email_review;
	
	
	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[2]/div/div[2]/div/div[2]/p[1]")
	private WebElement review_quantity;
	
	@FindBy(xpath = "//p[@data-testid=\"summary-total-amount\"][1]")
	private WebElement reward_points_review;
	
	
	public InfoReviewDetailsPage(WebDriver driver) {
		super(driver);
		
	}
	
	public void onPage(String page) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.urlContains(page));
		String page_url = driver.getCurrentUrl();
		System.out.println(page_url);
		Assert.assertTrue(page_url.contains(page), "Not on Checkout page");
		System.out.println("On Checkout page");
		
	}
	
	
	public void reviewBtnClick() {
		review_btn.click();
		LoggerUtil.log("INFO: Review Button Clicked on Review Details page");
	}
	
	public String firstNameReview() {
	return first_name_review.getAttribute("value");
	
	}
	
	public String lastNameReview() {
		return last_name_review.getAttribute("value");
		 
		}
	
	public String emailReview() {
		return email_review.getAttribute("value");
		 
		}
	
	public int reviewQuantity() {
		return Integer.parseInt(review_quantity.getText().replaceAll("[^0-9]", ""));
		
		
	}
	
	public String reviewRewardPoints() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String rewardtext =	(String)(js.executeScript("return document.querySelector(\"p[data-testid='summary-total-amount']\").textContent"));
		return rewardtext;
 
		 
	}
	
	
	

}
