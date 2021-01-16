package com.ishapirov.telegrambot.repositories;

import com.ishapirov.telegrambot.domain.shippingorder.ShippingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingOrderRepository extends JpaRepository<ShippingOrder, String> {
    public List<ShippingOrder> findByUserId(Integer userId);
}
