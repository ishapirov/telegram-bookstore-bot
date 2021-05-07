package com.ishapirov.telegrambot.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Currency;

@Entity
@Table(name="userprofile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    private int userId;
    private Currency currency;
    private String localeString;


    public UserProfile(Integer userId, Currency currency,String localeString) {
        this.userId = userId;
        this.currency = currency;
        this.localeString = localeString;
    }

}
