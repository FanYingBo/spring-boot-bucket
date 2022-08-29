package com.study.alibaba.rocketmq.producer.service;

import com.study.alibaba.rocketmq.producer.edm.OrderState;
import com.study.alibaba.rocketmq.producer.mode.Order;

public interface OrderService {

    Order initOrder(Long skuId, int number);

    OrderState doOrderTransaction(String orderNumber);
}
