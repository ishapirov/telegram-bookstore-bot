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
public class CatalogMenuView extends View {
    @Autowired
    ViewService viewService;

    @Override
    public InlineKeyboardMarkup generateKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonKidBooks = new InlineKeyboardButton().setText("Подборка книг для детей");
        buttonKidBooks.setCallbackData(getTypeString() + "-kids");
        InlineKeyboardButton buttonMomBooks = new InlineKeyboardButton().setText("Книги для мам");
        buttonMomBooks.setCallbackData(getTypeString() + "-back");
        keyboardButtonsRow1.add(buttonKidBooks);
        keyboardButtonsRow1.add(buttonMomBooks);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonCatalog= new InlineKeyboardButton().setText("Каталог книг");
        buttonCatalog.setCallbackData(getTypeString() + "-catalog");
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText("Назад");
        buttonBack  .setCallbackData(getTypeString() + "-back");
        keyboardButtonsRow2.add(buttonCatalog);
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "Каталог";
    }

    @Override
    public View getNextView(String messageText) {
        if(messageText.equals("Подборка книг для детей"))
            return viewService.getMainMenuView();
        else if(messageText.equals("Книги для мам"))
            return viewService.getMainMenuView();
        else if(messageText.equals("Каталог книг"))
            return viewService.getBookCatalogView();
        else if(messageText.equals("Меню") || messageText.equals("Назад"))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "catalogmenu";
    }
}
