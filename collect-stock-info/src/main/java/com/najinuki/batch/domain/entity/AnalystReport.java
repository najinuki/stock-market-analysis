package com.najinuki.batch.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "analyst_report")
@Getter
@Setter
public class AnalystReport implements Serializable {

    private static final long serialVersionUID = 1795630694469420960L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId; // 리포트 ID

    @Column(nullable = false)
    private String securitiesFirmNm; // 증권사명

    @Column(nullable = false)
    private String analystNm; // 애널리스트명

    @Column(nullable = false, length = 6)
    private String code; // 종목코드

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String targetPrice; // 목표가격

    @Column(nullable = false)
    private String writerDt; // 작성일

    @Column(nullable = false)
    private String pdfDownloadUrl; // PDF 다운로드 주소

    @Column
    private String delYn; // 삭제여부

    @Column
    private String regDt; // 등록일

    @Column
    private String regId; // 등록자

    @Column
    private String modDt; // 수정일

    @Column
    private String modId; // 수정자

}
