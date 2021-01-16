package com.ishapirov.telegrambot.domain.bookaddedtocart;

import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.cart.Cart;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="book_added_to_cart")
public class BookAddedToCart {
    @EmbeddedId
    private BookAddedToCartId bookAddedToCartId = new BookAddedToCartId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="userId")
    private Cart cart;

    @ManyToOne
    @MapsId("bookISBN")
    @JoinColumn(name="bookISBN")
    private Book book;

    private Integer quantity;
}
