package com.ishapirov.telegrambot.domain.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("kid")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KidBook extends Book{
    private KidBookCategory kidBookCategory;

    public static String typeOfBook(){
        return "kids";
    }
}
