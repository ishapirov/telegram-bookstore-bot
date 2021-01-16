package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.View;
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
        buttonKidBooks.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), kidsText()));
        InlineKeyboardButton buttonMomBooks = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.moms"));
        buttonMomBooks.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), momsText()));
        keyboardButtonsRow1.add(buttonKidBooks);
        keyboardButtonsRow1.add(buttonMomBooks);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonCatalog= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.catalog"));
        buttonCatalog.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), catalogText()));
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), backText()));
        keyboardButtonsRow2.add(buttonCatalog);
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String kidsText(){
        return "kids";
    }
    public String momsText(){
        return "moms";
    }
    public String catalogText(){
        return "catalog";
    }
    public String backText(){
        return "back";
    }

    @Override
    public String generateText() {
        return localeMessageService.getMessage("view.catalogMenu.generate");
    }

    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(kidsText()))
            return viewService.getKidBooksSelectAgeView();
        else if(messageText.equals(momsText()))
            return viewService.getParentingBooksSelectCategoryView();
        else if(messageText.equals(catalogText()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(viewService.getMainMenuView().getTypeString()) || messageText.equals(backText()))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "catalogmenu";
    }
}
