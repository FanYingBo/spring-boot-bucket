package com.study.alibaba.rocketmq.producer.service.impl;

import com.study.alibaba.rocketmq.producer.edm.OrderState;
import com.study.alibaba.rocketmq.producer.management.OrderManagement;
import com.study.alibaba.rocketmq.producer.mode.Order;
import com.study.alibaba.rocketmq.producer.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderManagement orderStateManagement;

    @Override
    public Order initOrder(Long skuId, int number) {
        Order order = new Order();
        order.setSkuId(1l);
        order.setOrderNumber(System.currentTimeMillis()+"");
        order.setOrderState(OrderState.INIT);
        order.setTransId(order.getOrderNumber());
        orderStateManagement.storeOrder(order, order.getTransId());
        return order;
    }

    @Override
    public OrderState doOrderTransaction(String orderNumber) {
        Random random = new Random();
        int nextInt = random.nextInt(10);
        logger.info("begin doOrderTransaction orderNumber: {}", orderNumber);
        if(nextInt <= 5){
            // 订单事务处理成功
            logger.info("doOrderTransaction success , orderNumber: {}", orderNumber);
            orderStateManagement.changeOrderStateTo(orderNumber, OrderState.SUCCESS);
            return OrderState.SUCCESS;
        }
        // 订单事务处理异常
        logger.error("doOrderTransaction error , orderNumber: {}", orderNumber);
        orderStateManagement.changeOrderStateTo(orderNumber, OrderState.FAILED);
        return OrderState.FAILED;
    }
}
