package com.ishapirov.telegrambot.controllers.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewAndEditBooksControllerInfo {
    private int userId;
    private int index;
}
