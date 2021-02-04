package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;

public class CustomPreCheckoutQuery extends PreCheckoutQuery {
    private String id;
    private User user;

    public CustomPreCheckoutQuery(String id,User user){
        this.id = id;
        this.user = user;
    }

    public User getFrom(){
        return this.user;
    }

    public String getId(){
        return this.id;
    }
}
