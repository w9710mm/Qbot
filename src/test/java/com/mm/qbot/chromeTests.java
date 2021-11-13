package com.mm.qbot;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot
 * @Description:
 * @date 2021/11/11 23:01
 */
public class chromeTests {

@Test
    public  void convertHtml2Image( ) {
    ChromeOptions chromeOptions=new ChromeOptions();
    System.setProperty("webdriver.chrome.driver","G:\\JavaProject\\Qbot\\chromedriver.exe");
    System.getProperty("user.dir");
    chromeOptions.addArguments("-headless");
    WebDriver driver = new ChromeDriver();
    driver.get("https://t.bilibili.com/591846012362841880?tab=2");
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    ((JavascriptExecutor)driver).executeScript("return document.querySelector('#internationalHeader').remove();");
    ((JavascriptExecutor)driver).executeScript("return document.querySelector('div.van-popover.van-popper').remove();");
    ((JavascriptExecutor)driver).executeScript("return document.querySelector('#app > div > div.detail-content > div > div > div > div.panel-area').remove();");

    WebElement element = driver.findElement(By.cssSelector("#app > div > div.detail-content > div"));
    File screenshotAs = element.getScreenshotAs(OutputType.FILE);
    try {
        BufferedImage tempImg = ImageIO.read(screenshotAs);
        System.out.println(1);
    } catch (IOException e) {
        e.printStackTrace();
    }


}
}
