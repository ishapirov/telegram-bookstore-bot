package com.ishapirov.telegrambot.domain.userviews;

import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Service
public class CurrencySelectionView extends View {
    @Autowired
    ViewService viewService;

    @Override
    public InlineKeyboardMarkup generateKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonUSD = new InlineKeyboardButton().setText("USD");
        buttonUSD.setCallbackData(getTypeString() + "-usd");
        InlineKeyboardButton buttonSUM = new InlineKeyboardButton().setText("Сум");
        buttonSUM.setCallbackData(getTypeString() + "-sum");
        keyboardButtonsRow1.add(buttonUSD);
        keyboardButtonsRow1.add(buttonSUM);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonRUB = new InlineKeyboardButton().setText("Рубли");
        buttonRUB.setCallbackData(getTypeString() + "-rub");
        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText("Назад");
        buttonBack.setCallbackData(getTypeString() + "-back");
        keyboardButtonsRow2.add(buttonRUB);
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "выберите валюту, которую вы хотите использовать";
    }

    @Override
    public View getNextView(String messageText) {
        if (messageText.equals("Usd") || messageText.equals("Сум") || messageText.equals("Рубли")) {
            //userSession.setCurrency(getCurrencyFromString(messageText));
            return viewService.getMainMenuView();
        } else if (messageText.equals("Меню") || messageText.equals("Назад"))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    public Currency getCurrencyFromString(String currency){
        if(currency.equals("Сум"))
            return Currency.getInstance("UZS");
        if(currency.equals("Рубли"))
            return Currency.getInstance("RUB");
        return Currency.getInstance("USD");
    }

    @Override
    public String getTypeString() {
        return "currencyselection";
    }
}
