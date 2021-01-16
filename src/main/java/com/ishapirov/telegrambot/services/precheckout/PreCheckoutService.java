package com.ishapirov.telegrambot.services.precheckout;

import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.services.bookservices.BookInventoryService;
import com.ishapirov.telegrambot.services.cartservices.AddRemoveBookToCartService;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PreCheckoutService {
    @Autowired
    BookInventoryService bookInventoryService;
    @Autowired
    CartService cartService;
    @Autowired
    AddRemoveBookToCartService addRemoveBookToCartService;

    @Transactional
    public BotApiMethod<?> handlePreCheckoutQuery(PreCheckoutQuery preCheckoutQuery) {
        Integer userId = preCheckoutQuery.getFrom().getId();
        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
        answerPreCheckoutQuery.setPreCheckoutQueryId(preCheckoutQuery.getId());
        List<String> unavailableBookTitles = getAnyUnavailableBookTitlesAndRemoveFromCart(userId);
        if(unavailableBookTitles.size() == 0){
            answerPreCheckoutQuery.setOk(true);
        } else {
            answerPreCheckoutQuery.setOk(false);
            String error = "The following books are no longer in stock and have been removed from your cart: ";
            for(String title: unavailableBookTitles)
                error+=title;
            answerPreCheckoutQuery.setErrorMessage(error);
        }
        return answerPreCheckoutQuery;
    }

    private List<String> getAnyUnavailableBookTitlesAndRemoveFromCart(Integer userId){
            List<String> namesOfUnavailableBooks = new ArrayList<>();
            Cart userCart = cartService.getCart(userId);
            for(BookAddedToCart bookAddedToCart: userCart.getBooksInCart()){
                String bookID = bookAddedToCart.getBook().getBookISBN();
                if(bookAddedToCart.getQuantity() > bookInventoryService.getQuantity(bookID)){
                    namesOfUnavailableBooks.add(bookAddedToCart.getBook().getTitle());
                    addRemoveBookToCartService.removeBookFromCart(bookAddedToCart);
                }
            }
            return namesOfUnavailableBooks;
    }


}
