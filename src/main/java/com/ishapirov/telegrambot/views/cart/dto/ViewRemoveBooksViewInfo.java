package com.ishapirov.telegrambot.views.cart.dto;

import com.ishapirov.telegrambot.domain.book.Book;
import lombok.Data;

@Data
public class ViewRemoveBooksViewInfo {
    private Book book;
    private boolean hasNext;
    private int index;
    private String convertedBookPrice;
    private String convertedTotalPrice;
    private int quantitySelected;
    private boolean noBooksInCart;
    private boolean removedFromCart;
    private String localeString;

    public boolean hasNext(){
        return this.hasNext;
    }
}
