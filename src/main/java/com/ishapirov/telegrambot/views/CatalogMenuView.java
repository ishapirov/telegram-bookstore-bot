package com.ishapirov.telegrambot.views;

import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.LocaleMessageService;
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
    @Autowired
    LocaleMessageService localeMessageService;

    @Override
    public InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonKidBooks = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.kids"));
        buttonKidBooks.setCallbackData(getTypeString() + "-" + getKidsText());
        InlineKeyboardButton buttonMomBooks = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.moms"));
        buttonMomBooks.setCallbackData(getTypeString() + "-" + getMomsText());
        keyboardButtonsRow1.add(buttonKidBooks);
        keyboardButtonsRow1.add(buttonMomBooks);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonCatalog= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.catalog"));
        buttonCatalog.setCallbackData(getTypeString() + "-" + getCatalogText());
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(getTypeString() + "-" + getBackText());
        keyboardButtonsRow2.add(buttonCatalog);
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String getKidsText(){
        return "kids";
    }

    public String getMomsText(){
        return "moms";
    }
    public String getCatalogText(){
        return "catalog";
    }

    public String getBackText(){
        return "back";
    }

    @Override
    public String generateText() {
        return localeMessageService.getMessage("view.catalogMenu.generate");
    }

    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(getKidsText()))
            return viewService.getMainMenuView();
        else if(messageText.equals(getMomsText()))
            return viewService.getMainMenuView();
        else if(messageText.equals(getCatalogText()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(viewService.getMainMenuView().getTypeString()) || messageText.equals(getBackText()))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "catalogmenu";
    }
}
