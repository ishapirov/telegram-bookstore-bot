package com.ishapirov.telegrambot.views.mainmenu;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.views.mainmenu.dto.MainMenuViewDTO;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.views.TelegramView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuView implements TelegramView {

    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING = "mainmenu";

    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId,String callback, boolean editMessagePreferred) {
        MainMenuViewDTO mainMenuViewDTO = (MainMenuViewDTO) object;
        if(editMessagePreferred){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(generateText(mainMenuViewDTO.getLocaleString()));
            editMessageText.setReplyMarkup(generateKeyboard(mainMenuViewDTO.getCurrency(),mainMenuViewDTO.getLocaleString()));
            return editMessageText;
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateText(mainMenuViewDTO.getLocaleString()));
            sendMessage.setReplyMarkup(generateKeyboard(mainMenuViewDTO.getCurrency(),mainMenuViewDTO.getLocaleString()));
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
    }

    public InlineKeyboardMarkup generateKeyboard(String currency,String locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonCatalog = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.catalog",locale));
        buttonCatalog.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_CATALOG_MENU,true));
        InlineKeyboardButton buttonBasket = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.basket",locale));
        buttonBasket.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,true));
        keyboardButtonsRow1.add(buttonCatalog);
        keyboardButtonsRow1.add(buttonBasket);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        InlineKeyboardButton buttonManager = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.manager",locale));
        buttonManager.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MANAGER_INFO,true));
        InlineKeyboardButton buttonCurrency = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.currency",locale) +" (" + currency + ")");
        buttonCurrency.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_CURRENCY_SELECT,true));
        keyboardButtonsRow2.add(buttonManager);
        keyboardButtonsRow2.add(buttonCurrency);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        InlineKeyboardButton buttonOrders = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.orders",locale));
        buttonOrders.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_ORDERS,true));
        InlineKeyboardButton buttonLanguage = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.language",locale));
        buttonLanguage.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_LANGUAGE_SELECT,true));
        keyboardButtonsRow3.add(buttonOrders);
        keyboardButtonsRow3.add(buttonLanguage);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String generateText(String locale){
        return localeMessageService.getMessage("view.mainmenu.generate",locale);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }

}
