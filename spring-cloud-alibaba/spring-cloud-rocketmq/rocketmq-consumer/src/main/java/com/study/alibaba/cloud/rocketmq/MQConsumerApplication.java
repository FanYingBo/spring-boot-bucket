package com.study.alibaba.cloud.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 不可用，对于spring cloud stream , rocketmq 的tag无法设置
 */
@SpringBootApplication
public class MQConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MQConsumerApplication.class);
    }
}
