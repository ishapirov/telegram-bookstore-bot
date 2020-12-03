package com.ishapirov.telegrambot.views;

import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public abstract class View {

    public SendMessage generateSendMessage(UserCallbackRequest userCallbackRequest){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard(userCallbackRequest));
        return sendMessage;
    }

    public abstract String getTypeString();
    protected abstract InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest);
    protected abstract String generateText();
    public abstract View getNextView(String messageText, UserCallbackRequest userCallbackRequest);

}