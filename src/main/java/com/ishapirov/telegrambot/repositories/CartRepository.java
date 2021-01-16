package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUserId(Integer userId);
}
