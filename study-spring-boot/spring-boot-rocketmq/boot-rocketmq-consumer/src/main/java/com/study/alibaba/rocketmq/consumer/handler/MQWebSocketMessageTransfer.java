package com.study.alibaba.rocketmq.consumer.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.alibaba.rocketmq.consumer.dto.RequestMessage;
import com.study.alibaba.rocketmq.consumer.dto.WSResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MQWebSocketMessageTransfer {

    private static final Logger logger = LoggerFactory.getLogger(MQWebSocketMessageTransfer.class);
    private LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(RequestMessage message){
        if(message.isSync()){
            rocketMQTemplate.asyncSend("localTest:TagA", MessageBuilder.withPayload(message).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.info("On Sent Success Event !!!");
                }

                @Override
                public void onException(Throwable throwable) {
                    logger.error("On Sent Failure Event !!!");
                }
            });
        }else{
            rocketMQTemplate.send("localTest:TagA", MessageBuilder.withPayload(message).build());
            logger.info("Sent Success !!! ");
        }
    }

    public void putMessage(String message){
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            logger.error("put message error "+ e);
        }
    }

    public WSResponse receiveMessage(){
        while(true){
            String message = null;
            try {
                message = messageQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("take message error " , e.getMessage());
                continue;
            }
            if(StringUtils.isNotBlank(message)){
                WSResponse wsResponse = new WSResponse();
                if(message.startsWith("hello")){
                    wsResponse.setName(message.split(" ")[1]);
                    wsResponse.setMessage("hello");
                }else{
                    ObjectMapper objectMapper = new ObjectMapper();
                    RequestMessage requestMessage = null;
                    try {
                        requestMessage = objectMapper.readValue(message, RequestMessage.class);
                    } catch (JsonProcessingException e) {
                        logger.error("read message error " , e.getMessage());
                        continue;
                    }

                    wsResponse.setName(requestMessage.getName());
                    wsResponse.setMessage("nice to meet you");
                }
                return wsResponse;
            }else{
                return new WSResponse();
            }
        }
    }
}
