package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class CurrencySelectionState extends UserState{
    public CurrencySelectionState(UserSession userSession) {
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
    public void changeStateBasedOnInput(String messageText) {
        if (messageText.equals("Usd") || messageText.equals("Сум") || messageText.equals("Рубли")) {
            userSession.setCurrency(getCurrencyFromString(messageText));
            this.userSession.setUserState(new MainMenuState(userSession));
        } else if (messageText.equals("Меню") || messageText.equals("Назад"))
            this.userSession.setUserState(new MainMenuState(userSession));
        else this.userSession.setUserState(new UnknownInputState(userSession,this));
    }

    public Currency getCurrencyFromString(String currency){
        if(currency.equals("Сум"))
            return Currency.getInstance("UZS");
        if(currency.equals("Рубли"))
            return Currency.getInstance("RUB");
        return Currency.getInstance("USD");
    }

}
