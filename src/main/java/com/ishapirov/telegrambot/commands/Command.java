package com.ishapirov.telegrambot.commands;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;

public interface Command {
    public Object execute(UserCallbackRequest userCallbackRequest);
    public ButtonAction getButtonActionTrigger();
}
