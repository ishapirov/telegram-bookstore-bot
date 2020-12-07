package com.ishapirov.telegrambot.domain.user;

import com.ishapirov.telegrambot.domain.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Entity
@Table(name="userprofile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    private int userId;
    private Currency currency;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "booksInCart",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> booksInCart;

    public UserProfile(Integer userId, Currency currency) {
        this.userId = userId;
        this.currency = currency;
        this.booksInCart = new ArrayList<>();
    }

    public void addToCart(Book book){
        booksInCart.add(book);
    }
}
