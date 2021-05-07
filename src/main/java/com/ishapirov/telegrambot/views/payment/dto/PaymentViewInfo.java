package com.ishapirov.telegrambot.views.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentViewInfo {
    private boolean cartEmpty;
    private BigDecimal totalCost;
    private String currency;
    private String localeString;
}
