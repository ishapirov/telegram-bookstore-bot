package com.ishapirov.telegrambot.views.bookcatalog.dto;

import com.ishapirov.telegrambot.domain.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCatalogViewInfo {
    private Book book;
    private boolean hasNext;
    private String convertedPrice;
    private Integer bookQuantityAvailable;
    private String bookType;
    private String bookSubType;
    private int index;
    private int bookQuantitySelected;
    private boolean addedToCartMessage;
    private boolean bookAlreadyInCart;
    private boolean lessBooksAvailable;
    private boolean noBooksInCatalog;

    private String localeString;

    public boolean hasNext() {
        return hasNext;
    }
}
