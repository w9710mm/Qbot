package com.mm.qbot.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.utils
 * @Description:
 * @date 2021/11/12 23:10
 */
public class ChromeUtils {

    private static ChromeOptions chromeOptions=new ChromeOptions();
    private  static WebDriver driver ;
    static {
        System.setProperty("webdriver.chrome.driver",String.format("%s\\chromedriver.exe",System.getProperty("user.dir")));
        chromeOptions.addArguments("-headless");

    }

    public static BufferedImage getDynamicImage(String dynamicId){
        driver = new ChromeDriver(chromeOptions);


        driver.get( String.format("https://t.bilibili.com/%s",dynamicId));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);



        ((JavascriptExecutor)driver).executeScript("return document.querySelector('#internationalHeader').remove();");
        ((JavascriptExecutor)driver).executeScript("return document.querySelector('div.van-popover.van-popper').remove();");
        ((JavascriptExecutor)driver).executeScript("return document.querySelector('#app > div > div.detail-content > div > div > div > div.panel-area').remove();");

        WebElement element = driver.findElement(By.cssSelector("#app > div > div.detail-content > div"));
        File screenshotAs = element.getScreenshotAs(OutputType.FILE);
        driver.close();
        try {
            return ImageIO.read(screenshotAs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
