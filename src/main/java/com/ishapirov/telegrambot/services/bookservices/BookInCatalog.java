package com.ishapirov.telegrambot.services.bookservices;

import com.ishapirov.telegrambot.domain.book.Book;
import lombok.Data;

@Data
public class BookInCatalog {
    private Book book;
    private boolean hasNext;

    public boolean hasNext() {
        return this.hasNext;
    }
}
