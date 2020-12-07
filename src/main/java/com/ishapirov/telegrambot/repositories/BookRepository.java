package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book,String> {
    Page<Book> findAll(Pageable pageable);
}
