package com.ishapirov.telegrambot.services.shipping;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerShippingQuery;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingOption;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingOptionsService {


    public AnswerShippingQuery handleShippingQuery(ShippingQuery shippingQuery){
        AnswerShippingQuery answerShippingQuery = new AnswerShippingQuery();
        answerShippingQuery.setShippingQueryId(shippingQuery.getId());

        answerShippingQuery.setShippingOptions(getShippingOptions());
        System.out.println(shippingQuery.getShippingAddress());
        answerShippingQuery.setOk(true);
        return answerShippingQuery;
    }

    private List<ShippingOption> getShippingOptions(){

        List<LabeledPrice> prices = new ArrayList<>();
        LabeledPrice labeledPrice = new LabeledPrice();
        labeledPrice.setLabel("ShippingPrice");
        labeledPrice.setAmount(100);
        prices.add(labeledPrice);

        List<ShippingOption> shippingOptions = new ArrayList<>();
        ShippingOption shippingOption = new ShippingOption();
        shippingOption.setId("option1");
        shippingOption.setPrices(prices);
        shippingOption.setTitle("Cool Shipping Option");
        shippingOptions.add(shippingOption);
        return shippingOptions;
    }

}
