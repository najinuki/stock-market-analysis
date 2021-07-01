package com.najinuki.batch.scheduler;

import com.najinuki.batch.domain.entity.AnalystReport;
import com.najinuki.batch.domain.entity.StockItem;
import com.najinuki.batch.service.AnalystReportService;
import com.najinuki.batch.service.StockItemService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DailyStockPriceCollectScheduler {

    private static Logger logger = LoggerFactory.getLogger(DailyStockPriceCollectScheduler.class);

    private final StockItemService stockItemService;

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    public DailyStockPriceCollectScheduler(StockItemService stockItemService) {
        this.stockItemService = stockItemService;
    }

    @Scheduled(fixedDelay = 10000)
    public void start() {
        try {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            WebDriver driver = new ChromeDriver(options);

            List<StockItem> stockItemList = stockItemService.getStockItemList();

            for (StockItem stockItem : stockItemList) {
                String code = stockItem.getCode();

                String url = "https://finance.daum.net/quotes/A" + code + "#current/quote";
                driver.get(url);
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

                List<WebElement> elementList = driver.findElement(By.id("boxDayHistory")).findElements(By.cssSelector("div.box_contents > div > table > tbody > tr.first > td"));
                String date = elementList.get(0).getText(); // 일자
                String open = elementList.get(1).getText(); // 시가
                String high = elementList.get(2).getText(); // 고가
                String low = elementList.get(3).getText(); // 저가
                String close = elementList.get(4).getText(); // 종가
                String diff = elementList.get(5).getText(); // 전일비
                String fluctuations = elementList.get(6).getText(); // 등락률
                String volume = elementList.get(7).getText(); // 거래량

                System.out.println("################## " + date + ", " + open + ", " + high + ", " + low + ", " + close
                + ", " + diff + ", " + fluctuations + ", " + volume);

                // Apache Kafka로 전송 로직 추가

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
        String url = "";
        if (m.find()) {
            url = m.group(1); // this variable should contain the link URL
        }

        return url;
    }

    private static String getCode(String title) {
        String regex = "\\([0-9]{6}\\)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(title);

        String code = "";
        while (m.matches()) {
            code = m.group();
        }

        return code;
    }

}
