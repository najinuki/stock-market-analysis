package com.najinuki.batch.scheduler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class StockYoutubeCollectScheduler {

    private static Logger logger = LoggerFactory.getLogger(StockYoutubeCollectScheduler.class);

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    public void start() {
        try {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
