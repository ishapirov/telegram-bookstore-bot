package com.ishapirov.telegrambot.views.booksordered.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ShippingOrderInfo {
    private List<BookOrderedInfo> booksOrdered;
    private Date dateOrdered;
    private String totalPrice;
}
