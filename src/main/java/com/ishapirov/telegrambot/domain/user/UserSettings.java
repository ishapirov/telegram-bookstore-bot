package com.ishapirov.telegrambot.domain.user;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="usersettings")
public class UserSettings {
    @Id
    private int userId;

}
