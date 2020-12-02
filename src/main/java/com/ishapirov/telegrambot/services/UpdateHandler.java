package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateHandler {

    @Autowired
    ViewService viewService;

    public SendMessage handleUpdate(Update update){

        if(update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return handleCallback(callbackQuery);
        }
        if(update.hasMessage()){
            Message message = update.getMessage();
            return handleMessage(message);
        }
        return null;
    }

    private SendMessage handleCallback(CallbackQuery callbackQuery){
        UserCallbackRequest userCallbackRequest = new UserCallbackRequest(callbackQuery);
        View viewUserClickedOn = viewService.getView(userCallbackRequest.getViewInWhichButtonWasClicked());
        System.out.println("1 " + viewUserClickedOn.getTypeString());
        View nextViewToRender = viewUserClickedOn.getNextView(userCallbackRequest.getButtonClicked());
        System.out.println("2 " + nextViewToRender.getTypeString());
        SendMessage sendMessage = nextViewToRender.generateSendMessage();
        System.out.println("3 " + sendMessage);
        sendMessage.setChatId(userCallbackRequest.getChatId());
        return sendMessage;
    }

    private SendMessage handleMessage(Message message){
        View mainMenuView = viewService.getMainMenuView();
        SendMessage sendMessage = mainMenuView.generateSendMessage();
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }
}
