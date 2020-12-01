package com.ishapirov.telegrambot.domain;


import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Data
public class UserCallbackRequest {
    private long chatId;
    private int userId;
    private String viewInWhichButtonWasClicked;
    private String buttonClicked;

    public UserCallbackRequest(CallbackQuery callbackQuery){
        this.chatId = callbackQuery.getMessage().getChatId();
        this.userId = callbackQuery.getFrom().getId();
        setViewAndButtonClickedFromString(callbackQuery.getData());
    }

    private void setViewAndButtonClickedFromString(String callbackString){
        String[] callbackParts = callbackString.split("-");
        this.viewInWhichButtonWasClicked = callbackParts[0];
        this.buttonClicked = callbackParts[1];
    }
}
