package com.ishapirov.telegrambot.controllers.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedCurrency {
    Integer userId;
    String currencyCodeSelected;
}
