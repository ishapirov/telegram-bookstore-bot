package com.ishapirov.telegrambot.views.cart;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.views.TelegramView;
import com.ishapirov.telegrambot.views.cart.dto.ViewRemoveBooksViewInfo;
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
public class ViewRemoveBooksInCartView implements TelegramView {
    @Autowired
    LocaleMessageService localeMessageService;

    public static final String TYPE_STRING ="viewandedit";

    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId,String callbackId, boolean editMessagePreferred) {
        ViewRemoveBooksViewInfo viewRemoveBooksViewInfo = (ViewRemoveBooksViewInfo)object;
        if(viewRemoveBooksViewInfo.isRemovedFromCart())
            return removedFromCartMessage(messageId,chatId,viewRemoveBooksViewInfo.getLocaleString());
        if(viewRemoveBooksViewInfo.isNoBooksInCart())
            return emptyEditMessage(chatId,viewRemoveBooksViewInfo.getLocaleString());
        if(editMessagePreferred){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(generateBookText(viewRemoveBooksViewInfo));
            editMessageText.setMessageId(messageId);
            editMessageText.setChatId(chatId);
            editMessageText.setReplyMarkup(generateKeyboard(viewRemoveBooksViewInfo.getIndex(), viewRemoveBooksViewInfo.hasNext(),viewRemoveBooksViewInfo.getLocaleString()));
            return editMessageText;
        } else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateBookText(viewRemoveBooksViewInfo));
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(generateKeyboard(viewRemoveBooksViewInfo.getIndex(), viewRemoveBooksViewInfo.hasNext(),viewRemoveBooksViewInfo.getLocaleString()));
            return sendMessage;
        }

    }

    public SendMessage emptyEditMessage(long chatId,String locale){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateEmptyText(locale));
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(generateEmptyKeyboard(locale));
        return sendMessage;
    }

    private InlineKeyboardMarkup generateEmptyKeyboard(String locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return",locale));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,false));

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate",locale));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,false));

        keyboardRow.add(buttonCart);
        keyboardRow.add(buttonMainMenu);

        rowList.add(keyboardRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private String generateEmptyText(String locale) {
        return localeMessageService.getMessage("view.viewandedit.nobooksincart",locale);
    }


    protected InlineKeyboardMarkup generateKeyboard(int index, boolean hasNext,String locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(forwardBackButtons(index,hasNext,locale));
        rowList.add(removeBook(index,locale));
        rowList.add(cartButton(locale));

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public List<InlineKeyboardButton> forwardBackButtons(int index,boolean hasNext,String locale){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        if(index > 0){
            InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back",locale));
            buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(ButtonAction.BACK_A_BOOK_IN_CART, String.valueOf(index), "true"));
            keyboardRow.add(buttonBack);
        }
        if(hasNext){
            InlineKeyboardButton buttonForward = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.forward",locale));
            buttonForward.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(ButtonAction.FORWARD_A_BOOK_IN_CART,String.valueOf(index), "true"));
            keyboardRow.add(buttonForward);
        }
        return keyboardRow;
    }

    private List<InlineKeyboardButton> removeBook(int index,String locale) {
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonRemove = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.remove",locale));
        buttonRemove.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(ButtonAction.REMOVE_BOOK_FROM_CART,String.valueOf(index), "false"));
        keyboardRow.add(buttonRemove);
        return keyboardRow;
    }


    public List<InlineKeyboardButton> cartButton(String locale){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return",locale));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,false));

        keyboardRow.add(buttonCart);
        return keyboardRow;
    }

    private EditMessageText removedFromCartMessage(int messageId,long chatId,String locale) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(generateRemovedBookText(locale));
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId);
        editMessageText.setReplyMarkup(removedKeyboard(locale));
        return editMessageText;
    }

    private String generateRemovedBookText(String locale) {
        return localeMessageService.getMessage("view.viewandedit.removedfromcart",locale);
    }

    private InlineKeyboardMarkup removedKeyboard(String locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return",locale));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,false));

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate",locale));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,false));

        keyboardRow.add(buttonCart);
        keyboardRow.add(buttonMainMenu);

        rowList.add(keyboardRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public String generateBookText(ViewRemoveBooksViewInfo viewRemoveBooksViewInfo) {
        String locale = viewRemoveBooksViewInfo.getLocaleString();
        return  localeMessageService.getMessage("view.bookcatalog.title",locale) + ": " + viewRemoveBooksViewInfo.getBook().getTitle() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.author",locale) + ": " + viewRemoveBooksViewInfo.getBook().getAuthor() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.price",locale) + ": " + viewRemoveBooksViewInfo.getConvertedBookPrice() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.publisher",locale) + ": " + viewRemoveBooksViewInfo.getBook().getPublisher() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.year",locale) + ": " + viewRemoveBooksViewInfo.getBook().getPublishingYear() + "\n" +
                viewRemoveBooksViewInfo.getBook().getDescription() + "\n" +
                viewRemoveBooksViewInfo.getBook().getLinkToAudioFile()  + "\n" +
                localeMessageService.getMessage("view.viewandedit.quantity",locale) + ": " + viewRemoveBooksViewInfo.getQuantitySelected() + "\n" +
                localeMessageService.getMessage("view.viewandedit.totalcost",locale) + ": " + viewRemoveBooksViewInfo.getConvertedTotalPrice();
    }

    protected String generateText(String locale) {
        return localeMessageService.getMessage("view.viewandedit.generate",locale);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
