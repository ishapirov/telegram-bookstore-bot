package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public abstract class UserState {
    protected UserSession userSession;

    public UserState(UserSession userSession){
        this.userSession = userSession;
    }

    public SendMessage generateSendMessage(){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard());
        return sendMessage;
    }

    public abstract ReplyKeyboardMarkup generateKeyboard();
    public abstract String generateText();
    public abstract void changeStateBasedOnInput(String messageText);


}
