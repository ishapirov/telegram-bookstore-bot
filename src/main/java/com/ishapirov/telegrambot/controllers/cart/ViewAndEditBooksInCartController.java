package com.ishapirov.telegrambot.controllers.cart;

import com.ishapirov.telegrambot.controllers.cart.dto.ViewAndEditBooksControllerInfo;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.services.cartservices.BookInCart;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.views.cart.dto.ViewRemoveBooksViewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;

@Controller
public class ViewAndEditBooksInCartController {
    @Autowired
    CartService cartService;
    @Autowired
    CurrencyConversionService currencyConversionService;
    @Autowired
    UserProfileService userProfileService;

    public static final String TYPE_STRING ="viewandedit";

    @Transactional
    public ViewRemoveBooksViewInfo getBookInCartInfo(ViewAndEditBooksControllerInfo viewAndEditBooksControllerInfo) {
        ViewRemoveBooksViewInfo viewRemoveBooksViewInfo = new ViewRemoveBooksViewInfo();
        BookInCart bookInCart = cartService.getBookFromCart(viewAndEditBooksControllerInfo.getUserId(), viewAndEditBooksControllerInfo.getIndex());
        if(bookInCart == null){
            viewRemoveBooksViewInfo.setNoBooksInCart(true);
            return viewRemoveBooksViewInfo;
        }
        viewRemoveBooksViewInfo.setNoBooksInCart(false);
        viewRemoveBooksViewInfo.setBook(bookInCart.getBookAddedToCart().getBook());
        viewRemoveBooksViewInfo.setHasNext(bookInCart.hasNext());
        viewRemoveBooksViewInfo.setIndex(viewAndEditBooksControllerInfo.getIndex());
        viewRemoveBooksViewInfo.setQuantitySelected(bookInCart.getBookAddedToCart().getQuantity());
        viewRemoveBooksViewInfo.setConvertedBookPrice(currencyConversionService.displayPrice(viewAndEditBooksControllerInfo.getUserId(),
                bookInCart.getBookAddedToCart().getBook().getPrice()));
        viewRemoveBooksViewInfo.setConvertedTotalPrice(currencyConversionService.displayPrice(viewAndEditBooksControllerInfo.getUserId(),
                bookInCart.getBookAddedToCart().getBook().getPrice(),
                bookInCart.getBookAddedToCart().getQuantity()));
        viewRemoveBooksViewInfo.setLocaleString(userProfileService.getLocaleForUser(viewAndEditBooksControllerInfo.getUserId()));
        viewRemoveBooksViewInfo.setRemovedFromCart(false);
        return viewRemoveBooksViewInfo;
    }

    @Transactional
    public ViewRemoveBooksViewInfo backABook(ViewAndEditBooksControllerInfo viewAndEditBooksControllerInfo) {
        viewAndEditBooksControllerInfo.setIndex(viewAndEditBooksControllerInfo.getIndex()-1);
        return getBookInCartInfo(viewAndEditBooksControllerInfo);
    }

    @Transactional
    public ViewRemoveBooksViewInfo forwardABook(ViewAndEditBooksControllerInfo viewAndEditBooksControllerInfo) {
        viewAndEditBooksControllerInfo.setIndex(viewAndEditBooksControllerInfo.getIndex()+1);
        return getBookInCartInfo(viewAndEditBooksControllerInfo);
    }

    @Transactional
    public ViewRemoveBooksViewInfo removeBook(ViewAndEditBooksControllerInfo viewAndEditBooksControllerInfo) {
        BookAddedToCart bookAddedToCart = cartService.getBookFromCart(viewAndEditBooksControllerInfo.getUserId(), viewAndEditBooksControllerInfo.getIndex()).getBookAddedToCart();
        cartService.removeBookFromCart(bookAddedToCart);
        ViewRemoveBooksViewInfo viewRemoveBooksViewInfo = getBookInCartInfo(viewAndEditBooksControllerInfo);
        viewAndEditBooksControllerInfo.setIndex(0);
        viewRemoveBooksViewInfo.setRemovedFromCart(true);
        return viewRemoveBooksViewInfo;
    }


}
