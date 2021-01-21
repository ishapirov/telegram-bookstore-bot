package com.ishapirov.telegrambot.services.inputprocessing;


import com.ishapirov.telegrambot.domain.book.BookInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@NoArgsConstructor
public class UserCallbackRequest {
    private final static String SEPARATOR = ",";
    private BookInfo bookInfo;
    private long chatId;
    private int userId;
    private int messageId;
    private String callBackId;
    private String viewInWhichButtonWasClicked;
    private String buttonClicked;
    private String bookType;
    private String bookSubType;
    private Integer index;
    private Integer quantity;
    private boolean editMessagePreferred;


    public UserCallbackRequest(CallbackQuery callbackQuery){
        this.chatId = callbackQuery.getMessage().getChatId();
        this.userId = callbackQuery.getFrom().getId();
        this.messageId = callbackQuery.getMessage().getMessageId();
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
        this.quantity = Integer.parseInt(callbackParts[5]);
        this.editMessagePreferred = Boolean.parseBoolean(callbackParts[6]);
    }

    public UserCallbackRequest(Message message){
        this.chatId = message.getChatId();
        this.userId = message.getFrom().getId();
        this.messageId = message.getMessageId();
    }


    public String generateQueryMessage(){
        return this.viewInWhichButtonWasClicked + SEPARATOR + this.buttonClicked + SEPARATOR +
                this.bookType + SEPARATOR + this.bookSubType + SEPARATOR + this.index + SEPARATOR +
                this.quantity + SEPARATOR + this.editMessagePreferred;
    }

    public static String generateQueryMessage(String viewInWhichButtonWasClicked,String buttonClicked,boolean editMessageNeeded){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + "all" + SEPARATOR + "none" +  SEPARATOR + "0" + SEPARATOR +"1" + SEPARATOR + editMessageNeeded;
    }

    public static String generateQueryMessageWithFilter(String viewInWhichButtonWasClicked,String buttonClicked,String bookType, String bookSubType){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + "0" + SEPARATOR +"1" + SEPARATOR + false;
    }

    public static String generateQueryMessageWithFilterIndex(String viewInWhichButtonWasClicked, String buttonClicked, String bookType, String bookSubType, String index){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + index + SEPARATOR +"1" + SEPARATOR + false;
    }
    public static String generateQueryMessageWithFilterIndexQuantityEdit(String viewInWhichButtonWasClicked, String buttonClicked, String bookType, String bookSubType, String index, String quantity, String editMessage){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + index + SEPARATOR + quantity + SEPARATOR + editMessage;
    }

    public static String generateQueryMessageIndexEdit(String viewInWhichButtonWasClicked,String buttonClicked,String index,String editMessage){
        return viewInWhichButtonWasClicked + SEPARATOR + buttonClicked + SEPARATOR + "all" + SEPARATOR + "none" +  SEPARATOR + index + SEPARATOR + "1" + SEPARATOR + editMessage;
    }
}
