package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.domain.book.KidBook;
import com.ishapirov.telegrambot.domain.book.KidBookCategory;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.view.ViewService;
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
public class KidBooksSelectAgeView implements TelegramView {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING = "kidselect";

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
        InlineKeyboardButton buttonSixMonth = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.sixmonth"));
        buttonSixMonth.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG, KidBook.typeOfBook(),KidBookCategory.SIX_MONTHS_TO_EIGHTEEN_MONTHS.name()));
        InlineKeyboardButton buttonOneToThree = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.onetothree"));
        buttonOneToThree.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG,KidBook.typeOfBook(),KidBookCategory.ONE_TO_THREE.name()));
        keyboardButtonsRow1.add(buttonSixMonth);
        keyboardButtonsRow1.add(buttonOneToThree);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonThreeToFive= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.threetofive"));
        buttonThreeToFive.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG, KidBook.typeOfBook(),KidBookCategory.THREE_YEARS_TO_FIVE_YEARS.name()));
        InlineKeyboardButton buttonFiveToSeven= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.fivetoseven"));
        buttonFiveToSeven.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG,KidBook.typeOfBook(),KidBookCategory.FIVE_YEARS_TO_SEVEN_YEARS.name()));
        keyboardButtonsRow2.add(buttonThreeToFive);
        keyboardButtonsRow2.add(buttonFiveToSeven);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        InlineKeyboardButton buttonAll = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.all"));
        buttonAll.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(ButtonAction.GO_TO_BOOK_CATALOG,KidBook.typeOfBook(),"all"));
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_CATALOG_MENU,true));
        keyboardButtonsRow3.add(buttonAll);
        keyboardButtonsRow3.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    protected String generateText() {
        return localeMessageService.getMessage("view.kidselect.generate");
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
