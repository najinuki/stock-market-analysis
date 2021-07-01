package com.najinuki.batch.scheduler;

import com.najinuki.batch.domain.entity.AnalystReport;
import com.najinuki.batch.service.AnalystReportService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnalystReportCollectScheduler {

    private static Logger logger = LoggerFactory.getLogger(AnalystReportCollectScheduler.class);

    private final AnalystReportService analystReportService;

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Value("${pdf.download.path}")
    private String pdfDownloadPath;

    public AnalystReportCollectScheduler(AnalystReportService analystReportService) {
        this.analystReportService = analystReportService;
    }

    //@Scheduled(fixedDelay = 10000)
    public void start() {
        try {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            WebDriver driver = new ChromeDriver(options);

            LocalDate today = LocalDate.now();
            String url = "http://consensus.hankyung.com/apps.analysis/analysis.list?sdate=" + today + "&edate=" + today + "&now_page=1&report_type=CO&pagenum=300";
            driver.get(url);

            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

            List<WebElement> elementList = driver.findElements(By.cssSelector("div.table_style01 > table > tbody > tr"));
            System.out.println("size : " + elementList.size());

            for (int i = 0; i < elementList.size(); i++) {
                List<WebElement> tdList = elementList.get(i).findElements(By.cssSelector("td"));

                String writerDt = tdList.get(0).getText();
                String title = tdList.get(1).getText();
                String price = tdList.get(2).getText();
                String analystNm = tdList.get(4).getText();
                String securitiesFirm = tdList.get(5).getText();
                String code = getCode(title);

                AnalystReport analystReport = new AnalystReport();
                analystReport.setWriterDt(writerDt);
                analystReport.setTitle(title);

                if ("0".equals(price)) {
                    price = "Not Rated";
                }
                analystReport.setTargetPrice(price);
                analystReport.setAnalystNm(analystNm);
                analystReport.setSecuritiesFirmNm(securitiesFirm);
                analystReport.setCode(code);

                List<WebElement> urlElement = elementList.get(i).findElements(By.cssSelector("td > div.dv_input"));
                String pdfUrl = urlElement.get(urlElement.size()-1).getAttribute("innerHTML");
                analystReport.setPdfDownloadUrl("http://consensus.hankyung.com" + getUrl(pdfUrl));

                analystReportService.save(analystReport);

                //analystReportService.downloadFile("http://consensus.hankyung.com" + getUrl(innerHtml), pdfDownloadPath);
            }
            driver.close();
            driver.quit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getUrl(String tag) throws Exception {
        Pattern p = Pattern.compile("href=\"(.*?)\"");
        Matcher m = p.matcher(tag);
        String url = "";
        if (m.find()) {
            url = m.group(1);
        }

        return url;
    }

    private static String getCode(String title) throws Exception {
        Pattern p = Pattern.compile("\\([0-9]{6}\\)");
        Matcher m = p.matcher(title);
        String code = "";
        if (m.find()) {
            code = m.group().replace("(", "").replace(")", "");
        }

        return code;
    }

}
