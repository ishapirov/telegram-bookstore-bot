package com.ishapirov.telegrambot.services.inputprocessing;

import com.ishapirov.telegrambot.services.precheckout.PreCheckoutService;
import com.ishapirov.telegrambot.services.shipping.ShippingOptionsService;
import com.ishapirov.telegrambot.services.successfulpayment.SuccessfulPaymentService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;

@Service
public class UserInputProcessorService {
    @Autowired
    ViewService viewService;
    @Autowired
    ShippingOptionsService shippingOptionsService;
    @Autowired
    PreCheckoutService preCheckoutService;
    @Autowired
    SuccessfulPaymentService successfulPaymentService;

    public BotApiMethod<?> handleUpdate(Update update){

        if(update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return handleCallback(callbackQuery);
        }
        if(update.hasMessage()){
            if(update.getMessage().hasSuccessfulPayment())
                return successfulPaymentService.handleSuccessfulPayment(update.getMessage());
            Message message = update.getMessage();
            return handleMessage(message);
        }
        if(update.hasShippingQuery()) {
            ShippingQuery shippingQuery = update.getShippingQuery();
            return shippingOptionsService.handleShippingQuery(shippingQuery);
        }
        if(update.hasPreCheckoutQuery()){
            PreCheckoutQuery preCheckoutQuery = update.getPreCheckoutQuery();
            return preCheckoutService.handlePreCheckoutQuery(preCheckoutQuery);
        }

        return null;
    }

    private BotApiMethod<?> handleCallback(CallbackQuery callbackQuery){
        if(callbackQuery.getData().equals("none"))
            return null;
        UserCallbackRequest userCallbackRequest = new UserCallbackRequest(callbackQuery);
        View viewUserClickedOn = viewService.getView(userCallbackRequest.getViewInWhichButtonWasClicked());
        View nextViewToRender = viewUserClickedOn.getNextView(userCallbackRequest);
        return nextViewToRender.generateMessage(userCallbackRequest);
    }

    private BotApiMethod<?> handleMessage(Message message){
        UserCallbackRequest userCallbackRequest = new UserCallbackRequest(message);
        View mainMenuView = viewService.getMainMenuView();
        return mainMenuView.generateMessage(userCallbackRequest);
    }

}
