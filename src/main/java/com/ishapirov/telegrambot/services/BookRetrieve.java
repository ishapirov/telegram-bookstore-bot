package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.domain.book.*;
import com.ishapirov.telegrambot.repositories.BookRepository;
import com.ishapirov.telegrambot.repositories.KidBookRepository;
import com.ishapirov.telegrambot.repositories.ParentingBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookRetrieve {
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
        ParentingBookCategory parentingBookCategory = ParentingBookCategory.valueOf(userCallbackRequest.getBookSubType());
        List<ParentingBook> books = parentingBookRepository.findByParentingBookCategory(parentingBookCategory);
        if(books.size() >= userCallbackRequest.getIndex())
            return null;
        if(books.size() == userCallbackRequest.getIndex() + 1){
            bookInfo.setHasNext(false);
        } else bookInfo.setHasNext(true);
        bookInfo.setBook(books.get(userCallbackRequest.getIndex()));
        return bookInfo;
    }

    private BookInfo getKidsBook(UserCallbackRequest userCallbackRequest){
        BookInfo bookInfo = new BookInfo();
        KidBookCategory kidBookCategory = KidBookCategory.valueOf(userCallbackRequest.getBookSubType());
        List<KidBook> books = kidBookRepository.findByKidBookCategory(kidBookCategory);
        if(books.size() >= userCallbackRequest.getIndex())
            return null;
        if(books.size() == userCallbackRequest.getIndex() + 1){
            bookInfo.setHasNext(false);
        } else bookInfo.setHasNext(true);
        bookInfo.setBook(books.get(userCallbackRequest.getIndex()));
        return bookInfo;
    }
}
