package com.ishapirov.telegrambot.services.currency;

import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Currency;

@Service
public class CurrencyConversionService {

    @Value("${exchangeRate.url}")
    private String EXCHANGE_RATE_API;
    @Autowired
    LocaleMessageService localeMessageService;

    @Autowired
    UserProfileService userProfileService;

    private String getRates() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(EXCHANGE_RATE_API))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public CurrencyConversionRate getConversionRate(Integer userId) {
        Currency currency = userProfileService.getCurrencyForUser(userId);
        CurrencyConversionRate currencyConversionRate = new CurrencyConversionRate();
        currencyConversionRate.setCurrency(currency);
        if (currency.getCurrencyCode().equals("UZS"))
            currencyConversionRate.setConversionRate(getUZS());
        else if (currency.getCurrencyCode().equals("RUB"))
            currencyConversionRate.setConversionRate(getRUB());
        else
            currencyConversionRate.setConversionRate(new BigDecimal(1));
        return currencyConversionRate;
    }

    public CurrencyConversionRate getConversionRate(String currencyCode) {
        CurrencyConversionRate currencyConversionRate = new CurrencyConversionRate();
        currencyConversionRate.setCurrency(Currency.getInstance(currencyCode));
        if (currencyCode.equals("UZS"))
            currencyConversionRate.setConversionRate(getUZS());
        else if (currencyCode.equals("RUB"))
            currencyConversionRate.setConversionRate(getRUB());
        else
            currencyConversionRate.setConversionRate(new BigDecimal(1));
        return currencyConversionRate;
    }


    public BigDecimal getUZS() {
        String jsonString = null;
        try {
            jsonString = getRates();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject(jsonString);
        return obj.getJSONObject("rates").getBigDecimal("UZS");
    }

    public BigDecimal getRUB() {
        String jsonString = null;
        try {
            jsonString = getRates();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject(jsonString);
        return obj.getJSONObject("rates").getBigDecimal("RUB");
    }

    private String displayPrice(CurrencyConversionRate currencyConversionRate, BigDecimal priceInUSD) {
        BigDecimal convertedPrice = currencyConversionRate.getConvertedPrice(priceInUSD);
        if (currencyConversionRate.getCurrency().getCurrencyCode().equals("UZS"))
            return convertedPrice + " " + localeMessageService.getMessage("currency.uzs");
        else if (currencyConversionRate.getCurrency().getCurrencyCode().equals("RUB"))
            return convertedPrice + localeMessageService.getMessage("currency.rub");
        else
            return localeMessageService.getMessage("currency.usd") + convertedPrice;
    }

    public String displayPrice(int userId,BigDecimal priceInUSD){
        CurrencyConversionRate currencyConversionRate = getConversionRate(userId);
        return displayPrice(currencyConversionRate,priceInUSD);
    }

    public String displayPrice(int userId,BigDecimal priceInUSD,int quantity){
        CurrencyConversionRate currencyConversionRate = getConversionRate(userId);
        return displayPrice(currencyConversionRate,priceInUSD.multiply(new BigDecimal(quantity)));
    }

    public String displayPrice(String currency,BigDecimal priceInUSD){
        CurrencyConversionRate currencyConversionRate = getConversionRate(currency);
        return displayPrice(currencyConversionRate,priceInUSD);
    }

    public String displayPriceAlreadyConverted(String currency,BigDecimal priceInUSD){
        if (currency.equals("UZS"))
            return priceInUSD + " " + localeMessageService.getMessage("currency.uzs");
        else if (currency.equals("RUB"))
            return priceInUSD + localeMessageService.getMessage("currency.rub");
        else
            return localeMessageService.getMessage("currency.usd") + priceInUSD;
    }
}
