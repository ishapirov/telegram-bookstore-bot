package com.ishapirov.telegrambot.domain.cart;

import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.user.UserProfile;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionRate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
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

    public BigDecimal getTotalCartCost(CurrencyConversionRate currencyConversionRate) {
        BigDecimal totalCost = new BigDecimal(0);
        for (BookAddedToCart bookAddedToCart : this.getBooksInCart())
            totalCost = totalCost.add(currencyConversionRate.getConvertedPrice(bookAddedToCart.getBook().getPrice().multiply(new BigDecimal(bookAddedToCart.getQuantity()))));
        return totalCost;
    }
}
