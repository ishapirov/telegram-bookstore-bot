package com.ishapirov.telegrambot.views.booksordered;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.views.TelegramView;
import com.ishapirov.telegrambot.views.booksordered.dto.BookOrderedInfo;
import com.ishapirov.telegrambot.views.booksordered.dto.ShippingOrderInfo;
import com.ishapirov.telegrambot.views.booksordered.dto.ShippingOrderViewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class BooksOrderedView implements TelegramView {
    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING = "booksordered";

    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId, String callbackId, boolean editMessagePreferred) {
        ShippingOrderViewInfo shippingOrderViewInfo = (ShippingOrderViewInfo)object;
        if(shippingOrderViewInfo.getShippingOrders().size() == 0){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(emptyText());
            editMessageText.setReplyMarkup(emptyKeyboard());
            return editMessageText;
        }
        else{
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(generateText() + orderInfo(shippingOrderViewInfo.getShippingOrders()));
            editMessageText.setReplyMarkup(generateKeyboard());
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
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,false));
        keyboardButtonsRow1.add(buttonMainMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    protected InlineKeyboardMarkup generateKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,false));
        keyboardButtonsRow1.add(buttonMainMenu);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    protected String generateText() {
        return localeMessageService.getMessage("view.orders.generate") + "\n";
    }

    private String orderInfo(List<ShippingOrderInfo> shippingOrders) {
        StringBuilder orderInfo = new StringBuilder();
        for(ShippingOrderInfo shippingOrder: shippingOrders){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(shippingOrder.getDateOrdered());
            orderInfo.append(localeMessageService.getMessage("shippingorder.date")).append(": ").append(calendar.get(Calendar.DAY_OF_MONTH)).append("-").append(calendar.get(Calendar.MONTH)).append("-").append(calendar.get(Calendar.YEAR)).append("\n");
            orderInfo.append(localeMessageService.getMessage("shippingorder.books")).append(":\n");
            for(BookOrderedInfo bookOrdered: shippingOrder.getBooksOrdered()){
                orderInfo.append(bookOrdered.getTitle()).append(", ").append(localeMessageService.getMessage("shippingorder.quantity")).append(": ").append(bookOrdered.getQuantity()).append("\n");
            }
            orderInfo.append(localeMessageService.getMessage("shippingorder.totalcost")).append(": ").append(shippingOrder.getTotalPrice()).append("\n\n");
        }
        return orderInfo.toString();
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }


}
