package com.study.self.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class JPAApplication {

    public static void main(String[] args) {
        SpringApplication.run(JPAApplication.class,args);
    }
}
