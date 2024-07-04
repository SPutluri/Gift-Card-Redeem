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

public class RewardSummaryPage extends BasePage {
	
	

	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[1]/div[2]/div/div/button[2]")
	private WebElement view_my_rewards_btn;
	
	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[1]/div[2]/div/div/button[1]")
	private WebElement continue_browsing_btn;
	
	@FindBy(linkText = "My Rewards")
	private WebElement my_rewards_link;
	
	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[1]/div[2]/p[1]/span")
	private WebElement rewards_id;
	
	@FindBy(className = "my-6")
	private WebElement rewardsid;
	
	
	
	
	
	public RewardSummaryPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public void onPage() {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.visibilityOf(rewardsid));
		Assert.assertTrue(rewardsid.isDisplayed(), "Not on Rewards Summary Page");
		LoggerUtil.log("INFO: On Rewards Summary Page");
	}
	
	public void clickViewRewardsBtn() {
		view_my_rewards_btn.click();
		LoggerUtil.log("INFO: View My Rewards Button Clicked");
	}
	
	public void clickContinueBrowsingBtn() {
		continue_browsing_btn.click();
		LoggerUtil.log("INFO: Continue Browsing Button Clicked");
	}
	
	public void clickMyRewardsLink() {
		my_rewards_link.click();
		LoggerUtil.log("INFO: My Rewards Link Clicked");
	}
	
	public String getRewardsId() {
		String reward_id= rewards_id.getText();
		return reward_id;
	}
	public String getPointBalance() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    String points = (String) js.executeScript(
	        "return document.querySelector(\"p[data-testid='header-points-balance']\").querySelector(\".font-bold\").textContent.match(/\\d{1,3}(,\\d{3})*/)[0];"
	    );
	    LoggerUtil.log("INFO: Points on Reward Summary:"+ points);
	    return points;
	}
	
	
	
	
	//*[@id="main-content"]/div/div[1]/div/div[1]/div[2]/p[2]/span/a
}
