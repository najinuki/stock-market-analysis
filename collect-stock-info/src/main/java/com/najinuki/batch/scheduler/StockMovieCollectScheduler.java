package com.najinuki.batch.scheduler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class StockMovieCollectScheduler {

    private static Logger logger = LoggerFactory.getLogger(StockMovieCollectScheduler.class);

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Value("${video.download.path}")
    private String videoDownloadPath;

    @Scheduled(fixedDelay = 10000)
    public void start() {
        try {
            String url = getVideoDownloadUrl();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.execute(URI.create(url), HttpMethod.GET, requestCallback(), responseExtractor());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestCallback requestCallback() {
        return request -> {
            request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            formHttpMessageConverter.setCharset(Charset.forName("UTF-8"));
        };
    }

    private ResponseExtractor responseExtractor() {
        return response -> {
            Path path = Paths.get(videoDownloadPath + "\\" + System.currentTimeMillis() + ".mp4");
            Files.copy(response.getBody(), path);
            // Kafka로 전송
            return null;
        };
    }

    private String getVideoDownloadUrl() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);

        String url = "https://www.mtn.co.kr/program/watch.mtn?program=537&vod=&tab=vod";
        driver.get(url);

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.findElement(By.id("callBox")).click(); // 동영상 실행

        WebDriverWait wait = new WebDriverWait(driver,100);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#flashPlayer > iframe")));

        driver.switchTo().frame(element);
        WebElement element2 = driver.findElement(By.cssSelector("html > body > iframe"));
        driver.switchTo().frame(element2);

        WebElement element3 = driver.findElement(By.cssSelector("div#player_div > video > source"));
        System.out.println("Video URL : " + element3.getAttribute("src"));

        return element3.getAttribute("src");
    }

}
