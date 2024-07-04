package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import context.WebDriverContext;
import listners.LogListener;
import listners.ReportListener;
import utils.*;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
@Listeners({ ReportListener.class, LogListener.class})
public class BaseTest {
    public static Properties prop;
    protected WebDriver driver;

    @BeforeTest
    public static void intitconfig() throws IOException {
        prop = new Properties();
        FileInputStream file = new FileInputStream("./src/test/resources/config.properties");
        prop.load(file);
    }

  

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        LoggerUtil.log("************************** Test Execution Started ************************************");
        TestProperties.loadAllPropertie();
    }

    @AfterSuite(alwaysRun = true)
    public void wrapAllUp(ITestContext context) {
        int total = context.getAllTestMethods().length;
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        LoggerUtil.log("Total number of testcases : " + total);
        LoggerUtil.log("Number of testcases Passed : " + passed);
        LoggerUtil.log("Number of testcases Failed : " + failed);
        LoggerUtil.log("Number of testcases Skipped  : " + skipped);
        boolean mailSent = MailUtil.sendMail(total, passed, failed, skipped);
        LoggerUtil.log("Mail sent : " + mailSent);
        LoggerUtil.log("************************** Test Execution Finished ************************************");
    }

    @BeforeClass
    protected void setup() {
    	//System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    	//WebDriverManager.chromedriver().clearDriverCache().setup();
        //WebDriverManager.chromedriver().setup();
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--remote-allow-origins=*", "disable-infobars");
        driver = new ChromeDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverContext.setDriver(driver);
        
        
    }

    @AfterClass
    public void wrapUp() {
        if (driver != null) {
            driver.quit();
        }
    }
}
