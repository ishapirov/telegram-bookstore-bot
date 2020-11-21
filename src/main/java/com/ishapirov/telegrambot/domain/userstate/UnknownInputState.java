package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class UnknownInputState extends UserState{

    UserState previousState;
    public UnknownInputState(UserSession userSession,UserState previousState) {
        super(userSession);
        this.previousState = previousState;
    }

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
    public void changeStateBasedOnInput(String messageText) { }
}
