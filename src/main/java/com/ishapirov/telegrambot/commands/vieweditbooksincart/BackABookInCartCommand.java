package com.ishapirov.telegrambot.commands.vieweditbooksincart;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.cart.ViewAndEditBooksInCartController;
import com.ishapirov.telegrambot.controllers.cart.dto.ViewAndEditBooksControllerInfo;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackABookInCartCommand implements Command {
    @Autowired
    ViewAndEditBooksInCartController viewAndEditBooksInCartController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.BACK_A_BOOK_IN_CART;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        ViewAndEditBooksControllerInfo viewAndEditBooksControllerInfo = new ViewAndEditBooksControllerInfo(userCallbackRequest.getUserId(),
                userCallbackRequest.getIndex());
        return viewAndEditBooksInCartController.backABook(viewAndEditBooksControllerInfo);
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}
