package com.ishapirov.telegrambot.services.bookservices;

import com.ishapirov.telegrambot.controllers.bookcatalog.dto.CatalogControllerData;
import com.ishapirov.telegrambot.domain.book.*;
import com.ishapirov.telegrambot.repositories.BookRepository;
import com.ishapirov.telegrambot.repositories.KidBookRepository;
import com.ishapirov.telegrambot.repositories.ParentingBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookCatalogRetrievalService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private KidBookRepository kidBookRepository;
    @Autowired
    private ParentingBookRepository parentingBookRepository;

    public void saveParentingBook(ParentingBook book){
        parentingBookRepository.save(book);
    }

    public void saveKidBook(KidBook book){
        kidBookRepository.save(book);
    }

    @Transactional
    public BookInCatalog getBook(CatalogControllerData catalogControllerData) {
        if(catalogControllerData.getBookType().equals(ParentingBook.typeOfBook())){
            return getParentingBook(catalogControllerData);
        }
        if(catalogControllerData.getBookType().equals(KidBook.typeOfBook())){
            return getKidsBook(catalogControllerData);
        }
        else return getBookNoFilter(catalogControllerData);
    }

    private BookInCatalog getBookNoFilter(CatalogControllerData catalogControllerData){

        BookInCatalog bookInCatalog = new BookInCatalog();
        List<Book> books = bookRepository.findAll();

        if(books.size() == 0)
            return null;
        if(catalogControllerData.getIndex() + 1 == books.size()){
            bookInCatalog.setHasNext(false);
        } else {
            bookInCatalog.setHasNext(true);
        }

        bookInCatalog.setBook(books.get(catalogControllerData.getIndex()));
        return bookInCatalog;
    }

    private BookInCatalog getParentingBook(CatalogControllerData catalogControllerData){
        BookInCatalog bookInCatalog = new BookInCatalog();
        List<ParentingBook> books;
        if(catalogControllerData.getBookSubType().equals("all"))
            books = parentingBookRepository.findAll();
        else{
            ParentingBookCategory parentingBookCategory = ParentingBookCategory.valueOf(catalogControllerData.getBookSubType());
            books = parentingBookRepository.findByParentingBookCategory(parentingBookCategory);
        }
        if(books.size() == 0)
            return null;
        if(books.size() == catalogControllerData.getIndex() + 1){
            bookInCatalog.setHasNext(false);
        } else bookInCatalog.setHasNext(true);
        bookInCatalog.setBook(books.get(catalogControllerData.getIndex()));
        return bookInCatalog;
    }

    private BookInCatalog getKidsBook(CatalogControllerData catalogControllerData){
        BookInCatalog bookInCatalog = new BookInCatalog();
        List<KidBook> books;
        if(catalogControllerData.getBookSubType().equals("all"))
            books = kidBookRepository.findAll();
        else{
            KidBookCategory kidBookCategory = KidBookCategory.valueOf(catalogControllerData.getBookSubType());
            books = kidBookRepository.findByKidBookCategory(kidBookCategory);
        }
        if(books.size() == 0)
            return null;
        if(books.size() == catalogControllerData.getIndex() + 1){
            bookInCatalog.setHasNext(false);
        } else bookInCatalog.setHasNext(true);
        bookInCatalog.setBook(books.get(catalogControllerData.getIndex()));
        return bookInCatalog;
    }
}
