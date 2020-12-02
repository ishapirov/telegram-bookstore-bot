package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.Message;

public class CustomMessage extends Message {
    private Long chatId;

    public CustomMessage(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public Long getChatId() {
        return chatId;
    }

}
