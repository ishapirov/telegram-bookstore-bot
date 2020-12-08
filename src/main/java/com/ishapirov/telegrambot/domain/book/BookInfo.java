package com.ishapirov.telegrambot.domain.book;

import lombok.Data;

@Data
public class BookInfo {
    private Book book;
    private boolean hasNext;
}
