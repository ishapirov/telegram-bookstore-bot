package com.ishapirov.telegrambot.domain.userviews;

import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainMenuView extends View {
    @Autowired
    ViewService viewService;

    @Override
    public InlineKeyboardMarkup generateKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonCatalog = new InlineKeyboardButton().setText("Подборка книг для детей");
        buttonCatalog.setCallbackData(getTypeString() + "-catalog");
        InlineKeyboardButton buttonBasket = new InlineKeyboardButton().setText("Корзина");
        buttonBasket.setCallbackData(getTypeString() + "-basket");
        keyboardButtonsRow1.add(buttonCatalog);
        keyboardButtonsRow1.add(buttonBasket);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonManager = new InlineKeyboardButton().setText("Связь с менеджером");
        buttonManager.setCallbackData(getTypeString() + "-manager");
        InlineKeyboardButton buttonCurrency = new InlineKeyboardButton().setText("Выбор валюты");
        buttonCurrency.setCallbackData(getTypeString() + "-basket");
        keyboardButtonsRow2.add(buttonManager);
        keyboardButtonsRow2.add(buttonCurrency);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String generateText(){
        return "Главное Меню";
    }

    @Override
    public View getNextView(String messageText) {
        if(messageText.equals("Меню") || messageText.equals("Назад"))
            return viewService.getMainMenuView();
        else if(messageText.equals("Каталог"))
            return viewService.getCatalogMenuView();
        else if(messageText.equals("Корзина"))
            return viewService.getBasketView();
        else if(messageText.equals("Связь с менеджером"))
            return viewService.getManagerContactInformationView();
        else if(messageText.startsWith("Выбор валюты"))
            return viewService.getCurrencySelectionView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "mainmenu";
    }
}
