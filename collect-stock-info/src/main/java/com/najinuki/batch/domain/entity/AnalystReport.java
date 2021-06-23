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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reportId;

    @Column(nullable = false, unique = true, length = 11)
    private String securitiesFirmCode;

    @Column(nullable = false, unique = true)
    private Integer analystId;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false, length = 11)
    private String title;

    @Column(nullable = false, length = 11)
    private String targetPrice;

    @Column(nullable = false, length = 11)
    private String writerDt;

    @Column
    private String delYn;

    @Column
    private String regDt;

    @Column
    private String regId;

    @Column
    private String modDt;

    @Column
    private String modId;

}
