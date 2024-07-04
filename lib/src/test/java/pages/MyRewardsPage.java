package pages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import utils.LoggerUtil;


public class MyRewardsPage extends BasePage {
	
	
	public MyRewardsPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	FluentWait<WebDriver> wait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(30))
            .pollingEvery(Duration.ofSeconds(1))
            .ignoring(NoSuchElementException.class);


	@FindBy(className = "font-bold")
	private WebElement reward_points;
	
	@FindBy(xpath = "//button[contains(@id, 'trigger-points-activity')]")
	private WebElement points_activity_link;
	
	@FindBy(xpath = "//div[@data-testid=\"order-status\"]")
	private List<WebElement> order_status;
	
	
	public void onPage(String page) {

        wait.until(ExpectedConditions.urlContains(page));
		String page_url = driver.getCurrentUrl();
		Assert.assertTrue(page_url.contains(page), "Not on My Rewards page");
		System.out.println("On My Rewards page");
		
	}
	
	public void pointActivityClick() {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
	            .withTimeout(Duration.ofSeconds(50))
	            .pollingEvery(Duration.ofSeconds(1))
	            .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.visibilityOf(points_activity_link));
		
		points_activity_link.click();
		LoggerUtil.log("INFO: Point Activity Link is clicked ");
	}
	
	public List returnOrderStatus(){
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
	            .withTimeout(Duration.ofSeconds(60))
	            .pollingEvery(Duration.ofSeconds(1))
	            .ignoring(NoSuchElementException.class);

		wait.until(ExpectedConditions.visibilityOfAllElements(order_status));
		ArrayList<String> al = new ArrayList<String>();
		for(WebElement status: order_status ) {
			String status_text= status.getAttribute("aria-label");
			System.out.println(status_text);
			String reward_status = (status.getAttribute("aria-label")).split(" ")[1];
			al.add(reward_status);
		}
		return al;
		
	}
	

    
	public static void takeFullPageScreenshot(WebDriver driver) throws IOException {
        // Cast driver to TakesScreenshot
        TakesScreenshot ts = (TakesScreenshot) driver;
        
        // Convert web driver object to javascript executor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get initial viewport height and scroll height of the webpage
        Long initialViewHeight = (Long) js.executeScript("return window.innerHeight;");
        Long scrollHeight = (Long) js.executeScript("return document.documentElement.scrollHeight;");

        // Scroll to the end of the page
        js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight);");

        // Wait for the page to load completely (adjust sleep time as needed)
        try {
            Thread.sleep(2000); // Adjust sleep time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Take screenshot of the entire page
        File screenshot = ts.getScreenshotAs(OutputType.FILE);

        // Save screenshot to a temporary file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "fullpage_screenshot_" + timestamp + ".png";

        // Update the file path based on your project structure
        String filePath = "/Users/sravanthiputluri/eclipse-workspace/RedeemRewards/lib/test-output/Ascenda Loyalty Automation/screenshots/" + fileName;
        FileHandler.copy(screenshot, new File(filePath));

        LoggerUtil.log("Full page screenshot saved: " + filePath);
    }
	
	public void refreshPage() {
	    driver.navigate().refresh();
	}
}