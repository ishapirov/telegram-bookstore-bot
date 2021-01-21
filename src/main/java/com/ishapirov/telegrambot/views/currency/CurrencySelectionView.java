package com.ishapirov.telegrambot.views.currency;

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
import java.util.Currency;
import java.util.List;

@Service
public class CurrencySelectionView extends View {
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
        InlineKeyboardButton buttonUSD = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.currency.usd"));
        buttonUSD.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), usdText(),true));
        InlineKeyboardButton buttonSUM = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.currency.sum"));
        buttonSUM.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), sumText(),true));
        keyboardButtonsRow1.add(buttonUSD);
        keyboardButtonsRow1.add(buttonSUM);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonRUB = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.currency.rub"));
        buttonRUB.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), rubText(),true));
        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), backText(),true));
        keyboardButtonsRow2.add(buttonRUB);
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String usdText(){
        return "usd";
    }
    public String sumText(){
        return "sum";
    }
    public String rubText(){
        return "rub";
    }
    public String backText(){
        return "back";
    }

    @Override
    public String generateText() {
        return localeMessageService.getMessage("view.currency.generate");
    }

    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        System.out.println(messageText);
        if (messageText.equals(usdText()) || messageText.equals(sumText()) || messageText.equals(rubText())) {
            userProfileService.setCurrencyForUser(userCallbackRequest.getUserId(), getCurrencyFromString(messageText));
            return viewService.getMainMenuView();
        } else if(messageText.equals(viewService.getMainMenuView().getTypeString()) || messageText.equals(backText()))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    public Currency getCurrencyFromString(String currency){
        if(currency.equals(sumText()))
            return Currency.getInstance("UZS");
        if(currency.equals(rubText()))
            return Currency.getInstance("RUB");
        return Currency.getInstance("USD");
    }

    @Override
    public String getTypeString() {
        return "currencyselection";
    }
}
