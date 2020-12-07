package com.ishapirov.telegrambot.views.bookcatalog.kidbooks;

import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.LocaleMessageService;
import com.ishapirov.telegrambot.services.ViewService;
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
        buttonSixMonth.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), sixMonth()));
        InlineKeyboardButton buttonOneToThree = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.onetothree"));
        buttonOneToThree.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), oneToThree()));
        keyboardButtonsRow1.add(buttonSixMonth);
        keyboardButtonsRow1.add(buttonOneToThree);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonThreeToFive= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.threetofive"));
        buttonThreeToFive.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), threeToFive()));
        InlineKeyboardButton buttonFiveToSeven= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.fivetoseven"));
        buttonFiveToSeven.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), fiveToSeven()));
        keyboardButtonsRow2.add(buttonThreeToFive);
        keyboardButtonsRow2.add(buttonFiveToSeven);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        InlineKeyboardButton buttonAll = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.kidselect.all"));
        buttonAll.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), allBooks()));
        InlineKeyboardButton buttonBack= new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), backText()));
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
    public View getNextView(String messageText, UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(sixMonth()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(oneToThree()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(threeToFive()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(fiveToSeven()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(allBooks()))
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
