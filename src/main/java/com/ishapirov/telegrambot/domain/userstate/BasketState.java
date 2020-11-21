package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class BasketState extends UserState{
    public BasketState(UserSession userSession) {
        super(userSession);
    }

    @Override
    public ReplyKeyboardMarkup generateKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Назад"));

        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "Ваша Корзина";
    }

    @Override
    public void changeStateBasedOnInput(String messageText) {
        if(messageText.equals("Меню") || messageText.equals("Назад")){
            this.userSession.setUserState(new MainMenuState(userSession));
        } else
            this.userSession.setUserState(new UnknownInputState(userSession,this));
    }


}
