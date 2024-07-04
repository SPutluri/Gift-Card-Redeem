package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import utils.LoggerUtil;

public class InfoReviewPage extends BasePage {
	
	

	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[1]/div[3]/div/button[2]")
	private WebElement redeem_now_btn;
	
	@FindBy(xpath = "//*[@id=\"main-content\"]/div/div[1]/div/div[1]/div[3]/div/button[1]")
	private WebElement back_to_details_btn;
	
	
	public InfoReviewPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public void redeemnowBtnClick() {
		redeem_now_btn.click();
		LoggerUtil.log("INFO: Reddem Now Button Clicked on Review Checkout page");
	}
	
	public void backToDetailsBtnlick() {
		back_to_details_btn.click();
		LoggerUtil.log("INFO: Back To Details Button Clicked on Review Checkout page");
	}
	

}
