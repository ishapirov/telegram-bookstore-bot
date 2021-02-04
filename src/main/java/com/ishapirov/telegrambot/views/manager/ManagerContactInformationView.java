package com.ishapirov.telegrambot.views.manager;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.TelegramView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerContactInformationView implements TelegramView {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING = "managercontactinformation";

    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId, String callbackId, boolean editMessagePreferred) {
        if(editMessagePreferred){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setText(generateText());
            editMessageText.setReplyMarkup(generateKeyboard());
            return editMessageText;
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateText());
            sendMessage.setReplyMarkup(generateKeyboard());
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
    }

    public InlineKeyboardMarkup generateKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,true));
        keyboardButtonsRow1.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String generateText() {
        return localeMessageService.getMessage("view.manager.generate");
    }

    public String backText(){
        return "back";
    }




    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
