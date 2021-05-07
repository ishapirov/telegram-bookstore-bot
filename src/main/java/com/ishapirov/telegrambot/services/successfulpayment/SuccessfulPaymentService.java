package com.ishapirov.telegrambot.services.successfulpayment;

import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.orders.OrdersService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.services.view.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.transaction.Transactional;

@Service
public class SuccessfulPaymentService {
    @Autowired
    CartService cartService;
    @Autowired
    LocaleMessageService localeMessageService;
    @Autowired
    ViewService viewService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    UserProfileService userProfileService;

    @Transactional
    public BotApiMethod<?> handleSuccessfulPayment(Message message) {
        Integer userId = message.getFrom().getId();
        Cart cart = cartService.getCart(userId);
        ordersService.createShippingOrder(cart,message);
        cartService.removeAllBooksFromCart(cart);
        return orderProcessedMessage(message);
    }

    private SendMessage orderProcessedMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        Integer userId = message.getFrom().getId();
        sendMessage.setText(localeMessageService.getMessage("successfulpayment.orderprocessed",userProfileService.getLocaleForUser(userId)));
        sendMessage.setReplyMarkup(viewService.getBasketView().generateKeyboard(userProfileService.getLocaleForUser(userId)));
        return sendMessage;
    }
}
