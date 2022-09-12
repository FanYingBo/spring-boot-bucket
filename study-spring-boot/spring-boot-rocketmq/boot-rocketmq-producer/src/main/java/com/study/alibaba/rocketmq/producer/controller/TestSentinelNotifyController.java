package com.study.alibaba.rocketmq.producer.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestSentinelNotifyController {

    private static Logger logger = LoggerFactory.getLogger(TestSentinelNotifyController.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private AtomicInteger requestCount = new AtomicInteger(0);

    @GetMapping("/sayHello/v1")
    public String sendMsg(String name, Boolean async){
        int get = requestCount.addAndGet(1);
        logger.info("request count !!! {} ", get);
        String message = "hello " + name;
        if(async){
            rocketMQTemplate.asyncSend("localTest:TagB", MessageBuilder.withPayload(message).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.info("On Sent Success Event !!!");
                }

                @Override
                public void onException(Throwable throwable) {
                    logger.error("On Sent Failure Event !!!");
                }
            });
        }else{
            rocketMQTemplate.send("localTest:TagA", MessageBuilder.withPayload(message).build());
            int co = atomicInteger.addAndGet(1);
            logger.info("Sent Success !!! {} {}", message, co);
        }

        return message;
    }
}
