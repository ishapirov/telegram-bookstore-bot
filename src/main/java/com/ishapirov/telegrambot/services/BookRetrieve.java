package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.domain.Book;
import com.ishapirov.telegrambot.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookRetrieve {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }
}
