package com.ishapirov.telegrambot.views.booksordered.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShippingOrderViewInfo {
    List<ShippingOrderInfo> shippingOrders;
    String locale;
}
