package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class CatalogMenuState extends UserState{
    public CatalogMenuState(UserSession userSession) {
        super(userSession);
    }

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
    public void changeStateBasedOnInput(String messageText) {
            if(messageText.equals("Подборка книг для детей"))
                this.userSession.setUserState(new MainMenuState(userSession));
            else if(messageText.equals("Книги для мам"))
                this.userSession.setUserState(new MainMenuState(userSession));
            else if(messageText.equals("Каталог книг"))
                this.userSession.setUserState(new BookCatalogState(userSession));
            else if(messageText.equals("Меню") || messageText.equals("Назад"))
                this.userSession.setUserState(new MainMenuState(userSession));
            else this.userSession.setUserState(new UnknownInputState(userSession,this));
    }

}
