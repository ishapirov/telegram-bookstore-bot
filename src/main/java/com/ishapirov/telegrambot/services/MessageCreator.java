package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.cache.UserSessionCache;
import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageCreator {

    @Autowired
    UserSessionCache userSessionCache;

    @Autowired
    StateService stateService;

    public SendMessage getSendMessage(Message message){
        Integer userId = message.getFrom().getId();
        UserSession session = getOrInitializeSession(userId);
        stateService.getState(session.getState()).changeSessionStateBasedOnInput(message.getText(), session);
        stateService.getState(session.getState()).generateSendMessage(session);
        return session.processMessage(message);
    }

    UserSession getOrInitializeSession(Integer userId){
        if(userSessionCache.getSession(userId) == null){
            UserSession userSession = new UserSession();
            userSessionCache.setSession(userId,userSession);
            return userSession;
        } else
            return userSessionCache.getSession(userId);

    }

}
