package com.ishapirov.telegrambot.services.orders;

import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.bookordered.BookOrdered;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.domain.shippingorder.ShippingOrder;
import com.ishapirov.telegrambot.repositories.BookOrderedRepository;
import com.ishapirov.telegrambot.repositories.ShippingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public class OrdersService {
    @Autowired
    ShippingOrderRepository shippingOrderRepository;
    @Autowired
    BookOrderedRepository bookOrderedRepository;

    public void createShippingOrder(Cart cart, Message message){
        ShippingOrder shippingOrder = new ShippingOrder(message);
        shippingOrderRepository.save(shippingOrder);
        for(BookAddedToCart bookAddedToCart: cart.getBooksInCart()){
            BookOrdered bookOrdered = new BookOrdered();
            bookOrdered.setBook(bookAddedToCart.getBook());
            bookOrdered.setShippingOrder(shippingOrder);
            bookOrdered.setQuantity(bookAddedToCart.getQuantity());
            bookOrderedRepository.save(bookOrdered);
        }
    }

    public List<ShippingOrder> getUserShippingOrders(int userId){
        return shippingOrderRepository.findByUserId(userId);
    }
}
