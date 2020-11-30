package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


@Service
public class BasketState extends UserState{

    @Override
    public State getState() {
        return State.BASKET;
    }

    @Override
    public ReplyKeyboardMarkup generateKeyboard(UserSession userSession) {
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
    public void changeSessionStateBasedOnInput(String messageText, UserSession userSession) {
        if(messageText.equals("Меню") || messageText.equals("Назад")){
            userSession.setState(State.MAIN_MENU);
        } else
            userSession.setState(new UnknownInputState(userSession,this));
    }


}
