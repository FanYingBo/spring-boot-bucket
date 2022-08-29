package com.study.alibaba.rocketmq.producer.dto;

public class CreateOrderRequest {

    private Long skuId;
    private Integer number;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
