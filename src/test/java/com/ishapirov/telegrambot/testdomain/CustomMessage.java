package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

public class CustomMessage extends Message {
    private Long chatId;
    private Integer messageId;
    private User user;
    private SuccessfulPayment successfulPayment;

    public CustomMessage(long chatId,int messageId,User user,SuccessfulPayment successfulPayment) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.user =user;
        this.successfulPayment = successfulPayment;
    }

    @Override
    public Long getChatId() {
        return this.chatId;
    }

    @Override
    public Integer getMessageId(){
        return this.messageId;
    }

    @Override
    public boolean hasSuccessfulPayment() {
        if(this.successfulPayment == null)
            return false;
        return true;
    }

    @Override
    public SuccessfulPayment getSuccessfulPayment() {
        return this.successfulPayment;
    }

    @Override
    public User getFrom() {
        return this.user;
    }
}
