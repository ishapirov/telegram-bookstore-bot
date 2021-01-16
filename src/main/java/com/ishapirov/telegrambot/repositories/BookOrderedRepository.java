package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.bookordered.BookOrdered;
import com.ishapirov.telegrambot.domain.bookordered.BookOrderedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookOrderedRepository extends JpaRepository<BookOrdered, BookOrderedId> {
}
