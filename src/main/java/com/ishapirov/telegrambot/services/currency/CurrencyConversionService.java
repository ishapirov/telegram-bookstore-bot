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


    public BigDecimal getUZS() {
        String jsonString = null;
        try {
            jsonString = getRates();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject(jsonString);
        return obj.getJSONObject("rates").getBigDecimal("UZS");
    }

    public BigDecimal getRUB() {
        String jsonString = null;
        try {
            jsonString = getRates();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject(jsonString);
        return obj.getJSONObject("rates").getBigDecimal("RUB");
    }

    public String displayPrice(CurrencyConversionRate currencyConversionRate, BigDecimal price) {
        if (currencyConversionRate.getCurrency().getCurrencyCode().equals("UZS"))
            return price + " " + localeMessageService.getMessage("currency.uzs");
        else if (currencyConversionRate.getCurrency().getCurrencyCode().equals("RUB"))
            return price + localeMessageService.getMessage("currency.rub");
        else
            return localeMessageService.getMessage("currency.usd") + price;
    }

    public String displayPrice(String currencyCode, BigDecimal price) {
        if (currencyCode.equals("UZS"))
            return price + " " + localeMessageService.getMessage("currency.uzs");
        else if (currencyCode.equals("RUB"))
            return price + localeMessageService.getMessage("currency.rub");
        else
            return localeMessageService.getMessage("currency.usd") + price;
    }
}
