package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.KidBook;
import com.ishapirov.telegrambot.domain.book.KidBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KidBookRepository extends JpaRepository<KidBook,String> {
    Optional<KidBook> findByBookISBN(String bookISBN);
    List<KidBook> findByKidBookCategory(KidBookCategory kidBookCategory);
}
