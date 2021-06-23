package com.najinuki.batch.domain.repository;

import com.najinuki.batch.domain.entity.AnalystReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalystReportRepository extends JpaRepository<AnalystReport, Long> {

    AnalystReport save(AnalystReport analystReport);

}
