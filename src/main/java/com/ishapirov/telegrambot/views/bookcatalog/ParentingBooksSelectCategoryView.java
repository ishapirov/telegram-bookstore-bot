package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.domain.book.ParentingBook;
import com.ishapirov.telegrambot.domain.book.ParentingBookCategory;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.views.TelegramView;
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
public class ParentingBooksSelectCategoryView implements TelegramView {
    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING = "parentingselect";


    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId, String callbackId, boolean editMessagePreferred) {
        if(editMessagePreferred){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(generateText());
            editMessageText.setReplyMarkup(generateKeyboard());
            return editMessageText;
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateText());
            sendMessage.setReplyMarkup(generateKeyboard());
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
    }

    protected InlineKeyboardMarkup generateKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonUpbringing = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.upbringing"));
        buttonUpbringing.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG, ParentingBook.typeOfBook(), ParentingBookCategory.UPBRINGING.name()));
        InlineKeyboardButton buttonPsychology = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.psychology"));
        buttonPsychology.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG,ParentingBook.typeOfBook(),ParentingBookCategory.PSYCHOLOGY.name()));
        keyboardButtonsRow1.add(buttonUpbringing);
        keyboardButtonsRow1.add(buttonPsychology);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonInspiration= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.inspiration"));
        buttonInspiration.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG,ParentingBook.typeOfBook(),ParentingBookCategory.INSPIRATION.name()));
        InlineKeyboardButton buttonAll= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.all"));
        buttonAll.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG,ParentingBook.typeOfBook(),"all"));
        keyboardButtonsRow2.add(buttonInspiration);
        keyboardButtonsRow2.add(buttonAll);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_CATALOG_MENU,true));
        keyboardButtonsRow3.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    protected String generateText() {
        return localeMessageService.getMessage("view.parentingselect.generate");
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
