package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainMenuState extends UserState{

    public ReplyKeyboardMarkup generateKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Каталог"));
        row1.add(new KeyboardButton("Корзина"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Связь с менеджером"));
        row2.add(new KeyboardButton("Выбор валюты "+"(" + userSession.getCurrency().toString() + ")"));

        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public String generateText(){
        return "Главное Меню";
    }

    @Override
    public void changeSessionStateBasedOnInput(String messageText,UserSession userSession) {
        if(messageText.equals("Меню") || messageText.equals("Назад"))
            userSession.setUserState(new MainMenuState(userSession));
        else if(messageText.equals("Каталог"))
            userSession.setUserState(new CatalogMenuState(userSession));
        else if(messageText.equals("Корзина"))
            userSession.setUserState(new BasketState(userSession));
        else if(messageText.equals("Связь с менеджером"))
            userSession.setUserState(new ManagerContactInformationState(userSession));
        else if(messageText.startsWith("Выбор валюты"))
            userSession.setUserState(new CurrencySelectionState(userSession));
        else userSession.setUserState(new UnknownInputState(userSession,this));
    }

    @Override
    public State getState() {
        return State.MAIN_MENU;
    }

}
