package com.najinuki.batch.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stock_item")
@Getter
@Setter
public class StockItem implements Serializable {

    private static final long serialVersionUID = 9114652343503073028L;

    @Id
    private String code; // 종목코드

    @Column(nullable = false)
    private String stockSymbol; // 종목명

    @Column(nullable = false)
    private String stockListingDate; // 상장일

    @Column(nullable = false)
    private String market_classification; // 시장구분

    @Column(nullable = false)
    private String stockType; // 주식종류

    @Column(nullable = false)
    private Integer par; // 액면가

    @Column(nullable = false)
    private Integer stockCnt; // 상장주식수

    @Column
    private String regDt; // 등록일

    @Column
    private String regId; // 등록자

    @Column
    private String modDt; // 수정일

    @Column
    private String modId; // 수정자

}
