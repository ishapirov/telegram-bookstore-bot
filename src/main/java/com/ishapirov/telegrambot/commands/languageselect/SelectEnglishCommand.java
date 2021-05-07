package com.ishapirov.telegrambot.commands.languageselect;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.language.LanguageSelectionController;
import com.ishapirov.telegrambot.controllers.language.dto.UpdatedLanguage;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelectEnglishCommand implements Command {
    @Autowired
    LanguageSelectionController languageSelectionController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.SELECT_ENG;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        UpdatedLanguage updatedLanguage = new UpdatedLanguage(userCallbackRequest.getUserId(),"en-US");
        return languageSelectionController.updateLanguage(updatedLanguage);
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}

