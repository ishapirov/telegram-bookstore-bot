package com.ishapirov.telegrambot.views.bookcatalog;

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
public class CatalogMenuView implements TelegramView {
    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING = "catalogmenu";

    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId, String callbackId, boolean editMessagePreferred) {
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
        InlineKeyboardButton buttonKidBooks = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.kids",locale));
        buttonKidBooks.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_KIDS_CATEGORIES,true));
        InlineKeyboardButton buttonMomBooks = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.moms",locale));
        buttonMomBooks.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_PARENTING_CATEGORIES,true));
        keyboardButtonsRow1.add(buttonKidBooks);
        keyboardButtonsRow1.add(buttonMomBooks);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonCatalog= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.catalogMenu.catalog",locale));
        buttonCatalog.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BOOK_CATALOG,true));
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back",locale));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,true));
        keyboardButtonsRow2.add(buttonCatalog);
        keyboardButtonsRow2.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String generateText(String locale) {
        return localeMessageService.getMessage("view.catalogMenu.generate",locale);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
