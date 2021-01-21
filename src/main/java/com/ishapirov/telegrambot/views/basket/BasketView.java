package com.ishapirov.telegrambot.views.basket;

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
public class BasketView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;

    @Override
    public InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonPay = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.basket.pay"));
        buttonPay.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),payText(),true));
        InlineKeyboardButton buttonViewRemove = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.basket.viewremove"));
        buttonViewRemove.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),viewRemoveText(),false));
        keyboardButtonsRow1.add(buttonPay);
        keyboardButtonsRow1.add(buttonViewRemove);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),backText(),true));
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(viewRemoveText()))
            return viewService.getViewAndEditBooksInBasketView();
        else if(messageText.equals(payText()))
            return viewService.getPaymentView();
        else if(messageText.equals(viewService.getMainMenuView().getTypeString()) || messageText.equals(backText()))
            return viewService.getMainMenuView();
        else
            throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "basket";
    }

    @Override
    public String generateText() {
        return localeMessageService.getMessage("view.basket.generate");
    }

    public String payText(){
        return "pay";
    }
    public String viewRemoveText(){
        return "viewremove";
    }
    public String backText(){
        return "back";
    }
}
