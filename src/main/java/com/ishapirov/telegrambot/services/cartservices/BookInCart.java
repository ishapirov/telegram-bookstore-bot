package com.ishapirov.telegrambot.services.cartservices;

import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import lombok.Data;

@Data
public class BookInCart {
    private BookAddedToCart bookAddedToCart;
    private boolean hasNext;

    public boolean hasNext(){
        return this.hasNext;
    }
}
