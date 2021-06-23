package com.najinuki.batch.service;

import com.najinuki.batch.domain.entity.AnalystReport;
import com.najinuki.batch.domain.repository.AnalystReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalystReportService {

    @Autowired
    private AnalystReportRepository analystReportRepository;

    public AnalystReport save(AnalystReport analystReport) {
        return analystReportRepository.save(analystReport);
    }

}
