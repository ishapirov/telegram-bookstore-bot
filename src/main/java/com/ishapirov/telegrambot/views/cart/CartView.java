package com.ishapirov.telegrambot.views.cart;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.views.TelegramView;
import com.ishapirov.telegrambot.views.dto.LocaleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Service
public class CartView implements TelegramView {
    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING = "basket";

    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId,String callbackId, boolean editMessagePreferred) {
        String locale = ((LocaleDTO)object).getLocaleString();
        if(editMessagePreferred){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(generateText(locale));
            editMessageText.setReplyMarkup(generateKeyboard(locale));
            return editMessageText;
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateText(locale));
            sendMessage.setReplyMarkup(generateKeyboard(locale));
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
    }


    public InlineKeyboardMarkup generateKeyboard(String locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonPay = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.basket.pay",locale));
        buttonPay.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.PAY,true));
        InlineKeyboardButton buttonViewRemove = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.basket.viewremove",locale));
        buttonViewRemove.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_VIEW_REMOVE,false));
        keyboardButtonsRow1.add(buttonPay);
        keyboardButtonsRow1.add(buttonViewRemove);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();

        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back",locale));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,true));
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }

    public String generateText(String locale) {
        return localeMessageService.getMessage("view.basket.generate",locale);
    }

}
