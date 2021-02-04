package com.ishapirov.telegrambot.commands.gotoview;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.bookcatalog.BookCatalogController;
import com.ishapirov.telegrambot.controllers.bookcatalog.dto.CatalogControllerData;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoToBookCatalogCommand implements Command {
    @Autowired
    BookCatalogController bookCatalogController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.GO_TO_BOOK_CATALOG;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        CatalogControllerData catalogControllerData = new CatalogControllerData(userCallbackRequest.getUserId(),
                userCallbackRequest.getIndex(),
                userCallbackRequest.getBookType(),
                userCallbackRequest.getBookSubType(),
                userCallbackRequest.getQuantity());
        return bookCatalogController.getBookCatalogBookInfo(catalogControllerData);
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}
