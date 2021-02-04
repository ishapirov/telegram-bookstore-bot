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
            return removedFromCartMessage(messageId,chatId);
        if(viewRemoveBooksViewInfo.isNoBooksInCart())
            return emptyEditMessage(chatId);
        if(editMessagePreferred){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(generateBookText(viewRemoveBooksViewInfo));
            editMessageText.setMessageId(messageId);
            editMessageText.setChatId(chatId);
            editMessageText.setReplyMarkup(generateKeyboard(viewRemoveBooksViewInfo.getIndex(), viewRemoveBooksViewInfo.hasNext()));
            return editMessageText;
        } else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateBookText(viewRemoveBooksViewInfo));
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(generateKeyboard(viewRemoveBooksViewInfo.getIndex(), viewRemoveBooksViewInfo.hasNext()));
            return sendMessage;
        }

    }

    public SendMessage emptyEditMessage(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateEmptyText());
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(generateEmptyKeyboard());
        return sendMessage;
    }

    private InlineKeyboardMarkup generateEmptyKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return"));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,false));

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,false));

        keyboardRow.add(buttonCart);
        keyboardRow.add(buttonMainMenu);

        rowList.add(keyboardRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private String generateEmptyText() {
        return localeMessageService.getMessage("view.viewandedit.nobooksincart");
    }


    protected InlineKeyboardMarkup generateKeyboard(int index, boolean hasNext) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(forwardBackButtons(index,hasNext));
        rowList.add(removeBook(index));
        rowList.add(cartButton());

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public List<InlineKeyboardButton> forwardBackButtons(int index,boolean hasNext){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        if(index > 0){
            InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
            buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(ButtonAction.BACK_A_BOOK_IN_CART, String.valueOf(index), "true"));
            keyboardRow.add(buttonBack);
        }
        if(hasNext){
            InlineKeyboardButton buttonForward = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.forward"));
            buttonForward.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(ButtonAction.FORWARD_A_BOOK_IN_CART,String.valueOf(index), "true"));
            keyboardRow.add(buttonForward);
        }
        return keyboardRow;
    }

    private List<InlineKeyboardButton> removeBook(int index) {
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonRemove = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.remove"));
        buttonRemove.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(ButtonAction.REMOVE_BOOK_FROM_CART,String.valueOf(index), "false"));
        keyboardRow.add(buttonRemove);
        return keyboardRow;
    }


    public List<InlineKeyboardButton> cartButton(){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return"));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,false));

        keyboardRow.add(buttonCart);
        return keyboardRow;
    }

    private EditMessageText removedFromCartMessage(int messageId,long chatId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(generateRemovedBookText());
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId);
        editMessageText.setReplyMarkup(removedKeyboard());
        return editMessageText;
    }

    private String generateRemovedBookText() {
        return localeMessageService.getMessage("view.viewandedit.removedfromcart");
    }

    private InlineKeyboardMarkup removedKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return"));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,false));

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,false));

        keyboardRow.add(buttonCart);
        keyboardRow.add(buttonMainMenu);

        rowList.add(keyboardRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public String generateBookText(ViewRemoveBooksViewInfo viewRemoveBooksViewInfo) {
        return  localeMessageService.getMessage("view.bookcatalog.title") + ": " + viewRemoveBooksViewInfo.getBook().getTitle() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.author") + ": " + viewRemoveBooksViewInfo.getBook().getAuthor() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.price") + ": " + viewRemoveBooksViewInfo.getConvertedBookPrice() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.publisher") + ": " + viewRemoveBooksViewInfo.getBook().getPublisher() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.year") + ": " + viewRemoveBooksViewInfo.getBook().getPublishingYear() + "\n" +
                viewRemoveBooksViewInfo.getBook().getDescription() + "\n" +
                viewRemoveBooksViewInfo.getBook().getLinkToAudioFile()  + "\n" +
                localeMessageService.getMessage("view.viewandedit.quantity") + ": " + viewRemoveBooksViewInfo.getQuantitySelected() + "\n" +
                localeMessageService.getMessage("view.viewandedit.totalcost") + ": " + viewRemoveBooksViewInfo.getConvertedTotalPrice();
    }

    protected String generateText() {
        return localeMessageService.getMessage("view.viewandedit.generate");
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
