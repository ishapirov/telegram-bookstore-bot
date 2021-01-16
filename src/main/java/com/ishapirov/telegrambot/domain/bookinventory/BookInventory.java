package com.ishapirov.telegrambot.domain.bookinventory;

import com.ishapirov.telegrambot.domain.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="book_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInventory {
    @Id
    private String bookISBN;

    @MapsId
    @OneToOne
    private Book book;

    private int quantity;
}
