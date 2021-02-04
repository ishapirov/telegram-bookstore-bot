package com.ishapirov.telegrambot.views;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface TelegramView {

    BotApiMethod<?> generateMessage(Object object,long chatId, int messageId,String callbackId,boolean editMessagePreferred);
    String getTypeString();

}
