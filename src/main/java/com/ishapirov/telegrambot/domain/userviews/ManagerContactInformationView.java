package com.ishapirov.telegrambot.domain.userviews;

import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerContactInformationView extends View {
    @Autowired
    ViewService viewService;

    @Override
    public InlineKeyboardMarkup generateKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText("Назад");
        buttonBack.setCallbackData(getTypeString() + "-back");
        keyboardButtonsRow1.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "Some contact information here";
    }

    @Override
    public View getNextView(String messageText) {
        if(messageText.equals("Меню") || messageText.equals("Назад"))
            return viewService.getMainMenuView();
        else
            throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "managercontactinformation";
    }
}
