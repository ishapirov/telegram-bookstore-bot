package com.ishapirov.telegrambot.cache;

import com.ishapirov.telegrambot.domain.UserSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryCache implements UserSessionCache {

    Map<Integer,UserSession> userSessionMap;

    public InMemoryCache(){
        this.userSessionMap = new HashMap<>();
    }

    @Override
    public UserSession getSession(Integer userID) {
        return userSessionMap.get(userID);
    }

    @Override
    public void setSession(Integer userID, UserSession userSession) {
        userSessionMap.put(userID,userSession);
    }

    @Override
    public UserSession newSession(Integer userID) {
        UserSession newSession = new UserSession();
        userSessionMap.put(userID,newSession);
        return newSession;
    }

}
