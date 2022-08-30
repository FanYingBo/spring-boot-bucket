package com.study.alibaba.cloud.rocketmq.service;

import com.study.alibaba.cloud.rocketmq.mode.Order;

public interface IOrderService {

    Order completeOrderTrans(Order order);
}
