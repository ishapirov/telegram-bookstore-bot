package com.ishapirov.telegrambot.commands.gotoview;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.gotoview.GoToController;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoToCurrencySelectCommand implements Command {
    @Autowired
    GoToController goToController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.GO_TO_CURRENCY_SELECT;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        return goToController.currencySelectData();
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}
