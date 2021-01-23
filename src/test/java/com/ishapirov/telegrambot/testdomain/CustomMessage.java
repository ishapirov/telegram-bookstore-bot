package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.Message;

public class CustomMessage extends Message {
    private Long chatId;
    private Integer messageId;

    public CustomMessage(long chatId,int messageId) {
        this.chatId = chatId;
        this.messageId = messageId;
    }

    @Override
    public Long getChatId() {
        return this.chatId;
    }

    @Override
    public Integer getMessageId(){
        return this.messageId;
    }

}
