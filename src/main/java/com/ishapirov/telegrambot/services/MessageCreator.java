package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.cache.UserSessionCache;
import com.ishapirov.telegrambot.domain.UserSession;
import com.ishapirov.telegrambot.domain.userstate.MainMenuState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageCreator {

    @Autowired
    UserSessionCache userSessionCache;

    public SendMessage getSendMessage(Message message){
        Integer userId = message.getFrom().getId();
        UserSession session = userSessionCache.getSession(userId);
        if(session == null)
            session = initializeSession(userId);
        return session.processMessage(message);
    }

    private UserSession initializeSession(Integer userId){
        userSessionCache.setSession(userId,new UserSession());
        return userSessionCache.getSession(userId);
    }

}
