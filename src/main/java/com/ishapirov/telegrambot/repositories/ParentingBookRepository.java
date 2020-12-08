package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.ParentingBook;
import com.ishapirov.telegrambot.domain.book.ParentingBookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentingBookRepository extends JpaRepository<ParentingBook,String> {
    Optional<ParentingBook> findByBookISBN(String bookISBN);
    List<ParentingBook> findByParentingBookCategory(ParentingBookCategory parentingBookCategory);
}
