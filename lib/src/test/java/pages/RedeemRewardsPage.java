//package pages;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.FindBy;
//import org.testng.Assert;
//
//public class RedeemRewardsPage extends BasePage {
//	
//	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[2]/div[2]/div[1]/form/div[1]/div/button[1]")
//	private WebElement select_giftcard_worth;
//	
//	
//	@FindBy(xpath = "//p[@data-testid='price']")
//	private WebElement redeemable_points;
//	
//	
//	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[2]/div[2]/div[1]/form/button")
//	private WebElement redeem_now_click;
//	
//	@FindBy(xpath = "//div[@class='pt-2']/span/input")
//	private WebElement giftcard_quantity;
//	
//	@FindBy(xpath = "//h2[text()='Instant Redemption']")
//	private WebElement redeemrewardspage_text;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button")
//	private List<WebElement> gift_card_values;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button[1]")
//	private WebElement gift_card_value_20;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button[2]")
//	private WebElement gift_card_value_25;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button[3]")
//	private WebElement gift_card_value_30;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button[4]")
//	private WebElement gift_card_value_40;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button[4]")
//	private WebElement gift_card_value_50;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button[5]")
//	private WebElement gift_card_value_60;
//	
//	@FindBy(xpath = "//div[@class='mb-10']/div/button[6]")
//	private WebElement gift_card_value_100;
//	
//
//	public RedeemRewardsPage(WebDriver driver) {
//		super(driver);
//		// TODO Auto-generated constructor stub
//	}
//	
//	public void onPage() {
//		Assert.assertTrue(redeemrewardspage_text.isDisplayed(),"Not on Redeem Rewards Page");
//	}
//	
//	public void selectGiftCardValue(String value) {
//		ArrayList al = new ArrayList();
//		for(WebElement gc_value : gift_card_values) {
//		al.add (gc_value.getText());
//		}
//		if(al.contains(value)) {
//			switch (value) {
//            case "$20":
//            	gift_card_value_20.click();
//            	break;
//            case "$25":
//            	gift_card_value_25.click();
//            	break;	
//            case "$30":
//            	gift_card_value_30.click();
//            	break;	
//            case "$40":
//            	gift_card_value_40.click();
//            	break;		
//            case "$50":
//            	gift_card_value_50.click();
//            	break;	
//            case "$60":
//            	gift_card_value_60.click();
//            	break;
//            case "$100":
//            	gift_card_value_100.click();
//            	break;
//            default:
//            	gift_card_value_20.click();
//            	break;
//            	}
//		
//	}
//		else {
//			Assert.assertFalse(al.contains(value), "Please choose to enter the values in {$20, $25, $30, $40, $50, $60, $100}");
//		}
//	}
//	
//	
//	
//	public void selectGiftCardWorth() {
//		select_giftcard_worth.click();
//	}
//	
//	public void selectGiftCardQuantityAndRedeem(String quantity) {
//		giftcard_quantity.sendKeys(quantity);
//		Actions act = new Actions(driver);
//		act.keyDown(Keys.ENTER)
//        .keyUp(Keys.ENTER)
//        .perform();
//		
//	}
//	
//	
//	public void clickGiftCard() {
//		redeem_now_click.click();
//	}
//	
//
//}


package pages;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.stream.Collectors;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.sikuli.script.FindFailed;
import org.testng.Assert;
import org.testng.log4testng.Logger;

import tests.TestRunner;
import utils.LoggerUtil;


public class RedeemRewardsPage extends BasePage {
	public static Properties prop;
	
	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[2]/div[2]/div[1]/form/div[1]/div/button[1]")
	private WebElement selectGiftcardWorth;
	
	@FindBy(xpath = "//p[@data-testid='price']")
	public WebElement redeemablePoints;
	
	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[2]/div[2]/div[1]/form/button")
	private WebElement redeemNowClick;
	
	@FindBy(xpath = "//div[@class='pt-2']/span/input")
	private WebElement giftcardQuantity;
	
	@FindBy(xpath = "//h2[text()='Instant Redemption']")
	private WebElement redeemRewardsPageText;
	
	@FindBy(xpath = "//div[@class='mb-10']/div/button")
	private List<WebElement> giftCardValues;

	public RedeemRewardsPage(WebDriver driver) throws IOException {
		super(driver);
		prop = new Properties();
        FileInputStream file = new FileInputStream("./src/test/resources/config.properties");
        prop.load(file);
	}
	
	public void onPage() {
		FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.visibilityOf(redeemRewardsPageText));
		Assert.assertTrue(redeemRewardsPageText.isDisplayed(), "Not on Redeem Rewards Page");
		LoggerUtil.log("on Redemption page");
	}
	
	public String selectGiftCardValue(String value) {
		List<String> values = giftCardValues.stream()
			.map(WebElement::getText)
			.collect(Collectors.toList());
		
		if (!values.contains(value)) {
			Assert.fail("ERROR: Please choose to enter the values in {$20, $25, $30, $40, $50, $60, $100}");
			return "invalid value";
		}
		
		for (WebElement gcValue : giftCardValues) {
			if (gcValue.getText().equals(value)) {
				gcValue.click();
				return value;
			}
		}
		return value;
		
		
	}
	
	public void selectGiftCardWorth() {
		selectGiftcardWorth.click();
		LoggerUtil.log("INFO: Gift card Value Selected");
	}
	
	public int selectGiftCardQuantityAndRedeem(String quantity) {
	    // Clear the text field using different strategies
	    giftcardQuantity.clear(); // Clear method
	    giftcardQuantity.sendKeys(Keys.CONTROL + "a"); // Select all text
	 
	    
//	    // Ensure the text field is cleared
//	    if (!giftcardQuantity.getAttribute("value").isEmpty()) {
//	        giftcardQuantity.sendKeys(Keys.CONTROL + "a");
//	        giftcardQuantity.sendKeys(Keys.DELETE);
//	    }
	    
	    // Send the new quantity
	    giftcardQuantity.sendKeys(quantity);

	    // Use Actions class to press Enter
	    Actions act = new Actions(driver);
	    act.sendKeys(Keys.ENTER).perform();
	    
	    // Retrieve the value from the text field and return it as an integer
	    String value = giftcardQuantity.getAttribute("value");
	    return Integer.parseInt(value);
	}
	
	
	public void clickRedeemNow() {
		redeemNowClick.click();
		LoggerUtil.log("INFO: Redeem Now Button Clicked on Redeem Reward Points Page");
	}
	
	public void insufficientBalance() {
		Assert.assertTrue(!(redeemNowClick.isEnabled()), "Redeem Now button is Enabled");
		LoggerUtil.log("INFO: Reward balance insufficient");
	
	}
	
	public String getExistingPointBalance() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    String points = (String) js.executeScript(
	        "return document.querySelector(\"p[data-testid='header-points-balance']\").querySelector(\".font-bold\").textContent.match(/\\d{1,3}(,\\d{3})*/)[0];"
	    );
	    return points;
	}
	public int calculateRedeemRewards() {
		String value = selectGiftCardValue(prop.getProperty("giftcardfailureredeem"));
		int gcquantity = selectGiftCardQuantityAndRedeem(prop.getProperty("giftcardqty"));
		int redeemable_points = Integer.parseInt(value.replaceAll("[^0-9]", "")) * gcquantity * 100;
		LoggerUtil.log("INFO: Redeemable Points on Redeem Rewards Page are claculated as :" + redeemable_points );
		return redeemable_points;
	}
	
	
}

