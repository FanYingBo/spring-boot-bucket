package com.study.alibaba.cloud.rocketmq.service.impl;

import com.study.alibaba.cloud.rocketmq.mode.Order;
import com.study.alibaba.cloud.rocketmq.provider.IMessageServiceProvider;
import com.study.alibaba.cloud.rocketmq.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private MessageChannel destination;

    @Override
    public Order completeOrderTrans(Order order) {
        order.setOrderNumber(System.currentTimeMillis()+"");
        order.setTransId(order.getOrderNumber());
        logger.info("send order completed message :{}", order.getOrderNumber());
        destination.send(MessageBuilder.withPayload(order).build());
        logger.info("send order completed message :{} success", order.getOrderNumber());
        return order;
    }
}
