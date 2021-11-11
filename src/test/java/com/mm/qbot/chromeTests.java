package com.mm.qbot;

import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
    System.setProperty("webdriver.chrome.driver","G:\\JavaProject\\Qbot\\chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    driver.get("http://www.itest.info");

    String title = driver.getTitle();
    System.out.print(title);

    }
}
