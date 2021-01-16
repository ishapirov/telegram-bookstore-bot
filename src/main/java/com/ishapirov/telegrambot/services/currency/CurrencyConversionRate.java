package com.ishapirov.telegrambot.services.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionRate {
    private BigDecimal conversionRate;
    private Currency currency;

    public BigDecimal getConvertedPrice(BigDecimal priceInDollars){
        return conversionRate.multiply(priceInDollars).setScale(2, RoundingMode.HALF_UP);
    }

}
