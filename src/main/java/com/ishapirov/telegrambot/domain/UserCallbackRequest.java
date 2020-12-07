package com.ishapirov.telegrambot.domain;


import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
public class UserCallbackRequest {
    private final static String SEPARATOR = ",";
    private long chatId;
    private int userId;
    private String viewInWhichButtonWasClicked;
    private String buttonClicked;
    private Integer page;

    public UserCallbackRequest(CallbackQuery callbackQuery){
        this.chatId = callbackQuery.getMessage().getChatId();
        this.userId = callbackQuery.getFrom().getId();
        setViewAndButtonClickedFromString(callbackQuery.getData());
    }

    private void setViewAndButtonClickedFromString(String callbackString){
        String[] callbackParts = callbackString.split(SEPARATOR);
        this.viewInWhichButtonWasClicked = callbackParts[0];
        this.buttonClicked = callbackParts[1];
        this.page = Integer.parseInt(callbackParts[2]);
    }

    public UserCallbackRequest(Message message){
        this.chatId = message.getChatId();
        this.userId = message.getFrom().getId();
    }

    public String generateQueryMessage(){
        return this.viewInWhichButtonWasClicked + SEPARATOR + this.buttonClicked + SEPARATOR + this.page;
    }
    public static String generateQueryMessage(String viewInWhichButtonWasClicked,String buttonClicked){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + "0";
    }

    public static String generateQueryMessageWithPage(String viewInWhichButtonWasClicked,String buttonClicked,String page){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + page;
    }
}
