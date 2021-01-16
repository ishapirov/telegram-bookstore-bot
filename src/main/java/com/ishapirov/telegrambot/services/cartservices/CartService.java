package com.ishapirov.telegrambot.services.cartservices;

import com.ishapirov.telegrambot.domain.book.BookInfo;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.repositories.CartRepository;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionRate;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserProfileService userProfileService;

    public Cart getCart(int userId){
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if(cart.isEmpty())
            return createNewCart(userId);
        else
            return cart.get();
    }

    private Cart createNewCart(int userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setUserProfile(userProfileService.getUserProfile(userId));
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public BookInfo getBookFromCart(UserCallbackRequest userCallbackRequest){
        Cart cart = getCart(userCallbackRequest.getUserId());
        BookInfo bookInfo = new BookInfo();
        BookAddedToCart bookAddedToCart = cart.getBookFromCart(userCallbackRequest.getIndex());
        if(bookAddedToCart == null)
            return null;
        bookInfo.setBook(bookAddedToCart.getBook());
        if(cart.getBooksInCart().size() == userCallbackRequest.getIndex() + 1) {
            bookInfo.setHasNext(false);
        } else bookInfo.setHasNext(true);
        bookInfo.setQuantity(bookAddedToCart.getQuantity());
        return bookInfo;
    }

    @Transactional
    public BigDecimal getTotalCartCost(UserCallbackRequest userCallbackRequest, CurrencyConversionRate currencyConversionRate){
        BigDecimal totalCost = new BigDecimal(0);
        Cart cart = getCart(userCallbackRequest.getUserId());
        for(BookAddedToCart bookAddedToCart: cart.getBooksInCart())
            totalCost = totalCost.add(currencyConversionRate.getConvertedPrice(bookAddedToCart.getBook().getPrice().multiply(new BigDecimal(bookAddedToCart.getQuantity()))));
        return totalCost;
    }


}
