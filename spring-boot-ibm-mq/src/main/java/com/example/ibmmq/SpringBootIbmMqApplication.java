package com.example.ibmmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot Application Class for IBM MQ Integration
 * 
 * This application demonstrates a complete end-to-end implementation
 * of Spring Boot with IBM MQ integration running on Docker.
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.example.ibmmq")
public class SpringBootIbmMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIbmMqApplication.class, args);
    }
}
