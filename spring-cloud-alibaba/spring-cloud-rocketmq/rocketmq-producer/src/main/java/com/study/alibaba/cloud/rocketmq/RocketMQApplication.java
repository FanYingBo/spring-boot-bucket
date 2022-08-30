package com.study.alibaba.cloud.rocketmq;

import com.study.alibaba.cloud.rocketmq.provider.IMessageServiceProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * 不可用
 */
@SpringBootApplication
@EnableBinding(value = {IMessageServiceProvider.class})
public class RocketMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQApplication.class);
    }
}
