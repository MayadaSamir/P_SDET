package sdet.pixelogic.selenium.test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.io.ByteArrayInputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
//import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class RegisterProcessTest {

	ChromeDriver driver;
	
	//Generating fake registration data
	Faker fakeData = new Faker();
	String firstname = fakeData.name().firstName();
	String lastname = fakeData.name().lastName();
	String phone = fakeData.phoneNumber().cellPhone();
	String email = fakeData.internet().emailAddress();
	
	//Generating password of 8 characters including upper case letters
	String password = fakeData.lorem().characters(8, true);
	
	//Instantiate wait;
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	
	String successLoginURL;
	String successLogoutURL;
    
	@BeforeTest
	public void openURL()
	{ 
		//Add Chrome Driver executable file in the system properties
		String ChromePath = System.getProperty("user.dir")+"\\resources\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver",ChromePath);

		//Take new object from Chrome Driver
		driver = new ChromeDriver();

		//Open URL from WebDriver
		driver.navigate().to("https://www.phptravels.net/register");
	}

	@Test(priority = 0)
	@Description("Sign Up Test Case")
	@Severity(SeverityLevel.CRITICAL)
	public void SignUp() 
	{
		WebElement firstnameTxt    = driver.findElement(By.name("firstname"));
		WebElement lastnameTxt     = driver.findElement(By.name("lastname"));
		WebElement phoneTxt        = driver.findElement(By.name("phone"));
		WebElement emailTxt        = driver.findElement(By.name("email"));
		WebElement passwordTxt     = driver.findElement(By.name("password"));
		WebElement cfmpasswordTxt  = driver.findElement(By.name("confirmpassword"));

		firstnameTxt.clear();
		firstnameTxt.sendKeys(firstname);

		lastnameTxt.clear();
		lastnameTxt.sendKeys(lastname);

		phoneTxt.clear();
		phoneTxt.sendKeys(phone);

		emailTxt.clear();
		emailTxt.sendKeys(email);

		passwordTxt.clear();
		passwordTxt.sendKeys(password);

		cfmpasswordTxt.clear();
		cfmpasswordTxt.sendKeys(password);

		cfmpasswordTxt.submit();
		
		//Add wait time to handle low speed internet connection
		wait.until(ExpectedConditions.titleContains("My Account"));
				
		//Validate that it has logged in successfully
		successLoginURL = driver.getCurrentUrl();
		Assert.assertEquals(successLoginURL,"https://www.phptravels.net/account/");
		
		//Add screenshot to Allure report
		AllureScreenshot(driver,"SignUp Screenshot");
	}
	
	@Test(priority = 1)
	@Description("Logout Test Case")
	@Severity(SeverityLevel.CRITICAL)
	public void Logout()
	{
		WebElement account = driver.findElement(By.cssSelector(".bx.bx-user"));
		account.click();
		
		WebElement logout  = driver.findElement(By.xpath("//*[@id=\"//header-waypoint-sticky\"]/div[1]/div/div/div[2]/div/ul/li[3]/div/div/div/a[2]"));
		logout.click();
		
		//Validate that it has logged in successfully
		successLogoutURL = driver.getCurrentUrl();
		Assert.assertEquals(successLogoutURL,"https://www.phptravels.net/login");
		
		//Add screenshot to Allure report
		AllureScreenshot(driver,"Logout Screenshot");
	}
	
	@Test(priority = 2)
	@Description("Login Test Case")
	@Severity(SeverityLevel.CRITICAL)
	public void Login()
	{
		wait.until(ExpectedConditions.titleContains("Login"));
		
		WebElement emailTxt        = driver.findElement(By.name("username"));
		WebElement passwordTxt     = driver.findElement(By.name("password"));
		
		emailTxt.clear();
		emailTxt.sendKeys(email);

		passwordTxt.clear();
		passwordTxt.sendKeys(password);
		
		//Hitting enter to Login
		passwordTxt.sendKeys(Keys.RETURN);
		
		//Add wait time to handle low speed internet connection
		wait.until(ExpectedConditions.titleContains("My Account"));
		
		//Validate that it has logged in successfully
		successLoginURL = driver.getCurrentUrl();
		Assert.assertEquals(successLoginURL,"https://www.phptravels.net/account/");
		
		//Add screenshot to Allure report
		AllureScreenshot(driver,"Login Screenshot");
	}
	
	//Add screenshot of failures to Allure and ScreenshotsOfFailures folder
	@AfterMethod
	public void RegisterLoginFailScreenshot(ITestResult result) throws IOException
	{
		if(ITestResult.FAILURE == result.getStatus())
		{
			//Create reference of TakeScreenshots
			TakesScreenshot ts = (TakesScreenshot)driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(source, new File("./ScreenshotsOfFailures/"+ result.getName() + ".png"));
		    //Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
			AllureScreenshot(driver,"Failure Screenshot of " + result.getName());
			driver.quit();
		}
	}
	
	//Close ChromeDriver
	@AfterTest
	public void closeDriver() 
	{
		driver.quit();
	}	
	
	//Add screenshots to Allure report
	public void AllureScreenshot(WebDriver driver, String ScreenshotTitle)
	{
		Allure.addAttachment(ScreenshotTitle, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
	}
	
	
	//Not sure of it
//	public void SignUpHTTPInterceptor() 
//	{
//	    Proxy proxyServer = new BrowserMobProxyServer();
//	    proxyServer.start();
//
//	    // capture content as a HAR (HTTP Archive)
//	    // to process when test is complete
//	    proxyServer.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT,
//	                                      CaptureType.RESPONSE_CONTENT);
//	    proxyServer.newHar();
//	    
//	    final Proxy proxyConfig = ClientUtil.createSeleniumProxy(proxyServer);
//	    
//	    String proxyDetails = "127.0.0.1:8080";
//	    final Proxy proxyConfig = new Proxy().
//	                                setHttpProxy(proxyDetails).
//	                                setSslProxy(proxyDetails);
//	    
//	    final ChromeOptions options = new ChromeOptions();
//	    options.setProxy(proxyConfig);
//	    options.setAcceptInsecureCerts(true);
//	    driver = new ChromeDriver(options);
//	    
//	    final Har httpMessages = proxyServer.getHar();
//	    for(HarEntry httpMessage : httpMessages.getLog().getEntries()){
//
//	        // check no errors on the XHR requests
//	        if(httpMessage.getRequest().getUrl().contains("/messageset")) {
//	            Assertions.assertEquals(200, httpMessage.getResponse().getStatus());
//	        }
//	    }
//	    proxyServer.abort();
//    }
}
