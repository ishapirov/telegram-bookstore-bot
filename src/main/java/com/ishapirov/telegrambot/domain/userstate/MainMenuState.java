package com.ishapirov.telegrambot.domain.views;

import com.ishapirov.telegrambot.domain.UserSession;
import com.ishapirov.telegrambot.domain.keyboard.Keyboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class MainMenuState extends UserState{

    public MainMenuState(@Autowired @Qualifier("basketKeyboard") Keyboard keyboard) {
        super(keyboard);
    }

    public ReplyKeyboardMarkup generateKeyboard(){
       keyboard.generateKeyboard();
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
