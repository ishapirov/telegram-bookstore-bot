package com.ishapirov.telegrambot.services.cartservices;

import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCartId;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.BookAlreadyInCartException;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.LessBooksAvailableThanRequestedException;
import com.ishapirov.telegrambot.repositories.BookAddedToCartRepository;
import com.ishapirov.telegrambot.repositories.CartRepository;
import com.ishapirov.telegrambot.services.bookservices.BookInventoryService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    BookAddedToCartRepository bookAddedToCartRepository;
    @Autowired
    BookInventoryService bookInventoryService;


    public Cart getCart(int userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty())
            return createNewCart(userId);
        else
            return cart.get();
    }

    private Cart createNewCart(int userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setUserProfile(userProfileService.getUserProfile(userId));
        cartRepository.save(cart);
        return cart;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public BookInCart getBookFromCart(int userId,int index) {
        Cart cart = getCart(userId);
        BookInCart bookInCart = new BookInCart();
        BookAddedToCart bookAddedToCart = cart.getBookFromCart(index);
        if(bookAddedToCart == null)
            return null;
        bookInCart.setBookAddedToCart(bookAddedToCart);
        if (cart.getBooksInCart().size() == index + 1) {
            bookInCart.setHasNext(false);
        } else
            bookInCart.setHasNext(true);
        return bookInCart;
    }

    @Transactional
    public void addBookToCart(Book book, Cart cart, Integer quantity){
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

    public void removeAllBooksFromCart(Cart cart){
        for(BookAddedToCart bookAddedToCart:cart.getBooksInCart()){
            removeBookFromCart(bookAddedToCart);
            bookInventoryService.subtractQuantity(bookAddedToCart.getBook().getBookISBN(),bookAddedToCart.getQuantity());
        }
    }

}
