package com.ishapirov.telegrambot.domain.userviews;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public abstract class View {

    public SendMessage generateSendMessage(){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard());
        return sendMessage;
    }

    public abstract String getTypeString();
    protected abstract InlineKeyboardMarkup generateKeyboard();
    protected abstract String generateText();
    public abstract View getNextView(String messageText);

}
