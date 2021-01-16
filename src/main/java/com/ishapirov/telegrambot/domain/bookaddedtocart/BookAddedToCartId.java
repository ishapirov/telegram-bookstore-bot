package com.ishapirov.telegrambot.domain.bookaddedtocart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BookAddedToCartId implements Serializable {
    private String bookISBN;
    private int userId;
}
