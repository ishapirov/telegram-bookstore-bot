package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.domain.book.KidBook;
import com.ishapirov.telegrambot.domain.book.KidBookCategory;
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
public class KidBooksSelectAgeView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;

    @Override
    protected InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonSixMonth = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.sixmonth"));
        buttonSixMonth.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), sixMonth(), KidBook.typeOfBook(),KidBookCategory.SIX_MONTHS_TO_EIGHTEEN_MONTHS.name()));
        InlineKeyboardButton buttonOneToThree = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.onetothree"));
        buttonOneToThree.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), oneToThree(),KidBook.typeOfBook(),KidBookCategory.ONE_TO_THREE.name()));
        keyboardButtonsRow1.add(buttonSixMonth);
        keyboardButtonsRow1.add(buttonOneToThree);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonThreeToFive= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.threetofive"));
        buttonThreeToFive.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), threeToFive(), KidBook.typeOfBook(),KidBookCategory.THREE_YEARS_TO_FIVE_YEARS.name()));
        InlineKeyboardButton buttonFiveToSeven= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.fivetoseven"));
        buttonFiveToSeven.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), fiveToSeven(),KidBook.typeOfBook(),KidBookCategory.FIVE_YEARS_TO_SEVEN_YEARS.name()));
        keyboardButtonsRow2.add(buttonThreeToFive);
        keyboardButtonsRow2.add(buttonFiveToSeven);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        InlineKeyboardButton buttonAll = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.all"));
        buttonAll.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilter(getTypeString(), allBooks(),KidBook.typeOfBook(),"all"));
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), backText(),true));
        keyboardButtonsRow3.add(buttonAll);
        keyboardButtonsRow3.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public String sixMonth(){
        return "sixmonth";
    }

    public String oneToThree(){
        return "onetothree";
    }

    public String threeToFive(){
        return "threetofive";
    }

    public String fiveToSeven(){
        return "fivetoseven";
    }

    public String allBooks(){
        return "all";
    }

    public String backText(){
        return "back";
    }


    @Override
    protected String generateText() {
        return localeMessageService.getMessage("view.kidselect.generate");
    }

    @Override
    public View getNextView(UserCallbackRequest userCallbackRequest) {
        String messageText = userCallbackRequest.getButtonClicked();
        if(messageText.equals(sixMonth()) || messageText.equals(oneToThree()) || messageText.equals(threeToFive()) || messageText.equals(fiveToSeven()) || messageText.equals(allBooks()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(viewService.getMainMenuView().getTypeString()) || messageText.equals(backText()))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "kidselect";
    }
}
