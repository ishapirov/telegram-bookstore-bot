package com.ishapirov.telegrambot.domain.cart;

import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private int userId;

    @MapsId
    @OneToOne
    private UserProfile userProfile;

    @OneToMany(mappedBy = "cart")
    private List<BookAddedToCart> booksInCart = new ArrayList<>();

    public boolean containsBook(String bookISBN){
        for (BookAddedToCart bookAddedToCart: booksInCart){
            if(bookAddedToCart.getBook().getBookISBN().equals(bookISBN))
                return true;
        }
        return false;
    }

    public BookAddedToCart getBookFromCart(int index){
        if(index >= booksInCart.size())
            return null;
        else
            return booksInCart.get(index);
    }
}
