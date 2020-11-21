package com.ishapirov.telegrambot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateHandler {

    @Autowired MessageCreator messageCreator;

    public SendMessage handleUpdate(Update update){

        if(update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return handleCallback(callbackQuery);
        }
        if(update.hasMessage()){
            Message message = update.getMessage();
            return messageCreator.getSendMessage(message);
        }
        return null;
    }

    private SendMessage handleCallback(CallbackQuery callbackQuery){
        long chatId = callbackQuery.getMessage().getChatId();
        int userId = callbackQuery.getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(callbackQuery.getData());
        return sendMessage;
    }
}
