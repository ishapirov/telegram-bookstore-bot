package com.ishapirov.telegrambot.controllers.bookcatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogControllerData {
    private int userId;
    private int index;
    private String bookType;
    private String bookSubType;
    private int quantitySelected;
}
