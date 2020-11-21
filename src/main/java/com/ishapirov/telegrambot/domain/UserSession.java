package com.ishapirov.telegrambot.domain;

import com.ishapirov.telegrambot.domain.userstate.StartState;
import com.ishapirov.telegrambot.domain.userstate.UserState;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Currency;

@Data
public class UserSession {
    private UserState userState;
    private Currency currency;

    public UserSession(){
        this.userState = new StartState(this);
        this.currency = Currency.getInstance("USD");
    }

    public SendMessage processMessage(Message message) {
        SendMessage sendMessage;
        userState.changeStateBasedOnInput(message.getText());
        sendMessage = userState.generateSendMessage();
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }

}
