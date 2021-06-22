package com.najinuki.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CollectStockInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectStockInfoApplication.class, args);
    }
}
