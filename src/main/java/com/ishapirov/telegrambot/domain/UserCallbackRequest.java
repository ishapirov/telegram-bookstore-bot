package com.ishapirov.telegrambot.domain;


import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
public class UserCallbackRequest {
    private final static String SEPARATOR = ",";
    private long chatId;
    private int userId;
    private String callBackId;
    private String viewInWhichButtonWasClicked;
    private String buttonClicked;
    private String bookType;
    private String bookSubType;
    private Integer index;


    public UserCallbackRequest(CallbackQuery callbackQuery){
        this.chatId = callbackQuery.getMessage().getChatId();
        this.userId = callbackQuery.getFrom().getId();
        this.callBackId = callbackQuery.getId();
        setViewAndButtonClickedFromString(callbackQuery.getData());
    }

    private void setViewAndButtonClickedFromString(String callbackString){
        String[] callbackParts = callbackString.split(SEPARATOR);
        this.viewInWhichButtonWasClicked = callbackParts[0];
        this.buttonClicked = callbackParts[1];
        this.bookType = callbackParts[2];
        this.bookSubType = callbackParts[3];
        this.index = Integer.parseInt(callbackParts[4]);
    }

    public UserCallbackRequest(Message message){
        this.chatId = message.getChatId();
        this.userId = message.getFrom().getId();
    }

    public String generateQueryMessage(){
        return this.viewInWhichButtonWasClicked + SEPARATOR + this.buttonClicked + SEPARATOR + this.bookType + SEPARATOR + this.bookSubType + SEPARATOR + this.index;
    }

    public static String generateQueryMessage(String viewInWhichButtonWasClicked,String buttonClicked){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + "all" + SEPARATOR + "none" +  SEPARATOR + "0";
    }

    public static String generateQueryMessageWithFilter(String viewInWhichButtonWasClicked,String buttonClicked,String bookType, String bookSubType){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + "0";
    }

    public static String generateQueryMessageWithFilterIndex(String viewInWhichButtonWasClicked, String buttonClicked, String bookType, String bookSubType, String index){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + index;
    }
}
