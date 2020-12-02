package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.User;

public class CustomUser extends User {
    private Integer id;

    public CustomUser(int id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
