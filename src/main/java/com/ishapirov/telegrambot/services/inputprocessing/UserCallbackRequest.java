package com.ishapirov.telegrambot.services.inputprocessing;


import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@NoArgsConstructor
public class UserCallbackRequest {
    private final static String SEPARATOR = ",";
    private long chatId;
    private int userId;
    private int messageId;
    private String callBackId;
    private String action;
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
        this.action = callbackParts[0];
        this.bookType = callbackParts[1];
        this.bookSubType = callbackParts[2];
        this.index = Integer.parseInt(callbackParts[3]);
        this.quantity = Integer.parseInt(callbackParts[4]);
        this.editMessagePreferred = Boolean.parseBoolean(callbackParts[5]);
    }

    public UserCallbackRequest(Message message){
        this.chatId = message.getChatId();
        this.userId = message.getFrom().getId();
        this.messageId = message.getMessageId();
    }


    public String generateQueryMessage(){
        return this.action + SEPARATOR +
                this.bookType + SEPARATOR + this.bookSubType + SEPARATOR + this.index + SEPARATOR +
                this.quantity + SEPARATOR + this.editMessagePreferred;
    }

    public static String generateQueryMessage(ButtonAction action, boolean editMessageNeeded){
        return action.name() + SEPARATOR + "all" + SEPARATOR + "none" +  SEPARATOR + "0" + SEPARATOR +"1" + SEPARATOR + editMessageNeeded;
    }

    public static String generateQueryMessageWithFilter(ButtonAction action,String bookType, String bookSubType){
        return action.name() + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + "0" + SEPARATOR +"1" + SEPARATOR + false;
    }

    public static String generateQueryMessageWithFilterIndex(ButtonAction action, String bookType, String bookSubType, String index){
        return action.name() + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + index + SEPARATOR +"1" + SEPARATOR + false;
    }
    public static String generateQueryMessageWithFilterIndexQuantityEdit(ButtonAction action, String bookType, String bookSubType, String index, String quantity, String editMessage){
        return action.name() + SEPARATOR + bookType + SEPARATOR + bookSubType + SEPARATOR + index + SEPARATOR + quantity + SEPARATOR + editMessage;
    }

    public static String generateQueryMessageIndexEdit(ButtonAction action,String index,String editMessage){
        return action.name() + SEPARATOR + "all" + SEPARATOR + "none" +  SEPARATOR + index + SEPARATOR + "1" + SEPARATOR + editMessage;
    }
}
