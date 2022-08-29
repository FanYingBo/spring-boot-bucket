package com.study.alibaba.rocketmq.producer.mq;

import com.study.alibaba.rocketmq.producer.mq.listener.OrderTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class MQConfig {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private OrderTransactionListener produceTransactionListener;

    private Boolean init = Boolean.FALSE;

    public static final String ORDER_TRANS_GROUP = "order_trans_group";
    public static final String TOPIC = "order_trans";
    public static final String TAG = "state_trace";

    @PostConstruct
    public void init(){
        if(init){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        rocketMQTemplate.createAndStartTransactionMQProducer(ORDER_TRANS_GROUP, produceTransactionListener, executorService, null);
        init = Boolean.TRUE;
    }
}
