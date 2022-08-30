package com.study.alibaba.cloud.rocketmq.provider;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface IMessageServiceProvider {

    @Output
    MessageChannel destination();
}
