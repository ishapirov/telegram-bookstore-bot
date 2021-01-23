package com.ishapirov.telegrambot.views;

import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public abstract class View {

    public BotApiMethod<?> generateMessage(UserCallbackRequest userCallbackRequest){
        if(userCallbackRequest.isEditMessagePreferred()){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userCallbackRequest.getChatId());
            editMessageText.setMessageId(userCallbackRequest.getMessageId());
            editMessageText.setText(generateText());
            editMessageText.setReplyMarkup(generateKeyboard(userCallbackRequest));
            return editMessageText;
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateText());
            sendMessage.setReplyMarkup(generateKeyboard(userCallbackRequest));
            sendMessage.setChatId(userCallbackRequest.getChatId());
            return sendMessage;
        }
    }

    public abstract String getTypeString();
    protected abstract InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest);
    protected abstract String generateText();
    public abstract View getNextView(UserCallbackRequest userCallbackRequest);

}
