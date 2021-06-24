package com.najinuki.batch.service;

import com.najinuki.batch.domain.entity.AnalystReport;
import com.najinuki.batch.domain.repository.AnalystReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class AnalystReportService {

    @Autowired
    private AnalystReportRepository analystReportRepository;

    public AnalystReport save(AnalystReport analystReport) {
        return analystReportRepository.save(analystReport);
    }

    public void downloadFile(String url, String downloadPath) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        File file = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
            File ret = File.createTempFile("download", ".tmp");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });

        FileCopyUtils.copy(file, new File(downloadPath + "\\" + System.currentTimeMillis() + ".pdf"));
    }

}
