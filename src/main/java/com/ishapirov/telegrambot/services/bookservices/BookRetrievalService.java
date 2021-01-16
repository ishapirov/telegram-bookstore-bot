package com.ishapirov.telegrambot.services.bookservices;

import com.ishapirov.telegrambot.domain.book.*;
import com.ishapirov.telegrambot.repositories.BookRepository;
import com.ishapirov.telegrambot.repositories.KidBookRepository;
import com.ishapirov.telegrambot.repositories.ParentingBookRepository;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookRetrievalService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private KidBookRepository kidBookRepository;
    @Autowired
    private ParentingBookRepository parentingBookRepository;
    @Autowired
    BookInventoryService bookInventoryService;

    public void saveParentingBook(ParentingBook book){
        parentingBookRepository.save(book);
    }

    public void saveKidBook(KidBook book){
        kidBookRepository.save(book);
    }

    @Transactional
    public BookInfo getBook(UserCallbackRequest userCallbackRequest) {
        if(userCallbackRequest.getBookType().equals(ParentingBook.typeOfBook())){
            return getParentingBook(userCallbackRequest);
        }
        if(userCallbackRequest.getBookType().equals(KidBook.typeOfBook())){
            return getKidsBook(userCallbackRequest);
        }
        else return getBookNoFilter(userCallbackRequest);
    }

    private BookInfo getBookNoFilter(UserCallbackRequest userCallbackRequest){
        BookInfo bookInfo = new BookInfo();
        List<Book> books = bookRepository.findAll();
        if(userCallbackRequest.getIndex() >= books.size())
            return null;
        if(books.size() == userCallbackRequest.getIndex() + 1){
            bookInfo.setHasNext(false);
        } else bookInfo.setHasNext(true);
        bookInfo.setBook(books.get(userCallbackRequest.getIndex()));
        return bookInfo;
    }

    private BookInfo getParentingBook(UserCallbackRequest userCallbackRequest){
        BookInfo bookInfo = new BookInfo();
        List<ParentingBook> books;
        if(userCallbackRequest.getBookSubType().equals("all"))
            books = parentingBookRepository.findAll();
        else{
            ParentingBookCategory parentingBookCategory = ParentingBookCategory.valueOf(userCallbackRequest.getBookSubType());
            books = parentingBookRepository.findByParentingBookCategory(parentingBookCategory);
        }
        if(userCallbackRequest.getIndex() >= books.size())
            return null;
        if(books.size() == userCallbackRequest.getIndex() + 1){
            bookInfo.setHasNext(false);
        } else bookInfo.setHasNext(true);
        bookInfo.setBook(books.get(userCallbackRequest.getIndex()));
        return bookInfo;
    }

    private BookInfo getKidsBook(UserCallbackRequest userCallbackRequest){
        BookInfo bookInfo = new BookInfo();
        List<KidBook> books;
        if(userCallbackRequest.getBookSubType().equals("all"))
            books = kidBookRepository.findAll();
        else{
            KidBookCategory kidBookCategory = KidBookCategory.valueOf(userCallbackRequest.getBookSubType());
            books = kidBookRepository.findByKidBookCategory(kidBookCategory);
        }
        if(userCallbackRequest.getIndex() >= books.size())
            return null;
        if(books.size() == userCallbackRequest.getIndex() + 1){
            bookInfo.setHasNext(false);
        } else bookInfo.setHasNext(true);
        bookInfo.setBook(books.get(userCallbackRequest.getIndex()));
        return bookInfo;
    }
}
