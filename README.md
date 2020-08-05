# P_SDET

Selenium framework project to automate Registeration process and verify successful login.

Tech stack: Selenium, Java, Maven, TestNG, Faker, DDT and Allure Reports.

IDE used: Eclipse

Limitations: this project is developed to run on Google Chrome.

The project has 3 automated Test Cases besides BeforeTest, AfterTest, AfterMethod and AllureScreenshot method:
BeforeTest
  {
    a.Initiate Chrome Driver
    b.Open URL "https://www.phptravels.net/register"
  }

1.Register Test Case
  { 
    a.Find form elements
    b.Enter valid fake data
    c.Click Signup
    d.Wait for page loading
    e.Assert successful Sign up
    f.Add screenshot to Allure report
  }
  
2.Logout Test Case
  {
    a.Find and click on Account menu
    b.Find and click on Logout
    c.Assert successful Logout
    d.Add screenshot to Allure report
  }

3.Login Test Case (verifies successful login)
  {
    a.Wait until Login page loads
    b.Find username and password fields
    c.Enter valid fake data to username and password
    d.Click Enter to Login
    e.Assert successful login
    f.Add screenshot to Allure report
  }
 
AfterMethod (RegisterLoginFailScreenshot)
  {
    a.If any of the tests failed
      1.Take screenshot and save it to ScreenshotsOfFailures folder
      2.Add screenshot to Allure report
  }
  
AllureScreenshot
  {
    a.Add screenshot to Allure report
  }
  
AfterTest
  {
    a.Close Chrome Driver
  }
  
Allure report is customized with screenshots of failures can be accessed by the command 'allure.bat serve' from cmd inside the project.
