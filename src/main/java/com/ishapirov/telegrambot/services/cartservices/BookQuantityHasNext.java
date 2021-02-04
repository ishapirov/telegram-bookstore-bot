package com.ishapirov.telegrambot.services.cartservices;

import com.ishapirov.telegrambot.domain.book.Book;
import lombok.Data;

@Data
public class BookQuantityHasNext {
    private Book book;
    private int quantityInCart;
    private boolean hasNext;

    public boolean hasNext() {
        return hasNext;
    }
}
