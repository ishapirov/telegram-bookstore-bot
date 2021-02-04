package com.ishapirov.telegrambot.commands.gotoview;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.cart.ViewAndEditBooksInCartController;
import com.ishapirov.telegrambot.controllers.cart.dto.ViewAndEditBooksControllerInfo;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoToViewRemoveCommand implements Command {
    @Autowired
    ViewAndEditBooksInCartController viewAndEditBooksInCartController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.GO_TO_VIEW_REMOVE;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        ViewAndEditBooksControllerInfo viewAndEditBooksControllerInfo = new ViewAndEditBooksControllerInfo(userCallbackRequest.getUserId(),
                userCallbackRequest.getIndex());
        return viewAndEditBooksInCartController.getBookInCartInfo(viewAndEditBooksControllerInfo);
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}
