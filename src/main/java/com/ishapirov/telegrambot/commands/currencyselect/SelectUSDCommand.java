package com.ishapirov.telegrambot.commands.currencyselect;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.currency.CurrencySelectionController;
import com.ishapirov.telegrambot.controllers.currency.dto.UpdatedCurrency;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelectUSDCommand implements Command {
    @Autowired
    CurrencySelectionController currencySelectionController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.SELECT_USD;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        UpdatedCurrency updatedCurrency = new UpdatedCurrency(userCallbackRequest.getUserId(),"USD");
        return currencySelectionController.updateCurrency(updatedCurrency);
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}
