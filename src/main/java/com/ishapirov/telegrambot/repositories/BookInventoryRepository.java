package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.bookinventory.BookInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookInventoryRepository extends JpaRepository<BookInventory,String> {
    public Optional<BookInventory> findByBookISBN(String bookISBN);
}
