package com.ishapirov.telegrambot.domain.bookordered;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BookOrderedId implements Serializable {
    private String bookISBN;
    private String providerPaymentChargeId;
}
