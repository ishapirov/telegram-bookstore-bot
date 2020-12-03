package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.KidBook;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KidBookRepository extends BookRepository{
    Optional<KidBook> findByBookNumber(Integer bookNumber);
}
