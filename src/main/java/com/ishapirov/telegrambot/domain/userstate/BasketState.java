package com.ishapirov.telegrambot.domain.views;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


@Service
public class BasketState extends UserState{

    @Override
    public State getState() {
        return State.BASKET;
    }

    @Override
    public ReplyKeyboardMarkup generateKeyboard(UserSession userSession) {
        return getKeyboard().generateKeyboard(userSession);
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
