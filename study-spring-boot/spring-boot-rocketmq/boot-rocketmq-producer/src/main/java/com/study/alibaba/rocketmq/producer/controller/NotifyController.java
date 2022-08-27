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

@RestController
public class NotifyController {

    private static Logger logger = LoggerFactory.getLogger(NotifyController.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/sayHello")
    public String sendMsg(String name, Boolean async){
        String message = "hello " + name;
        if(async){
            rocketMQTemplate.asyncSend("localTest:TagA", MessageBuilder.withPayload(message).build(), new SendCallback() {
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
            logger.info("Sent Success !!! {}", message);
        }
        return message;
    }
}
