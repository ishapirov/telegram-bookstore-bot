package com.ishapirov.telegrambot.cache;

import com.ishapirov.telegrambot.domain.UserSession;

public interface UserSessionCache {
    UserSession getSession(Integer userID);
    void setSession(Integer userID, UserSession userSession);
}
