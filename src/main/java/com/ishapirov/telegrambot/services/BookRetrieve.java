package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.book.KidBook;
import com.ishapirov.telegrambot.domain.book.ParentingBook;
import com.ishapirov.telegrambot.repositories.BookRepository;
import com.ishapirov.telegrambot.repositories.KidBookRepository;
import com.ishapirov.telegrambot.repositories.ParentingBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookRetrieve {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private KidBookRepository kidBookRepository;
    @Autowired
    private ParentingBookRepository parentingBookRepository;

    public Page<Book> getAllBooks(Pageable pageable){
        return bookRepository.findAll(pageable);
    }

    public void saveParentingBook(ParentingBook book){
        parentingBookRepository.save(book);
    }

    public void saveKidBook(KidBook book){
        kidBookRepository.save(book);
    }
}
