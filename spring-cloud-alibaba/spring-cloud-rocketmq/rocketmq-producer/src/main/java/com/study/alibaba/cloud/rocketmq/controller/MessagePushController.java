package com.study.alibaba.cloud.rocketmq.controller;

import com.study.alibaba.cloud.rocketmq.mode.Order;
import com.study.alibaba.cloud.rocketmq.provider.IMessageServiceProvider;
import com.study.alibaba.cloud.rocketmq.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MessagePushController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/order/completed")
    public Order orderCompleted(@RequestBody Order order){
        return orderService.completeOrderTrans(order);
    }

}
