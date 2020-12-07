package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.KidBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KidBookRepository extends PagingAndSortingRepository<KidBook,String> {
    Optional<KidBook> findByBookISBN(String bookISBN);
    Page<KidBook> findAll(Pageable pageable);
}
