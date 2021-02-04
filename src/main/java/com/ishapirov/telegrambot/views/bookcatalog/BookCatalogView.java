package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.bot.Bot;
import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.views.TelegramView;
import com.ishapirov.telegrambot.views.bookcatalog.dto.BookCatalogViewInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookCatalogView implements TelegramView {
    @Autowired
    LocaleMessageService localeMessageService;
    @Autowired
    Bot bot;

    public static final String TYPE_STRING = "bookcatalog";

    @Override
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId,String callbackId, boolean editMessagePreferred) {
        BookCatalogViewInfo bookInfo = (BookCatalogViewInfo) object;
        if(bookInfo.isNoBooksInCatalog())
            return noBooksSendMessage(chatId,messageId);
        if(bookInfo.isAddedToCartMessage())
            addedToCartMessage(callbackId);
        if(bookInfo.isBookAlreadyInCart())
            bookAlreadyInCart(callbackId);
        if(bookInfo.isLessBooksAvailable())
            lessBooksAvailableThanRequested(callbackId);
        if(!editMessagePreferred) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateBookText(bookInfo.getBook(), bookInfo.getConvertedPrice()));
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(generateKeyboard(bookInfo));
            sendBookPhoto(chatId, bookInfo.getBook());
            return sendMessage;
        } else {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(generateBookText(bookInfo.getBook(),bookInfo.getConvertedPrice()));
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(messageId);
            editMessageText.setReplyMarkup(generateKeyboard(bookInfo));
            return editMessageText;
        }
    }

    public InlineKeyboardMarkup generateKeyboard(BookCatalogViewInfo bookInfo) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(forwardBackButtons(bookInfo));
        rowList.addAll(bookQuantityAddToCartButtons(bookInfo));
        rowList.add(mainMenuCartButtons());

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup noBooksKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.mainmenu"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,true));
        keyboardRow.add(buttonMainMenu);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_CATALOG_MENU,true));
        keyboardRow.add(buttonBack);
        rowList.add(keyboardRow);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public void sendBookPhoto(long chatId,Book book) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(book.getTitle(),new ByteArrayInputStream(book.getPicture()));
        try {
            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public List<InlineKeyboardButton> forwardBackButtons(BookCatalogViewInfo bookInfo){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        if(bookInfo.getIndex() > 0){
            InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
            buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndex(ButtonAction.BACK_A_BOOK_IN_CATALOG,bookInfo.getBookType(),bookInfo.getBookSubType(), String.valueOf(bookInfo.getIndex())));
            keyboardRow.add(buttonBack);
        }
        if(bookInfo.isHasNext()){
            InlineKeyboardButton buttonForward = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.forward"));
            buttonForward.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndex(ButtonAction.FORWARD_A_BOOK_IN_CATALOG,bookInfo.getBookType(),bookInfo.getBookSubType(), String.valueOf(bookInfo.getIndex())));
            keyboardRow.add(buttonForward);
        }
        return keyboardRow;
    }

    public List<List<InlineKeyboardButton>> bookQuantityAddToCartButtons(BookCatalogViewInfo bookInfo){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        if(bookInfo.getBookQuantityAvailable() == 0){
            rowList.add(outOfStockButton());
            return rowList;
        }

        if(bookInfo.getBookQuantitySelected() > 1) {
            InlineKeyboardButton decreaseQuantityButton = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.decrement"));
            decreaseQuantityButton.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndexQuantityEdit(ButtonAction.DECREASE_QUANTITY, bookInfo.getBookType(), bookInfo.getBookSubType()
                    , String.valueOf(bookInfo.getIndex()), String.valueOf(bookInfo.getBookQuantitySelected()), String.valueOf(true)));
            keyboardRow.add(decreaseQuantityButton);
        }
        InlineKeyboardButton quantityButton = new InlineKeyboardButton().setText(String.valueOf(bookInfo.getBookQuantitySelected()));
        quantityButton.setCallbackData("none");
        keyboardRow.add(quantityButton);

        if(bookInfo.getBookQuantitySelected() < bookInfo.getBookQuantityAvailable()){
            InlineKeyboardButton incrementQuantityButton = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.increment"));
            incrementQuantityButton.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndexQuantityEdit(ButtonAction.INCREASE_QUANTITY,bookInfo.getBookType(),bookInfo.getBookSubType()
                    , String.valueOf(bookInfo.getIndex()),String.valueOf(bookInfo.getBookQuantitySelected()),String.valueOf(true)));
            keyboardRow.add(incrementQuantityButton);
        }
        rowList.add(keyboardRow);
        rowList.add(addToCartButton(bookInfo));
        return rowList;
    }

    public List<InlineKeyboardButton> addToCartButton(BookCatalogViewInfo bookInfo){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonAddToCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.addtocart"));
        buttonAddToCart.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndexQuantityEdit(ButtonAction.ADD_BOOK_TO_CART,bookInfo.getBookType(),bookInfo.getBookSubType()
                , String.valueOf(bookInfo.getIndex()), String.valueOf(bookInfo.getBookQuantitySelected()), String.valueOf(false)));
        keyboardRow.add(buttonAddToCart);
        return keyboardRow;
    }

    public List<InlineKeyboardButton> mainMenuCartButtons(){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.mainmenu"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,false));

        InlineKeyboardButton buttonYourCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.basket.generate"));
        buttonYourCart.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,false));

        keyboardRow.add(buttonMainMenu);
        keyboardRow.add(buttonYourCart);
        return keyboardRow;
    }

    public List<InlineKeyboardButton> outOfStockButton(){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton outOfStock = new InlineKeyboardButton().setText(String.valueOf(localeMessageService.getMessage("view.bookcatalog.outofstock")));
        outOfStock.setCallbackData("none");
        keyboardRow.add(outOfStock);
        return keyboardRow;
    }

    private EditMessageText noBooksSendMessage(long chatId,int messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(noBooks());
        editMessageText.setReplyMarkup(noBooksKeyboard());
        return editMessageText;
    }

    private String noBooks() {
        return localeMessageService.getMessage("view.bookcatalog.nobooks");
    }



    public String generateText() {
        return localeMessageService.getMessage("view.bookcatalog.generate");
    }

    public String generateBookText(Book book,String convertedPrice) {
        return  localeMessageService.getMessage("view.bookcatalog.title") + ": " + book.getTitle() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.author") + ": " + book.getAuthor() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.price") + ": " + convertedPrice + "\n" +
                localeMessageService.getMessage("view.bookcatalog.publisher") + ": " + book.getPublisher() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.year") + ": " + book.getPublishingYear() +
                book.getDescription() + "\n" +
                book.getLinkToAudioFile();
    }



    private void addedToCartMessage(String callbackId) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeMessageService.getMessage("view.bookcatalog.addedtocart"));
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setShowAlert(true);
        try {
            bot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void lessBooksAvailableThanRequested(String callbackId) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeMessageService.getMessage("view.bookcatalog.lessbooksavailable"));
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setShowAlert(true);
        try {
            bot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void bookAlreadyInCart(String callbackId) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeMessageService.getMessage("view.bookcatalog.bookalreadyincart"));
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setShowAlert(true);
        try {
            bot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }
}
