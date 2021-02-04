package com.ishapirov.telegrambot.commands.bookcatalog;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.bookcatalog.BookCatalogController;
import com.ishapirov.telegrambot.controllers.bookcatalog.dto.CatalogControllerData;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddBookToCartCommand implements Command {
    @Autowired
    BookCatalogController bookCatalogController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.ADD_BOOK_TO_CART;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        CatalogControllerData catalogControllerData = new CatalogControllerData(userCallbackRequest.getUserId(),
                userCallbackRequest.getIndex(),
                userCallbackRequest.getBookType(),
                userCallbackRequest.getBookSubType(),
                userCallbackRequest.getQuantity());

        return bookCatalogController.addBookToCart(catalogControllerData);
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}
