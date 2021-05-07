package com.ishapirov.telegrambot.controllers.language.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedLanguage {
    Integer userId;
    String localeString;
}
