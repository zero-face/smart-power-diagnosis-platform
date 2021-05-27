package com.zero;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//@EnableAsync
@SpringBootApplication
@MapperScan("com.zero.smart_power_diagnosis_platform.mapper")
public class SmartPowerDiagnosisPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartPowerDiagnosisPlatformApplication.class, args);
    }

}
