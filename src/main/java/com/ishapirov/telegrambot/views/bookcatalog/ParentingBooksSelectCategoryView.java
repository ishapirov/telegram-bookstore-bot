package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.domain.book.ParentingBook;
import com.ishapirov.telegrambot.domain.book.ParentingBookCategory;
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
public class ParentingBooksSelectCategoryView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;

    @Override
    protected InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonUpbringing = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.upbringing"));
        buttonUpbringing.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), upbringing(), ParentingBook.typeOfBook(), ParentingBookCategory.UPBRINGING.name()));
        InlineKeyboardButton buttonPsychology = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.psychology"));
        buttonPsychology.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), psychology(),ParentingBook.typeOfBook(),ParentingBookCategory.PSYCHOLOGY.name()));
        keyboardButtonsRow1.add(buttonUpbringing);
        keyboardButtonsRow1.add(buttonPsychology);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonInspiration= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.inspiration"));
        buttonInspiration.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), inspiration(),ParentingBook.typeOfBook(),ParentingBookCategory.INSPIRATION.name()));
        InlineKeyboardButton buttonAll= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.parentingselect.all"));
        buttonAll.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), allBooks(),ParentingBook.typeOfBook(),"all"));
        keyboardButtonsRow2.add(buttonInspiration);
        keyboardButtonsRow2.add(buttonAll);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), backText(),true));
        keyboardButtonsRow3.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String upbringing(){
        return "upbringing";
    }

    public String psychology(){
        return "psychology";
    }

    public String inspiration(){
        return "inspiration";
    }

    public String allBooks(){
        return "all";
    }

    public String backText(){
        return "back";
    }

    @Override
    protected String generateText() {
        return localeMessageService.getMessage("view.parentingselect.generate");
    }

    @Override
    public View getNextView(UserCallbackRequest userCallbackRequest) {
        String messageText = userCallbackRequest.getButtonClicked();
        if(messageText.equals(upbringing()) || messageText.equals(psychology()) || messageText.equals(inspiration()) || messageText.equals(allBooks()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(viewService.getMainMenuView().getTypeString()) || messageText.equals(backText()))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "parentingselect";
    }
}
