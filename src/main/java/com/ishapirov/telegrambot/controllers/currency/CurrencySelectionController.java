package com.ishapirov.telegrambot.controllers.currency;

import com.ishapirov.telegrambot.controllers.currency.dto.UpdatedCurrency;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.views.mainmenu.dto.MainMenuViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Currency;

@Controller
public class CurrencySelectionController {
    @Autowired
    UserProfileService userProfileService;

    public MainMenuViewDTO updateCurrency(UpdatedCurrency updatedCurrency) {
        Currency newCurrency = Currency.getInstance(updatedCurrency.getCurrencyCodeSelected());
        userProfileService.setCurrencyForUser(updatedCurrency.getUserId(), newCurrency);
        return new MainMenuViewDTO(updatedCurrency.getCurrencyCodeSelected());
    }


}
