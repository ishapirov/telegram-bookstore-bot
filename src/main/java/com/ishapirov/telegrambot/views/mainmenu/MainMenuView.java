package com.ishapirov.telegrambot.views.mainmenu;

import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.View;
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
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    LocaleMessageService localeMessageService;

    @Override
    public InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonCatalog = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.catalog"));
        buttonCatalog.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),catalogText()));
        InlineKeyboardButton buttonBasket = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.basket"));
        buttonBasket.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),basketText()));
        keyboardButtonsRow1.add(buttonCatalog);
        keyboardButtonsRow1.add(buttonBasket);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonManager = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.manager"));
        buttonManager.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),managerText()));
        InlineKeyboardButton buttonCurrency = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.currency") +" (" + userProfileService.getUserProfile(userCallbackRequest.getUserId()).getCurrency() + ")");
        buttonCurrency.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),currencyText()));
        keyboardButtonsRow2.add(buttonManager);
        keyboardButtonsRow2.add(buttonCurrency);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        InlineKeyboardButton buttonOrders = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.orders"));
        buttonOrders.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),ordersText()));
        keyboardButtonsRow3.add(buttonOrders);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String generateText(){
        return localeMessageService.getMessage("view.mainmenu.generate");
    }

    public String catalogText(){
        return "catalog";
    }

    public String basketText(){
        return "basket";
    }

    public String managerText(){
        return "manager";
    }

    public String currencyText(){
        return "currency";
    }

    private String ordersText() {
        return "orders";
    }

    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(getTypeString()) || messageText.equals("back"))
            return viewService.getMainMenuView();
        else if(messageText.equals(catalogText()))
            return viewService.getCatalogMenuView();
        else if(messageText.equals(basketText()))
            return viewService.getBasketView();
        else if(messageText.equals(managerText()))
            return viewService.getManagerContactInformationView();
        else if(messageText.startsWith(currencyText()))
            return viewService.getCurrencySelectionView();
        else if(messageText.startsWith(ordersText()))
            return viewService.getBooksOrderedView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "mainmenu";
    }
}
