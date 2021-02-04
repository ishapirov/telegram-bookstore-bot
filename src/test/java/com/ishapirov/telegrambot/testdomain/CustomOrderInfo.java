package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.payments.OrderInfo;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingAddress;

public class CustomOrderInfo extends OrderInfo {
    private ShippingAddress shippingAddress;
    private String email;
    private String phoneNumber;

    public CustomOrderInfo(ShippingAddress shippingAddress, String email, String phoneNumber) {
        this.shippingAddress = shippingAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }
}


