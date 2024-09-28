package com.rw.apps.xchange.ratechecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XchangeRateCheckerApplication {

    public static void main(String[] args) {
        SpringApplication.run(XchangeRateCheckerApplication.class, args);
    }
}
