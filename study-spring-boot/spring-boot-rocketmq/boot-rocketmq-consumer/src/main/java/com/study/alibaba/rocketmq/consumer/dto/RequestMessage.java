package com.study.alibaba.rocketmq.consumer.dto;

public class RequestMessage {

    private String name;
    private boolean sync;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
