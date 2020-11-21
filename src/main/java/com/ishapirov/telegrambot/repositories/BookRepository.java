package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    Optional<Book> findByBookNumber(Integer bookNumber);
}
