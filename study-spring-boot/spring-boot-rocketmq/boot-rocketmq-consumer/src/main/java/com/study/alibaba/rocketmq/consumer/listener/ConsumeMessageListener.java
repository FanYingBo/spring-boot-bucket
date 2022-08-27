package com.study.alibaba.rocketmq.consumer.listener;

import com.study.alibaba.rocketmq.consumer.handler.MQWebSocketMessageTransfer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RocketMQMessageListener(consumerGroup = "group_consumer", topic = "localTest",selectorExpression="TagA")
public class ConsumeMessageListener implements RocketMQListener<String> {

    private Logger logger = LoggerFactory.getLogger(ConsumeMessageListener.class);
    @Autowired
    private MQWebSocketMessageTransfer mqWebSocketMessageTransfer;

    @Override
    public void onMessage(String o) {
        logger.info("received message: "+ o);
        mqWebSocketMessageTransfer.putMessage(o);
    }
}
