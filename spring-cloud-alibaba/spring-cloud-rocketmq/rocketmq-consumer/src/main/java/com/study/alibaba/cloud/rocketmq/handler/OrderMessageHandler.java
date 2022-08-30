package com.study.alibaba.cloud.rocketmq.handler;

import com.study.alibaba.cloud.rocketmq.mode.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
public class OrderMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageHandler.class);

    public void afterOrderCompleted(Order order){
        logger.info("receive message : {}", order.getOrderNumber());
    }
}
