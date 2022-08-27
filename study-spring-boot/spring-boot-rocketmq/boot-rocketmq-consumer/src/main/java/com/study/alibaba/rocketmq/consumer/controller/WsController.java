package com.study.alibaba.rocketmq.consumer.controller;

import com.study.alibaba.rocketmq.consumer.dto.RequestMessage;
import com.study.alibaba.rocketmq.consumer.dto.WSResponse;
import com.study.alibaba.rocketmq.consumer.handler.MQWebSocketMessageTransfer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WsController {

    private static final Logger logger = LoggerFactory.getLogger(WsController.class);
    @Autowired
    private MQWebSocketMessageTransfer mqWebSocketMessageTransfer;

    @RequestMapping("/ws")
    public String getPage(){
        return "ws";
    }

    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public WSResponse say(RequestMessage message) {
        mqWebSocketMessageTransfer.sendMessage(message);
        return mqWebSocketMessageTransfer.receiveMessage();
    }
}
