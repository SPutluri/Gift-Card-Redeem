package tests;

import java.awt.AWTException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pages.*;
import utils.LoggerUtil;


public class TestRunner extends BaseTest {
    private CreateMagicLink link;
    private String magicUrl;

    @BeforeClass
    protected void initializeCreateMagicLink() throws IOException {
        link = new CreateMagicLink();
        magicUrl = link.getMagiclink();
    }

    @Test(groups = {"smoke","regression"},priority = 1, description = "Create and Launch Magic Link")
    public void launchMagicUrl() throws IOException, InterruptedException, AWTException, FindFailed {
        driver.get(magicUrl);
        RewardsHomePage rewards_hp_actions = new RewardsHomePage(driver);
        rewards_hp_actions.onPage("Home | Rewards");
        LoggerUtil.log("INFO: On Home Page");
    }

    @Test(groups = {"regression"},priority = 1, description = "User searches for an Apple Gift Card")
    public void searchAppleGiftCard() throws IOException, InterruptedException, AWTException, FindFailed {
        launchMagicUrl();
      
        performSearch(prop.getProperty("Keyword"));
    }

    @Test(groups = {"smoke","regression"},priority = 1, description = "User searches for a Gift Card with invalid keyword")
    public void searchInvalidGiftCard() throws IOException, InterruptedException, AWTException, FindFailed {
        launchMagicUrl();
        performSearch(prop.getProperty("invalidKeyword"));
    }

    @Test(groups = {"smoke"},priority = 1, description = "Verify Insufficient balance by Redeeming reward points for gift card which is worth more than reward points in the account")
    public void redeemRewardPointsForGiftCardOverRanged() throws IOException, InterruptedException, AWTException, FindFailed {
        redeemRewardPointsForGiftCard(true);
    }

    @Test(priority = 1, description = "Verify successful redemption of reward points for gift card within points balance")
    public void redeemRewardPointsForGiftCardWithinBalance() throws IOException, InterruptedException, AWTException, FindFailed {
        redeemRewardPointsForGiftCard(false);
    }
    
//    @Test(priority = 5, description = "Verify refund of reward points for gift card within points balance")
//    public void RefundRewardPointsForGiftCard() throws IOException, InterruptedException, AWTException, FindFailed {
//    	redeemRewardPointsForGiftCard(false);
//    }
    

    
    @Test(priority = 1, description = "Validate navigation to Reward Page by Reviewing Recipient Details in Review page")
    public void reviewDetails() throws IOException, InterruptedException, AWTException, FindFailed {
        int redeemablePoints = redeemRewardPointsForGiftCard(false);
        InfoReviewDetailsPage checkout = new InfoReviewDetailsPage(driver);
        checkout.onPage("checkout");
        validateReviewDetails(checkout);
        Assert.assertEquals(redeemablePoints, Integer.parseInt(checkout.reviewRewardPoints().replaceAll("[^0-9]", "")), "Redeemable points don't match");
        checkout.reviewBtnClick();
        completeRedemption();
    }
    

    @Test(priority = 1, description = "Validate Reward points on Point Activity Page and Take screenshot of Rewards Page")
    public void validateRewardPointsOnPointActivity() throws IOException, InterruptedException, AWTException, FindFailed {
    	retunRemainingPointsBalance();
        
    }

    @Test(priority = 1, description = "Validate the remaining points balance against point balance in get user info Response")
    public void validateRemainingPointsBalanceWithGetUserInfoResponse() throws IOException, InterruptedException, AWTException, FindFailed {
        int remainingPointBalance = retunRemainingPointsBalance();
        String balance = getPointBalanceFromAPI();
        Assert.assertEquals(remainingPointBalance, Integer.parseInt(balance.split("\\.")[0]), "ERROR: Points balance doesn't match");
    }
    
    
    

    @Test(priority = 1, description = "Verify the Point activity when the redemption is failed")
    public void redemptionFailedAndPointsUpdated() throws IOException, InterruptedException, AWTException, FindFailed {
        searchAppleGiftCard();
        RedeemRewardsPage rrp = new RedeemRewardsPage(driver);
        rrp.onPage();
        int redeemable_points = rrp.calculateRedeemRewards();
        Assert.assertTrue(
                ((rrp.redeemablePoints.getText()).replaceAll("[^0-9]", "")).contains(String.valueOf(redeemable_points)),
                "ERROR: Redeemable points unmatched");
        int existing_rewards_balance = Integer.parseInt(rrp.getExistingPointBalance().replaceAll("[^0-9]", ""));
        if (existing_rewards_balance > redeemable_points) {
            rrp.clickRedeemNow();
        }
        InfoReviewDetailsPage checkout = new InfoReviewDetailsPage(driver);
        checkout.onPage("checkout");
        checkout.reviewBtnClick();
        InfoReviewPage reviewcheckout = new InfoReviewPage(driver);
        reviewcheckout.redeemnowBtnClick();
        RewardSummaryPage rew_summary = new RewardSummaryPage(driver);
        rew_summary.onPage();
        int rewards_balance_on_reward_summary = Integer.parseInt(rew_summary.getPointBalance().replaceAll("[^0-9]", ""));
        String bal = getPointBalanceFromAPI();
        Assert.assertTrue((Integer.parseInt(bal.split("\\.")[0])) == rewards_balance_on_reward_summary, "ERROR: remaining points balance unmatched ");
        rew_summary.clickViewRewardsBtn();
        MyRewardsPage my_rewards = new MyRewardsPage(driver);
        my_rewards.onPage("my-rewards");

        List<String> orderStatusList = my_rewards.returnOrderStatus();

        // Check if orderStatusList is not empty before accessing elements
        if (!orderStatusList.isEmpty()) {
            if (orderStatusList.get(0).equalsIgnoreCase("pending")) {
                waitUntilOrderStatusChangesToFailed(my_rewards);
                orderStatusList = my_rewards.returnOrderStatus(); // Refresh the order status list
                Assert.assertTrue(orderStatusList.get(0).equalsIgnoreCase("failed"), "ERROR: Order status is still pending");
            }

            if (orderStatusList.get(0).equalsIgnoreCase("failed")) {
            	
            	my_rewards.pointActivityClick();
                PointsActivityPage pointsActivity = new PointsActivityPage(driver);
                pointsActivity.onPage("points-activity");

                int pointActivityBalance = pointsActivity.remainingPointsBalance();
                Assert.assertEquals(pointActivityBalance, rewards_balance_on_reward_summary + redeemable_points, "ERROR: Points not updated");

                String updatedBalance = getPointBalanceFromAPI();
                Assert.assertEquals(pointActivityBalance, Integer.parseInt(updatedBalance.split("\\.")[0]), "ERROR: Points balance doesn't match");
            }
        } else {
            // Handle case where orderStatusList is empty
            Assert.fail("ERROR: Order status list is empty. No order status found.");
        }
    }

    
    public int retunRemainingPointsBalance() throws IOException, FindFailed, InterruptedException, AWTException {
    	List<Integer> pointList = new ArrayList<>();
        reviewDetails();
        MyRewardsPage myRewards = new MyRewardsPage(driver);
        myRewards.onPage("my-rewards");
        myRewards.pointActivityClick();
        Thread.sleep(30000);
        PointsActivityPage pointsActivity = new PointsActivityPage(driver);
        pointsActivity.onPage("points-activity");

        List<String> pointsList = pointsActivity.points();
        for (String point : pointsList) {
            if (point != null && !point.trim().isEmpty()) {
                // Parse the point value, considering the possibility of negative values
                pointList.add(Integer.parseInt(point.replaceAll("[^0-9-]", "")));
            }
        }
        
        for(int point:pointList ) {
        	System.out.println(point);
        }

        // Calculate the sum of all points except the last element
        int sumOfRedeemedPoints = pointList.subList(0, pointList.size() - 1).stream().mapToInt(Integer::intValue).sum();
        int remainingPointBalance = pointsActivity.remainingPointsBalance();
        int lastElement = pointList.get(pointList.size() - 1);

        Assert.assertEquals(Integer.parseInt(link.getTotalAmount().split("\\.")[0]), lastElement, "ERROR: Total Points don't match");
        Assert.assertEquals(remainingPointBalance, lastElement + sumOfRedeemedPoints, "ERROR: Remaining points don't match");
        pointsActivity.myRewardsClick();
        myRewards.onPage("my-rewards");
        MyRewardsPage.takeFullPageScreenshot(driver);
        return remainingPointBalance;
    }
    

    public void waitUntilOrderStatusChangesToFailed(MyRewardsPage myRewards) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120)); // Adjust the timeout as needed
        wait.until((ExpectedCondition<Boolean>) driver -> {
            myRewards.refreshPage();
            List<String> orderStatusList = myRewards.returnOrderStatus();
            for(String order_status:orderStatusList) {
            System.out.println("order_status");
            }
            return orderStatusList.get(0).equalsIgnoreCase("failed");
        });
    }
    
    public String getPointBalanceFromAPI() {
        Response res = link.getUserInfo(link.getUserId());
        JsonPath jsonPath = res.jsonPath();
        return jsonPath.getString("points_account.points_balance");
    }
    
    private void validateReviewDetails(InfoReviewDetailsPage checkout) {
        Assert.assertEquals(checkout.firstNameReview(), link.getFirstName(), "FirstName doesn't match");
        Assert.assertEquals(checkout.lastNameReview(), link.getLastName(), "LastName doesn't match");
        Assert.assertEquals(checkout.emailReview(), link.getEmail(), "Email doesn't match");
    }

    private void completeRedemption() throws IOException, InterruptedException, AWTException, FindFailed {
        InfoReviewPage reviewCheckout = new InfoReviewPage(driver);
        reviewCheckout.redeemnowBtnClick();
        RewardSummaryPage rewardSummary = new RewardSummaryPage(driver);
        rewardSummary.onPage();
        rewardSummary.clickViewRewardsBtn();
    }
    public int redeemRewardPointsForGiftCard(boolean isOverRanged) throws IOException, InterruptedException, AWTException, FindFailed {
    	searchAppleGiftCard();
        RedeemRewardsPage rrp = new RedeemRewardsPage(driver);
        rrp.onPage();

        String giftCardValueKey = isOverRanged ? "giftcardvalueoverranged" : "giftcardvalue";
        String value = rrp.selectGiftCardValue(prop.getProperty(giftCardValueKey));
        int gcquantity = rrp.selectGiftCardQuantityAndRedeem(prop.getProperty("giftcardqty"));
        int redeemable_points = Integer.parseInt(value.replaceAll("[^0-9]", "")) * gcquantity * 100;

        Assert.assertTrue(
                ((rrp.redeemablePoints.getText()).replaceAll("[^0-9]", "")).contains(String.valueOf(redeemable_points)),
                "ERROR: Redeemable points unmatched"
        );
        LoggerUtil.log ("INFO:Redeemable points Matched with Calculation");

        int existing_rewards_balance = Integer.parseInt(rrp.getExistingPointBalance().replaceAll("[^0-9]", ""));
        if (existing_rewards_balance >= redeemable_points) {
            rrp.clickRedeemNow();
        } else {
            rrp.insufficientBalance();
        }

        return redeemable_points;
    }
    
    private void performSearch(String keyword) throws IOException, InterruptedException, AWTException, FindFailed {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50)); // 50 seconds timeout
        RewardsHomePage rewards_hp_actions = new RewardsHomePage(driver);
        try {
            if (rewards_hp_actions.privacyPolicyDisplay()) {
                rewards_hp_actions.checkAcceptTerms();
                rewards_hp_actions.clickAcceptBtn();
                LoggerUtil.log("INFO: Clicked on Accept Terms and Conditions");
            }
        } catch (TimeoutException e) {
            // Log the exception if needed
        	LoggerUtil.log("Privacy policy elements not found within 30 seconds: " + e.getMessage());
        } catch (NoSuchElementException e) {
            // Log the exception if needed
        	LoggerUtil.log("Privacy policy elements not found: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
        	LoggerUtil.log("An unexpected error occurred: " + e.getMessage());
        }

        // Execution continues here even if an exception was caught
        rewards_hp_actions.hoverRedeemRewards();
        rewards_hp_actions.clickGiftCard();
        GiftCardPage gcp = new GiftCardPage(driver);
        gcp.onPage("gift-cards");
        gcp.enterSearchKeyword(keyword);
        if (keyword.equals(prop.getProperty("invalidKeyword"))) {
            gcp.noGiftCardResults();
        } else {
            gcp.clickGiftCard();
        }
    }



}
