package com.ishapirov.telegrambot.testdomain;

import org.telegram.telegrambots.meta.api.objects.payments.ShippingAddress;

public class CustomShippingAddress extends ShippingAddress {
    private String city;
    private String countryCode;
    private String postCode;
    private String state;
    private String streetLine1;
    private String streetLine2;

    public CustomShippingAddress(String city, String countryCode, String postCode, String state, String streetLine1, String streetLine2) {
        this.city = city;
        this.countryCode = countryCode;
        this.postCode = postCode;
        this.state = state;
        this.streetLine1 = streetLine1;
        this.streetLine2 = streetLine2;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getPostCode() {
        return postCode;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getStreetLine1() {
        return streetLine1;
    }

    @Override
    public String getStreetLine2() {
        return streetLine2;
    }

}
