package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAddedToCartRepository extends JpaRepository<BookAddedToCart, BookAddedToCartId> {
}
