package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CustomUpdate extends Update {
    private Message message;
    private CallbackQuery callbackQuery;

    public CustomUpdate(Message message,CallbackQuery callbackQuery){
        this.message = message;
        this.callbackQuery = callbackQuery;
    }

    @Override
    public boolean hasCallbackQuery() {
        if(this.callbackQuery == null)
            return false;
        else return true;
    }

    @Override
    public boolean hasMessage() {
        if(this.message == null)
            return false;
        else return true;
    }

    @Override
    public CallbackQuery getCallbackQuery(){
        return this.callbackQuery;
    }

    @Override
    public Message getMessage(){
        return this.message;
    }
}
