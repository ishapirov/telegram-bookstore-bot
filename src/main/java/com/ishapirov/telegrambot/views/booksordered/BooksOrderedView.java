package com.ishapirov.telegrambot.views.booksordered;

import com.ishapirov.telegrambot.domain.bookordered.BookOrdered;
import com.ishapirov.telegrambot.domain.shippingorder.ShippingOrder;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.orders.OrdersService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class BooksOrderedView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    CurrencyConversionService currencyConversionService;

    @Override
    @Transactional
    public BotApiMethod<?> generateMessage(UserCallbackRequest userCallbackRequest){
        List<ShippingOrder> shippingOrders = ordersService.getUserShippingOrders(userCallbackRequest.getUserId());
        if(shippingOrders.size() == 0){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userCallbackRequest.getChatId());
            editMessageText.setMessageId(userCallbackRequest.getMessageId());
            editMessageText.setText(emptyText());
            editMessageText.setReplyMarkup(emptyKeyboard());
            return editMessageText;
        }
        else{
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(userCallbackRequest.getChatId());
            editMessageText.setMessageId(userCallbackRequest.getMessageId());
            editMessageText.setText(generateText() + orderInfo(shippingOrders));
            editMessageText.setReplyMarkup(generateKeyboard(userCallbackRequest));
            return editMessageText;
        }
    }


    private String emptyText() {
        return localeMessageService.getMessage("view.orders.empty");
    }

    private InlineKeyboardMarkup emptyKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), mainMenuText(),false));
        keyboardButtonsRow1.add(buttonMainMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    protected InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), mainMenuText(),false));
        keyboardButtonsRow1.add(buttonMainMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    protected String generateText() {
        return localeMessageService.getMessage("view.orders.generate") + "\n";
    }

    @Transactional
    private String orderInfo(List<ShippingOrder> shippingOrders) {
        String orderInfo = "";
        for(ShippingOrder shippingOrder: shippingOrders){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(shippingOrder.getDateOrdered());
            orderInfo += localeMessageService.getMessage("shippingorder.date") + ": " + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR) + "\n";
            orderInfo += localeMessageService.getMessage("shippingorder.books") + ":\n";
            for(BookOrdered bookOrdered: shippingOrder.getBooksOrdered()){
                orderInfo+= bookOrdered.getBook().getTitle() + ", " + localeMessageService.getMessage("shippingorder.quantity") +": " + bookOrdered.getQuantity() +"\n";
            }
            orderInfo+=localeMessageService.getMessage("shippingorder.totalcost") + ": " + currencyConversionService.displayPrice(shippingOrder.getCurrency(), shippingOrder.getTotalCost()) +"\n\n";
        }
        return orderInfo;
    }

    @Override
    public String getTypeString() {
        return "booksordered";
    }

    public String mainMenuText() {
        return "mainmenu";
    }

    @Override
    public View getNextView(UserCallbackRequest userCallbackRequest) {
        String messageText = userCallbackRequest.getButtonClicked();
        if(messageText.equals(mainMenuText()))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }
}
