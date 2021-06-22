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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnalystReportCollectScheduler {

    private static Logger logger = LoggerFactory.getLogger(AnalystReportCollectScheduler.class);

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Scheduled(fixedDelay = 10000)
    public void start() {
        try {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            WebDriver driver = new ChromeDriver(options);

            LocalDate today = LocalDate.now();
            System.out.println(">>> today : " + today);

            //이동을 원하는 url
            String url = "http://consensus.hankyung.com/apps.analysis/analysis.list?sdate=" + today + "&edate=" + today + "&now_page=1&report_type=CO&pagenum=300";
            driver.get(url);

            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            List<WebElement> el1 = driver.findElements(By.cssSelector("div.table_style01 > table > tbody > tr"));
            System.out.println("size : " + el1.size());

            for (int i = 0; i < el1.size(); i++) {
                List<WebElement> el2 = el1.get(i).findElements(By.cssSelector("td > div.dv_input"));
                String innerHtml = el2.get(el2.size()-1).getAttribute("innerHTML");
                System.out.println(">>>> " + getUrl(innerHtml));
            }

            driver.close();
            driver.quit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getUrl(String tag) {
        Pattern p = Pattern.compile("href=\"(.*?)\"");
        Matcher m = p.matcher(tag);
        String url = null;
        if (m.find()) {
            url = m.group(1); // this variable should contain the link URL
        }

        return url;
    }

}
