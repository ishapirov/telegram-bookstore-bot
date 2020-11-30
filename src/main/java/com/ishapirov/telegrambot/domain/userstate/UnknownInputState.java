package com.ishapirov.telegrambot.domain.views;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class UnknownInputState extends UserState{

    @Override
    public SendMessage generateSendMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard());
        userSession.setUserState(previousState);
        return sendMessage;
    }

    @Override
    public ReplyKeyboardMarkup generateKeyboard() {
        return previousState.generateKeyboard();
    }

    @Override
    public String generateText() {
        return "Неизвестный ввод. Пожалуйста, используйте кнопки на клавиатуре. Введите «Меню», чтобы вернуться в главное меню.";
    }

    @Override
    public void changeSessionStateBasedOnInput(String messageText,UserSession userSession) { }

    @Override
    public State getPossibleState() {
        return State.UNKNOWN_INPUT;
    }
}
