package com.study.alibaba.rocketmq.producer.management;

import com.study.alibaba.rocketmq.producer.edm.OrderState;
import com.study.alibaba.rocketmq.producer.mode.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderManagement {

    private Map<String, Order> orderMap = new ConcurrentHashMap<>();

    private Map<String, OrderState> mqTransOrderStatMap = new ConcurrentHashMap<>();

    private Map<String,String> orderIdMqTransMap = new ConcurrentHashMap<>();

    public boolean changeOrderStateTo(String orderNumber, OrderState toOrderState){
        if(orderMap.containsKey(orderNumber)){
            Order order = orderMap.get(orderNumber);
            order.setOrderState(toOrderState);
            if(orderIdMqTransMap.containsKey(orderNumber)){
                String transId = orderIdMqTransMap.get(orderNumber);
                if(mqTransOrderStatMap.containsKey(transId)){
                    mqTransOrderStatMap.replace(transId,toOrderState);
                    return true;
                }
            }
        }
        return false;
    }

    public OrderState getOrderState(String transId){
        return mqTransOrderStatMap.get(transId);
    }

    public boolean exists(String transId){
        return mqTransOrderStatMap.containsKey(transId);
    }

    public Order storeOrder(Order order, String transId){
        mqTransOrderStatMap.putIfAbsent(transId,order.getOrderState());
        orderIdMqTransMap.putIfAbsent(order.getOrderNumber(),transId);
        orderMap.put(order.getOrderNumber(), order);
        return order;
    }
}
