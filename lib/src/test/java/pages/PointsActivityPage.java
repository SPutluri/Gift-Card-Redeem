package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import utils.LoggerUtil;



public class PointsActivityPage extends BasePage {
	
	


	@FindBy(xpath = "//h2[@data-testid='formatted-points-balance']")
	private WebElement reward_points;
	
	@FindBy(xpath = "//button[contains(@id, 'trigger-my-rewards')]")
	private WebElement my_rewards_link;

	@FindBy(xpath = "//td[@data-testid='pa-item-amount']")
	private List<WebElement> amount_calc_column;
	
	public PointsActivityPage(WebDriver driver) {
		super(driver);
		
	}
	
	public void onPage(String page) {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(40))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.urlContains(page));
		String page_url = driver.getCurrentUrl();
		Assert.assertTrue(page_url.contains(page), "ERROR: Not on Points Activity page");
		LoggerUtil.log("INFO: On Points Activity page");
		
	}
	
	
	public List points() {
		 List<String> pointsList = new ArrayList<>();
	        JavascriptExecutor js = (JavascriptExecutor) driver;
		List<String> points = (List<String>) js.executeScript(
	            "let points = []; " +
	            "document.querySelectorAll(\"p[data-testid='points-transaction-signed-amount']\").forEach(element => points.push(element.textContent.trim())); " +
	            "return points;"
	        );

	        // Add all points to the pointsList
	        if (points != null) {
	            pointsList.addAll(points);
	        }

	        return pointsList;	
	        }

	
	public int remainingPointsBalance() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String remaining_point_bal=(String)js.executeScript("return document.querySelector('h2[data-testid=\"formatted-points-balance\"]').textContent");
		int remaining_point_balance = Integer.parseInt(remaining_point_bal.replaceAll("[^0-9]", ""));
		LoggerUtil.log("INFO: Remaining Points balance is "+ remaining_point_balance);
		return remaining_point_balance;
	}
	
	public void myRewardsClick() {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.visibilityOf(my_rewards_link));
		
        my_rewards_link.click();
        LoggerUtil.log("INFO: My Rewards Link Clicked  ");
	}
	

}
