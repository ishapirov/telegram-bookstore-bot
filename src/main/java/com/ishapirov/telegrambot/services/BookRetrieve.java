package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.repositories.KidBookRepository;
import com.ishapirov.telegrambot.repositories.ParentingBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookRetrieve {
    @Autowired
    private KidBookRepository kidBookRepository;
    @Autowired
    private ParentingBookRepository parentingBookRepository;

    public List<Book> getAllBooks(){
        return Stream.concat(kidBookRepository.findAll().stream(), parentingBookRepository.findAll().stream())
                .collect(Collectors.toList());
    }
}
