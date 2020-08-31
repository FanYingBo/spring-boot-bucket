package com.study.self.spring.cloud.netflix.eureka.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaSecondNodeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaSecondNodeServerApplication.class);
    }
}
