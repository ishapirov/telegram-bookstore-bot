package com.ishapirov.telegrambot.domain.views;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogMenuState extends UserState{

    @Override
    public ReplyKeyboardMarkup generateKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Подборка книг для детей"));
        row1.add(new KeyboardButton("Книги для мам"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Каталог книг"));
        row2.add(new KeyboardButton("Назад"));

        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "Каталог";
    }

    @Override
    public void changeSessionStateBasedOnInput(String messageText,UserSession userSession) {
            if(messageText.equals("Подборка книг для детей"))
                userSession.setUserState(new MainMenuState(userSession));
            else if(messageText.equals("Книги для мам"))
                userSession.setUserState(new MainMenuState(userSession));
            else if(messageText.equals("Каталог книг"))
                userSession.setUserState(new BookCatalogState(userSession));
            else if(messageText.equals("Меню") || messageText.equals("Назад"))
                userSession.setUserState(new MainMenuState(userSession));
            else userSession.setUserState(new UnknownInputState(userSession,this));
    }

    @Override
    public State getPossibleState() {
        return State.CATALOG_MENU;
    }
}
