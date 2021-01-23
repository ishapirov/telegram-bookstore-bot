package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;


public class CustomCallback extends CallbackQuery {
    private String data;
    private User user;
    private Message message;
    private String callbackId;

    public CustomCallback(String data, User user, Message message,String callbackId) {
        this.data = data;
        this.user = user;
        this.message = message;
        this.callbackId = callbackId;
    }

    public User getFrom(){
        return this.user;
    }

    public Message getMessage(){
        return this.message;
    }

    public String getData(){
        return this.data;
    }

    public String getId(){return this.callbackId;}
}
