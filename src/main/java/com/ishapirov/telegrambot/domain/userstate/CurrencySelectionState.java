package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Service
public class CurrencySelectionState extends UserState{

    @Override
    public ReplyKeyboardMarkup generateKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("USD"));
        row1.add(new KeyboardButton("Сум"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Рубли"));
        row2.add(new KeyboardButton("Назад"));

        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "выберите валюту, которую вы хотите использовать";
    }

    @Override
    public void changeSessionStateBasedOnInput(String messageText,UserSession userSession) {
        if (messageText.equals("Usd") || messageText.equals("Сум") || messageText.equals("Рубли")) {
            userSession.setCurrency(getCurrencyFromString(messageText));
            userSession.setUserState(new MainMenuState(userSession));
        } else if (messageText.equals("Меню") || messageText.equals("Назад"))
            userSession.setUserState(new MainMenuState(userSession));
        else userSession.setUserState(new UnknownInputState(userSession,this));
    }

    public Currency getCurrencyFromString(String currency){
        if(currency.equals("Сум"))
            return Currency.getInstance("UZS");
        if(currency.equals("Рубли"))
            return Currency.getInstance("RUB");
        return Currency.getInstance("USD");
    }

    @Override
    public State getPossibleState() {
        return State.CURRENCY_SELECTION;
    }

}
