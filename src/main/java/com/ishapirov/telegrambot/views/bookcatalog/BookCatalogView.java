package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.bot.Bot;
import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.book.BookInfo;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.BookAlreadyInCartException;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.LessBooksAvailableThanRequestedException;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.bookservices.BookInventoryService;
import com.ishapirov.telegrambot.services.bookservices.BookRetrievalService;
import com.ishapirov.telegrambot.services.cartservices.AddRemoveBookToCartService;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.View;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionRate;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookCatalogView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    BookRetrievalService bookRetrievalService;
    @Autowired
    LocaleMessageService localeMessageService;
    @Autowired
    Bot bot;
    @Autowired
    CartService cartService;
    @Autowired
    AddRemoveBookToCartService addRemoveBookToCartService;
    @Autowired
    BookInventoryService bookInventoryService;
    @Autowired
    CurrencyConversionService currencyConversionService;

    @Override
    public BotApiMethod<?> generateMessage(UserCallbackRequest userCallbackRequest){
        BookInfo bookInfo = bookRetrievalService.getBook(userCallbackRequest);
        if (bookInfo == null)
            return noBooksSendMessage(userCallbackRequest);
        userCallbackRequest.setBookInfo(bookInfo);
        if(!userCallbackRequest.isEditMessagePreferred()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateBookText(bookInfo.getBook(), userCallbackRequest));
            sendMessage.setChatId(userCallbackRequest.getChatId());
            sendMessage.setReplyMarkup(generateKeyboard(userCallbackRequest));
            sendBookPhoto(userCallbackRequest, bookInfo.getBook());
            return sendMessage;
        } else {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(generateBookText(bookInfo.getBook(),userCallbackRequest));
            editMessageText.setChatId(userCallbackRequest.getChatId());
            editMessageText.setMessageId(userCallbackRequest.getMessageId());
            editMessageText.setReplyMarkup(generateKeyboard(userCallbackRequest));
            return editMessageText;
        }
    }

    @Override
    public InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(forwardBackButtons(userCallbackRequest));
        rowList.addAll(bookQuantityAddToCartButtons(userCallbackRequest));
        rowList.add(mainMenuCartButtons());

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup noBooksKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.mainmenu"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),mainmenuText(),true));
        keyboardRow.add(buttonMainMenu);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),backToCategoryText(),true));
        keyboardRow.add(buttonBack);
        rowList.add(keyboardRow);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public void sendBookPhoto(UserCallbackRequest userCallbackRequest,Book book) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(userCallbackRequest.getChatId());
        sendPhoto.setPhoto(book.getTitle(),new ByteArrayInputStream(book.getPicture()));
        try {
            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public List<InlineKeyboardButton> forwardBackButtons(UserCallbackRequest userCallbackRequest){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        if(userCallbackRequest.getIndex() > 0){
            InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
            buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndex(getTypeString(),backText(),userCallbackRequest.getBookType(),userCallbackRequest.getBookSubType(), String.valueOf(userCallbackRequest.getIndex())));
            keyboardRow.add(buttonBack);
        }
        if(userCallbackRequest.getBookInfo().isHasNext()){
            InlineKeyboardButton buttonForward = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.forward"));
            buttonForward.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndex(getTypeString(),forwardText(),userCallbackRequest.getBookType(),userCallbackRequest.getBookSubType(), String.valueOf(userCallbackRequest.getIndex())));
            keyboardRow.add(buttonForward);
        }
        return keyboardRow;
    }

    public List<List<InlineKeyboardButton>> bookQuantityAddToCartButtons(UserCallbackRequest userCallbackRequest){

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        int quantity = bookInventoryService.getQuantity(userCallbackRequest.getBookInfo().getBook().getBookISBN());

        if(quantity == 0){
            rowList.add(outOfStockButton());
            return rowList;
        }

        if(userCallbackRequest.getQuantity() > 1) {
            InlineKeyboardButton decreaseQuantityButton = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.decrement"));
            decreaseQuantityButton.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndexQuantityEdit(getTypeString(), decrementText(), userCallbackRequest.getBookType(), userCallbackRequest.getBookSubType()
                    , String.valueOf(userCallbackRequest.getIndex()), String.valueOf(userCallbackRequest.getQuantity() - 1), String.valueOf(true)));
            keyboardRow.add(decreaseQuantityButton);
        }
        InlineKeyboardButton quantityButton = new InlineKeyboardButton().setText(String.valueOf(userCallbackRequest.getQuantity()));
        quantityButton.setCallbackData("none");
        keyboardRow.add(quantityButton);

        if(userCallbackRequest.getQuantity() < quantity){
            InlineKeyboardButton incrementQuantityButton = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.increment"));
            incrementQuantityButton.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndexQuantityEdit(getTypeString(),incrementText(),userCallbackRequest.getBookType(),userCallbackRequest.getBookSubType()
                    , String.valueOf(userCallbackRequest.getIndex()),String.valueOf(userCallbackRequest.getQuantity()+1),String.valueOf(true)));
            keyboardRow.add(incrementQuantityButton);
        }
        rowList.add(keyboardRow);
        rowList.add(addToCartButton(userCallbackRequest));
        return rowList;
    }

    public List<InlineKeyboardButton> addToCartButton(UserCallbackRequest userCallbackRequest){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonAddToCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.addtocart"));
        buttonAddToCart.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndexQuantityEdit(getTypeString(),cartText(),userCallbackRequest.getBookType(),userCallbackRequest.getBookSubType()
                , String.valueOf(userCallbackRequest.getIndex()), String.valueOf(userCallbackRequest.getQuantity()), String.valueOf(userCallbackRequest.isEditMessagePreferred())));
        keyboardRow.add(buttonAddToCart);
        return keyboardRow;
    }

    public List<InlineKeyboardButton> mainMenuCartButtons(){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.mainmenu"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),mainmenuText(),false));

        InlineKeyboardButton buttonYourCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.basket.generate"));
        buttonYourCart.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),yourCartText(),false));

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

    private EditMessageText noBooksSendMessage(UserCallbackRequest userCallbackRequest) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(userCallbackRequest.getChatId());
        editMessageText.setMessageId(userCallbackRequest.getMessageId());
        editMessageText.setText(noBooks());
        editMessageText.setReplyMarkup(noBooksKeyboard());
        return editMessageText;
    }

    private String noBooks() {
        return localeMessageService.getMessage("view.bookcatalog.nobooks");
    }

    public String decrementText(){
        return "decrement";
    }

    public String incrementText(){
        return "increment";
    }

    public String forwardText(){
        return "forward";
    }

    public String backText(){
        return "back";
    }

    public String backToCategoryText() { return "backcategory";} //BACK BASE ON CATEGORY

    public String cartText(){
        return "addtocart";
    }

    public String yourCartText(){
        return "gotocart";
    }

    public String mainmenuText(){
        return "mainmenu";
    }

    @Override
    public String generateText() {
        return localeMessageService.getMessage("view.bookcatalog.generate");
    }

    public String generateBookText(Book book,UserCallbackRequest userCallbackRequest) {
        CurrencyConversionRate currencyConversionRate = currencyConversionService.getConversionRate(userCallbackRequest.getUserId());
        BigDecimal convertedPrice = currencyConversionRate.getConvertedPrice(book.getPrice());
        return  localeMessageService.getMessage("view.bookcatalog.title") + ": " + book.getTitle() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.author") + ": " + book.getAuthor() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.price") + ": " + currencyConversionService.displayPrice(currencyConversionRate,convertedPrice) + "\n" +
                localeMessageService.getMessage("view.bookcatalog.publisher") + ": " + book.getPublisher() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.year") + ": " + book.getPublishingYear() +
                book.getDescription() + "\n" +
                book.getLinkToAudioFile();
    }


    @Override
    public View getNextView(UserCallbackRequest userCallbackRequest) {
        String messageText = userCallbackRequest.getButtonClicked();
        if(messageText.equals(forwardText())){
            userCallbackRequest.setIndex(userCallbackRequest.getIndex()+1);
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(backText())){
            userCallbackRequest.setIndex(userCallbackRequest.getIndex()-1);
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(cartText())){
            Book book = bookRetrievalService.getBook(userCallbackRequest).getBook();
            Cart cart = cartService.getCart(userCallbackRequest.getUserId());
            try {
                addRemoveBookToCartService.addBookToCart(book, cart, userCallbackRequest.getQuantity());
                addedToCartMessage(userCallbackRequest);
            } catch(LessBooksAvailableThanRequestedException e){
                lessBooksAvailableThanRequested(userCallbackRequest);
            } catch(BookAlreadyInCartException e){
                bookAlreadyInCart(userCallbackRequest);
            }
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(incrementText()) || messageText.equals(decrementText()))
            return viewService.getBookCatalogView();
        else if(messageText.equals(mainmenuText()))
            return viewService.getMainMenuView();
        else if(messageText.equals(yourCartText()))
            return viewService.getBasketView();
        else if(messageText.equals(backToCategoryText()))
            return viewService.getCatalogMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    private void addedToCartMessage(UserCallbackRequest userCallbackRequest) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeMessageService.getMessage("view.bookcatalog.addedtocart"));
        answerCallbackQuery.setCallbackQueryId(userCallbackRequest.getCallBackId());
        answerCallbackQuery.setShowAlert(true);
        try {
            bot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void lessBooksAvailableThanRequested(UserCallbackRequest userCallbackRequest) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeMessageService.getMessage("view.bookcatalog.lessbooksavailable"));
        answerCallbackQuery.setCallbackQueryId(userCallbackRequest.getCallBackId());
        answerCallbackQuery.setShowAlert(true);
        try {
            bot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void bookAlreadyInCart(UserCallbackRequest userCallbackRequest) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeMessageService.getMessage("view.bookcatalog.bookalreadyincart"));
        answerCallbackQuery.setCallbackQueryId(userCallbackRequest.getCallBackId());
        answerCallbackQuery.setShowAlert(true);
        try {
            bot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTypeString() {
        return "bookcatalog";
    }
}
