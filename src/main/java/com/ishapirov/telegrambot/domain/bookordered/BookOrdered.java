package com.ishapirov.telegrambot.domain.bookordered;

import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.shippingorder.ShippingOrder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="book_ordered")
public class BookOrdered {
    @EmbeddedId
    private BookOrderedId bookOrderedId = new BookOrderedId();

    @ManyToOne
    @MapsId("providerPaymentChargeId")
    @JoinColumn(name="providerPaymentChargeId")
    private ShippingOrder shippingOrder;

    @ManyToOne
    @MapsId("bookISBN")
    @JoinColumn(name="bookISBN")
    private Book book;

    private Integer quantity;

}
