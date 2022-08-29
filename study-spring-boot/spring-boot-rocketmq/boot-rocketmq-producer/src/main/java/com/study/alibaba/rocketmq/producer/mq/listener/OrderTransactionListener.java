package com.study.alibaba.rocketmq.producer.mq.listener;

import com.study.alibaba.rocketmq.producer.edm.OrderState;
import com.study.alibaba.rocketmq.producer.management.OrderManagement;
import com.study.alibaba.rocketmq.producer.mode.Order;
import com.study.alibaba.rocketmq.producer.service.OrderService;
import com.study.alibaba.rocketmq.producer.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderTransactionListener implements RocketMQLocalTransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderTransactionListener.class);
    @Autowired
    private OrderManagement orderStateManagement;
    @Autowired
    private OrderService orderService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        logger.info("on executeLocalTransaction transId: {}",
                message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        // 执行本地事务
        Order order = null;
        try {
            order = JsonUtils.readJsonFromString(new String((byte[])message.getPayload()), Order.class);
        } catch (IOException e) {
            logger.error("on executeLocalTransaction error transId: {}",
                    message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        // 执行订单的事务，订单成功后，才能提交消息
        orderService.doOrderTransaction(order.getOrderNumber());
        logger.info("on executeLocalTransaction orderNumber: {} orderState {}", order.getOrderNumber(), order.getOrderState());
        RocketMQLocalTransactionState transState = getTransState(message);
        logger.info("on executeLocalTransaction orderNumber: {} orderState {} transStat {}",
                order.getOrderNumber(), order.getOrderState(), transState);
        return transState;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        logger.info("on checkLocalTransaction transId: {}",
                message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        RocketMQLocalTransactionState state = getTransState(message);
        Order order = (Order)message.getPayload();
        logger.info("on checkLocalTransaction orderNumber: {} orderState {} transStat {}",
                order.getOrderNumber(), order.getOrderState(), state);
        return state ;
    }

    private RocketMQLocalTransactionState getTransState(Message message){
        String transId = message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID).toString();
        if(StringUtils.isNotBlank(transId)){
            if(!orderStateManagement.exists(transId)){
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            OrderState orderState = orderStateManagement.getOrderState(transId);
            if(orderState.equals(OrderState.SUCCESS)){
                return RocketMQLocalTransactionState.COMMIT;
            }
            if(orderState.equals(OrderState.FAILED)){
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
