package com.ishapirov.telegrambot.controllers.bookcatalog;

import com.ishapirov.telegrambot.controllers.bookcatalog.dto.CatalogControllerData;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.BookAlreadyInCartException;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.LessBooksAvailableThanRequestedException;
import com.ishapirov.telegrambot.services.bookservices.BookInCatalog;
import com.ishapirov.telegrambot.services.bookservices.BookInventoryService;
import com.ishapirov.telegrambot.services.bookservices.BookCatalogRetrievalService;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.views.bookcatalog.dto.BookCatalogViewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BookCatalogController {
    @Autowired
    CurrencyConversionService currencyConversionService;
    @Autowired
    BookCatalogRetrievalService bookCatalogRetrievalService;
    @Autowired
    BookInventoryService bookInventoryService;
    @Autowired
    CartService cartService;

    public BookCatalogViewInfo getBookCatalogBookInfo(CatalogControllerData controllerData){
        BookCatalogViewInfo bookCatalogViewInfo = new BookCatalogViewInfo();
        bookCatalogViewInfo.setBookType(controllerData.getBookType());
        bookCatalogViewInfo.setBookSubType(controllerData.getBookSubType());
        bookCatalogViewInfo.setIndex(controllerData.getIndex());
        bookCatalogViewInfo.setBookQuantitySelected(controllerData.getQuantitySelected());

        BookInCatalog bookInCatalog = bookCatalogRetrievalService.getBook(controllerData);
        if(bookInCatalog == null){
            bookCatalogViewInfo.setNoBooksInCatalog(true);
            return bookCatalogViewInfo;
        }
        bookCatalogViewInfo.setNoBooksInCatalog(false);
        bookCatalogViewInfo.setBook(bookInCatalog.getBook());
        bookCatalogViewInfo.setHasNext(bookInCatalog.hasNext());

        bookCatalogViewInfo.setBookQuantityAvailable(bookInventoryService.getQuantity(bookInCatalog.getBook().getBookISBN()));

        bookCatalogViewInfo.setConvertedPrice(currencyConversionService.displayPrice(controllerData.getUserId(), bookInCatalog.getBook().getPrice()));
        return bookCatalogViewInfo;
    }

    public BookCatalogViewInfo addBookToCart(CatalogControllerData controllerData){
        BookCatalogViewInfo bookCatalogViewInfo = getBookCatalogBookInfo(controllerData);
        Cart cart = cartService.getCart(controllerData.getUserId());
        try {
            cartService.addBookToCart(bookCatalogViewInfo.getBook(), cart, bookCatalogViewInfo.getBookQuantitySelected());
            bookCatalogViewInfo.setAddedToCartMessage(true);
        } catch(LessBooksAvailableThanRequestedException e){
            bookCatalogViewInfo.setLessBooksAvailable(true);
        } catch(BookAlreadyInCartException e){
            bookCatalogViewInfo.setBookAlreadyInCart(true);
        }
        return bookCatalogViewInfo;
    }

    public BookCatalogViewInfo forwardABook(CatalogControllerData controllerData){
        controllerData.setIndex(controllerData.getIndex() + 1);
        return getBookCatalogBookInfo(controllerData);
    }

    public BookCatalogViewInfo backABook(CatalogControllerData controllerData){
        controllerData.setIndex(controllerData.getIndex() - 1);
        return getBookCatalogBookInfo(controllerData);
    }

    public BookCatalogViewInfo increaseQuantity(CatalogControllerData controllerData){
        controllerData.setQuantitySelected(controllerData.getQuantitySelected() + 1);
        return getBookCatalogBookInfo(controllerData);
    }

    public BookCatalogViewInfo decreaseQuantity(CatalogControllerData controllerData){
        controllerData.setQuantitySelected(controllerData.getQuantitySelected() - 1);
        return getBookCatalogBookInfo(controllerData);
    }

}
