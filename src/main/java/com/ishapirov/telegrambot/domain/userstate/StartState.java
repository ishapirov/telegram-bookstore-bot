package com.ishapirov.telegrambot.domain.views;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class StartState extends UserState{

    @Override
    public SendMessage generateSendMessage(UserSession userSession) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard());
        userSession.setState(State.MAIN_MENU);
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
    public void changeSessionStateBasedOnInput(String messageText,UserSession userSession) { }

    @Override
    public State getState() {
        return State.STATE;
    }
}
