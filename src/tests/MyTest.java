package tests;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
1. open url: wrike.com
2.click "Login" button
3.click "sign Up"
4.fill it up email address(random generated)
5.click "Get started for free" button
6.check at the loaded page, that you have success confirmation of registration
7.click resend button
8.open "Pricing" link
9.click "Get started for free" for professional plan
10.In appeared window fill it up another random generated email
11.click "Create my Wrike account" button
12.check at the loaded page, that you have success confirmation of registration
13.click resend button
*/
public class MyTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    @BeforeClass
    public static void openBrowser() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\kvakin\\Downloads\\chromedriver_win32\\chromedriver.exe");
       // driver = (ChromeDriver) new RemoteWebDriver(new URL("http://localhost:9000/"), DesiredCapabilities.chrome());
        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,20);
    }

    @org.junit.Test
    public void Test() throws InterruptedException {

        System.out.println("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
        WebElement element;
        driver.manage().window().maximize();

        driver.get("https://www.wrike.com/");

        Thread.sleep(1000);
        /////////////////////////////////
        Assert.assertEquals("https://www.wrike.com/",driver.getCurrentUrl()); //почему-то заходил на "https://www.wrike.com/wa"
        //login button
        element = driver.findElement(By.xpath("/html/body/header/div/nav/a"));//.click();
        // element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/header/div[2]/div[2]/div[1]/div/div[2]/div/form/a"));//кнопка login с "https://www.wrike.com/wa"
        element.click();

        //create acc
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"nativeLoginForm\"]/div[3]/a")));
        driver.findElement(By.xpath("//*[@id=\"nativeLoginForm\"]/div[3]/a")).click();

        //email textbox at https://www.wrike.com/free-trial
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        element =driver.findElement(By.name("email"));

        //generating email
        String email = emailGen();
        element.sendKeys(email);

        //Click get started for free
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/main/div[1]/div/form/div/span/button")));
        driver.findElement(By.xpath("/html/body/main/div[1]/div/form/div/span/button")).click();


        //check if text is ok
        checkRegistration(email);
        //then resend
        driver.findElement(By.id("resendEmail")).click();

        //open pricing link
        driver.findElement(By.xpath("/html/body/header/div/nav/ul/li[4]/a")).click();
        //professional plan
        By plan = By.xpath("/html/body/main/section/div/div[2]/div[2]/div[1]/div[3]/button");
        wait.until(ExpectedConditions.presenceOfElementLocated(plan));
        driver.findElement(plan).click();


        //appeared window
        By appearedEmail = By.xpath("/html/body/div[3]/div/form/div[1]/input");
        wait.until(ExpectedConditions.presenceOfElementLocated(appearedEmail));
        element = driver.findElement(appearedEmail);
        //generating email
        email = emailGen();
        element.sendKeys(email);
        //click create wrike acc
        element.submit();


        /*By button = By.xpath("/*//*[@id=\"register_button_outer\"]/input");
        wait.until(ExpectedConditions.presenceOfElementLocated(button));//By.name("start-project")));
        Thread.sleep(1000);
        element =driver.findElement(button);//.click();//By.name("start-project")).click();*/


        //check if text is ok
        checkRegistration(email);
        //then resend
        driver.findElement(By.id("resendEmail")).click();

        System.out.println("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());

    }
    private void checkRegistration(String email)
    {
        //confirmation text
        String expectedText = "We’ve sent you an activation email\nat "+email;

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/main/section/div/h2")));
        //read text
        String text = driver.findElement(By.xpath("/html/body/main/section/div/h2")).getText();
        System.out.println("Check: "+expectedText.equals(text));
        //check it
        Assert.assertEquals(expectedText,text);

    }

    @AfterClass
    public static void closeBrowser(){
        driver.quit();
    }

    static private String emailGen()
    {
        Random random = new Random();
        String login="",sld="";
        String[] tld = {".com",".ru",".net"};
        char[] dict = new char[3];//from ascii 0 - A, 1 - a, 2 - numbers
        for(int i = 0; i<1+random.nextInt(10);i++)
        {
            dict[0] = (char)(65+ random.nextInt(25));//A-Z
            dict[1] = (char)(97+ random.nextInt(25));//a-z
            dict[2] = (char)(48+ random.nextInt(9));//0-9

            login+=dict[random.nextInt(3)];
            sld+=dict[random.nextInt(3)];
        }
        return login+"@"+sld+tld[random.nextInt(3)];
    }

}
