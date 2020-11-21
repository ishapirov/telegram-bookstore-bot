package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class StartState extends UserState{

    public StartState(UserSession userSession) {
        super(userSession);
    }

    @Override
    public SendMessage generateSendMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard());
        userSession.setUserState(new MainMenuState(userSession));
        return sendMessage;
    }

    @Override
    public ReplyKeyboardMarkup generateKeyboard() {
        return (new MainMenuState(userSession)).generateKeyboard();
    }

    @Override
    public String generateText() {
        return "Welcome to the bot";
    }

    @Override
    public void changeStateBasedOnInput(String messageText) { }
}
