package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BookRepository extends JpaRepository<Book,Integer> {
}
