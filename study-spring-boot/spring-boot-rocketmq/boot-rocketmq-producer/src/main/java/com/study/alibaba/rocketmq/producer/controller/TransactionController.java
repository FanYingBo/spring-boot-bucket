package com.study.alibaba.rocketmq.producer.controller;

import com.study.alibaba.rocketmq.producer.dto.CreateOrderRequest;
import com.study.alibaba.rocketmq.producer.mq.MQConfig;
import com.study.alibaba.rocketmq.producer.mq.listener.OrderTransactionListener;
import com.study.alibaba.rocketmq.producer.mode.Order;
import com.study.alibaba.rocketmq.producer.service.OrderService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private OrderTransactionListener produceTransactionListener;
    @Autowired
    private OrderService orderService;

    @PostMapping("/order/create")
    public Order createOrder(@RequestBody CreateOrderRequest createOrderRequest){
        Order order = orderService.initOrder(createOrderRequest.getSkuId(), createOrderRequest.getNumber());

        Message<Order> orderMessage = MessageBuilder.withPayload(order)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, order.getTransId()).build();// transaction id 需要设置

        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(MQConfig.ORDER_TRANS_GROUP, MQConfig.TOPIC + ":" + MQConfig.TAG, orderMessage, null);
        LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();
        logger.info("send mq init message state: " + localTransactionState.name());
        return order;
    }
}
