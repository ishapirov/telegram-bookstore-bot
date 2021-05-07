package com.ishapirov.telegrambot.controllers.payment;

import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionRate;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.views.payment.dto.PaymentViewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentController {
    @Autowired
    CurrencyConversionService currencyConversionService;
    @Autowired
    CartService cartService;
    @Autowired
    UserProfileService userProfileService;

    public static final String TYPE_STRING = "payment";


    public PaymentViewInfo getPaymentInfo(PaymentControllerInfo paymentControllerInfo) {
        PaymentViewInfo paymentViewInfo = new PaymentViewInfo();
        Cart cart = cartService.getCart(paymentControllerInfo.getUserId());
        if(cart.getBooksInCart().size() == 0){
            paymentViewInfo.setCartEmpty(true);
            return paymentViewInfo;
        }
        paymentViewInfo.setCartEmpty(false);
        CurrencyConversionRate currencyConversionRate = currencyConversionService.getConversionRate(paymentControllerInfo.getUserId());
        paymentViewInfo.setTotalCost(cart.getTotalCartCost(currencyConversionRate));
        paymentViewInfo.setCurrency(currencyConversionRate.getCurrency().getCurrencyCode());
        paymentViewInfo.setLocaleString(userProfileService.getLocaleForUser(paymentControllerInfo.getUserId()));
        return paymentViewInfo;
    }

}
