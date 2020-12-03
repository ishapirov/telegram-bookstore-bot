package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.ParentingBook;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentingBookRepository extends BookRepository{
    Optional<ParentingBook> findByBookNumber(Integer bookNumber);
}
