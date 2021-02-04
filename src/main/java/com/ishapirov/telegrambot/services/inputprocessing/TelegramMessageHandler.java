package com.ishapirov.telegrambot.services.inputprocessing;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.command.CommandService;
import com.ishapirov.telegrambot.services.precheckout.PreCheckoutService;
import com.ishapirov.telegrambot.services.shipping.ShippingOptionsService;
import com.ishapirov.telegrambot.services.successfulpayment.SuccessfulPaymentService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.TelegramView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;

@Service
public class TelegramMessageHandler {
    @Autowired
    ViewService viewService;
    @Autowired
    ShippingOptionsService shippingOptionsService;
    @Autowired
    PreCheckoutService preCheckoutService;
    @Autowired
    SuccessfulPaymentService successfulPaymentService;
    @Autowired
    CommandService commandService;

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
        ButtonAction buttonAction = ButtonAction.valueOf(userCallbackRequest.getAction());
        Object object = commandService.getCommand(buttonAction).execute(userCallbackRequest);
        TelegramView telegramView = viewService.getNextView(buttonAction);
        return telegramView.generateMessage(object, userCallbackRequest.getChatId(), userCallbackRequest.getMessageId(), userCallbackRequest.getCallBackId(), userCallbackRequest.isEditMessagePreferred());
    }

    private BotApiMethod<?> handleMessage(Message message){
        UserCallbackRequest userCallbackRequest = new UserCallbackRequest(message);
        TelegramView mainMenuTelegramView = viewService.getMainMenuView();
        return mainMenuTelegramView.generateMessage(commandService.getCommand(ButtonAction.GO_TO_MAIN_MENU).execute(userCallbackRequest),
                userCallbackRequest.getChatId(), userCallbackRequest.getMessageId(),userCallbackRequest.getCallBackId(), userCallbackRequest.isEditMessagePreferred());
    }

}
