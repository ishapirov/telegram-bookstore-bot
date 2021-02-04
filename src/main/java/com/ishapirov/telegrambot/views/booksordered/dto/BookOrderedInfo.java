package com.ishapirov.telegrambot.views.booksordered.dto;

import lombok.Data;

@Data
public class BookOrderedInfo {
    private String title;
    private int quantity;
    private String price;
}
