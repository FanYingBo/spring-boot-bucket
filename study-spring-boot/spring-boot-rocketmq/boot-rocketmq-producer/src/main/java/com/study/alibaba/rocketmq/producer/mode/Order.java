package com.study.alibaba.rocketmq.producer.mode;

import com.study.alibaba.rocketmq.producer.edm.OrderState;

import java.math.BigDecimal;

public class Order {
    private String orderNumber;
    private Long userId;
    private Long skuId;
    private int number;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private BigDecimal actualPaid;
    private boolean useDiscount;
    private Long couponId;

    private OrderState orderState;
    private String transId;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getActualPaid() {
        return actualPaid;
    }

    public void setActualPaid(BigDecimal actualPaid) {
        this.actualPaid = actualPaid;
    }

    public boolean isUseDiscount() {
        return useDiscount;
    }

    public void setUseDiscount(boolean useDiscount) {
        this.useDiscount = useDiscount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}
