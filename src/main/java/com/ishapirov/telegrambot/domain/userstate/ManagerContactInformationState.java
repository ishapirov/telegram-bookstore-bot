package com.ishapirov.telegrambot.domain.views;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerContactInformationState extends UserState{

    @Override
    public ReplyKeyboardMarkup generateKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Назад"));

        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "Some contact information here";
    }

    @Override
    public void changeSessionStateBasedOnInput(String messageText,UserSession userSession) {
        if(messageText.equals("Меню") || messageText.equals("Назад"))
            userSession.setUserState(new MainMenuState(userSession));
        else
           userSession.setUserState(new UnknownInputState(userSession,this));
    }

    @Override
    public State getPossibleState() {
        return State.MANAGER_CONTACT_INFORMATION;
    }
}
