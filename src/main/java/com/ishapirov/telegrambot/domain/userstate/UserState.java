package com.ishapirov.telegrambot.domain.views;

import com.ishapirov.telegrambot.domain.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public abstract class UserState {

    public SendMessage generateSendMessage(UserSession userSession){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard());
        return sendMessage;
    }

    public abstract State getState();
    public abstract ReplyKeyboardMarkup generateKeyboard(UserSession userSession);
    public abstract String generateText();
    public abstract void changeSessionStateBasedOnInput(String messageText, UserSession userSession);

}
