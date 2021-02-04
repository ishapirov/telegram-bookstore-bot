package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;

public class CustomUpdate extends Update {
    private Message message;
    private CallbackQuery callbackQuery;
    private PreCheckoutQuery preCheckoutQuery;

    public CustomUpdate(Message message,CallbackQuery callbackQuery,PreCheckoutQuery preCheckoutQuery){
        this.message = message;
        this.callbackQuery = callbackQuery;
        this.preCheckoutQuery = preCheckoutQuery;
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

    @Override
    public boolean hasPreCheckoutQuery(){
        if(this.preCheckoutQuery == null)
            return false;
        return true;
    }

    @Override
    public PreCheckoutQuery getPreCheckoutQuery(){
        return this.preCheckoutQuery;
    }
}
