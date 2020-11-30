package com.ishapirov.telegrambot.domain;

import com.ishapirov.telegrambot.domain.userstate.State;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Currency;

@Data
public class UserSession {
    private State state;
    private Currency currency;

    public UserSession(){
        this.state = State.STATE;
        this.currency = Currency.getInstance("USD");
    }

    public SendMessage processMessage(Message message) {
        SendMessage sendMessage;
        userState.changeSessionStateBasedOnInput(message.getText(),this);
        sendMessage = userState.generateSendMessage();
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }

}
