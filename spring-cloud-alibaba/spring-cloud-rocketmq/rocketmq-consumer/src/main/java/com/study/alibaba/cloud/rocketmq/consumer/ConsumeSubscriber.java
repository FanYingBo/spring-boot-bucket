package com.study.alibaba.cloud.rocketmq.consumer;

import com.alibaba.cloud.stream.binder.rocketmq.properties.RocketMQConsumerProperties;
import com.study.alibaba.cloud.rocketmq.handler.OrderMessageHandler;
import com.study.alibaba.cloud.rocketmq.message.IMessageProvider;
import com.study.alibaba.cloud.rocketmq.mode.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {IMessageProvider.class})
public class ConsumeSubscriber {

    private Logger logger = LoggerFactory.getLogger(ConsumeSubscriber.class);

    @Autowired
    private OrderMessageHandler orderMessageHandler;
    /**
     *  不支持streamListener
     */
//    @StreamListener("messageSource")
    @Bean
    public ApplicationRunner runner(PollableMessageSource input){
        return args -> {
            while(true){
                input.poll(message->{
                    orderMessageHandler.afterOrderCompleted((Order) message.getPayload());
                });
            }
        };
    }


}
