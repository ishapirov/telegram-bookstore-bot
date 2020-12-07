package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.ParentingBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentingBookRepository extends PagingAndSortingRepository<ParentingBook,String> {
    Optional<ParentingBook> findByBookISBN(String bookISBN);
    Page<ParentingBook> findAll(Pageable pageable);
}
