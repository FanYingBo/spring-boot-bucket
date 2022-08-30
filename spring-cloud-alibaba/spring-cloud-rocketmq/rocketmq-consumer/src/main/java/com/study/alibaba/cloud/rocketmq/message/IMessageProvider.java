package com.study.alibaba.cloud.rocketmq.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.binder.PollableMessageSource;

public interface IMessageProvider {

    @Input
    PollableMessageSource input();

}
