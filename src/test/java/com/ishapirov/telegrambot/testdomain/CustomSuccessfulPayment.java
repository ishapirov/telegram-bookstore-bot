package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.payments.OrderInfo;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

public class CustomSuccessfulPayment extends SuccessfulPayment {
    private String providerPaymentChargeId;
    private int totalAmount;
    private String currency;
    private String shippingOptionId;
    private OrderInfo orderInfo;
    private String telegramPaymentChargeId;

    public CustomSuccessfulPayment(String providerPaymentChargeId, int totalAmount, String currency, String shippingOptionId, OrderInfo orderInfo,String telegramPaymentChargeId) {
        this.providerPaymentChargeId = providerPaymentChargeId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.shippingOptionId = shippingOptionId;
        this.orderInfo = orderInfo;
        this.telegramPaymentChargeId = telegramPaymentChargeId;
    }

    @Override
    public String getProviderPaymentChargeId(){
        return providerPaymentChargeId;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public Integer getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String getShippingOptionId() {
        return shippingOptionId;
    }

    @Override
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    @Override
    public String getTelegramPaymentChargeId() {
        return telegramPaymentChargeId;
    }
}
