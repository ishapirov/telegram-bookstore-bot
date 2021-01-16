package com.ishapirov.telegrambot.services.cartservices;

import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCartId;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.BookAlreadyInCartException;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.LessBooksAvailableThanRequestedException;
import com.ishapirov.telegrambot.repositories.BookAddedToCartRepository;
import com.ishapirov.telegrambot.services.bookservices.BookInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddRemoveBookToCartService {
    @Autowired
    BookAddedToCartRepository bookAddedToCartRepository;
    @Autowired
    BookInventoryService bookInventoryService;

    public void addBookToCart(Book book, Cart cart,Integer quantity){
        if(bookInventoryService.getQuantity(book.getBookISBN()) < quantity)
            throw new LessBooksAvailableThanRequestedException();
        BookAddedToCartId bookAddedToCartId = new BookAddedToCartId(book.getBookISBN(), cart.getUserId());
        Optional<BookAddedToCart> bookAddedToCart = bookAddedToCartRepository.findById(bookAddedToCartId);
        if(bookAddedToCart.isPresent()){
            BookAddedToCart bookAdded = bookAddedToCart.get();
            bookAdded.setQuantity(quantity);
            bookAddedToCartRepository.save(bookAdded);
            throw new BookAlreadyInCartException();
        }
        addNewBookToCart(book,cart,quantity);
    }

    public void removeBookFromCart(BookAddedToCart bookAddedToCart){
        bookAddedToCartRepository.delete(bookAddedToCart);
    }

    public void addNewBookToCart(Book book, Cart cart,Integer quantity){
        BookAddedToCart bookAddedToCart = new BookAddedToCart();
        bookAddedToCart.setBook(book);
        bookAddedToCart.setCart(cart);
        bookAddedToCart.setQuantity(quantity);
        bookAddedToCartRepository.save(bookAddedToCart);
    }

    public void successfulPurchase(Cart cart){
        for(BookAddedToCart bookAddedToCart:cart.getBooksInCart()){
            removeBookFromCart(bookAddedToCart);
            bookInventoryService.subtractQuantity(bookAddedToCart.getBook().getBookISBN(),bookAddedToCart.getQuantity());
        }
    }

}
