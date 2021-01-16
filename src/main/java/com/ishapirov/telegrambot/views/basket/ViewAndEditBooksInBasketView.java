package com.ishapirov.telegrambot.views.basket;

import com.ishapirov.telegrambot.bot.Bot;
import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.book.BookInfo;
import com.ishapirov.telegrambot.domain.bookaddedtocart.BookAddedToCart;
import com.ishapirov.telegrambot.domain.cart.Cart;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ViewAndEditBooksInBasketView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;
    @Autowired
    CartService cartService;
    @Autowired
    AddRemoveBookToCartService addRemoveBookToCartService;
    @Autowired
    Bot bot;
    @Autowired
    CurrencyConversionService currencyConversionService;

    @Override
    public BotApiMethod<?> generateMessage(UserCallbackRequest userCallbackRequest){
        BookInfo bookInfo = cartService.getBookFromCart(userCallbackRequest);
        if(bookInfo == null)
            return emptyEditMessage(userCallbackRequest);
        userCallbackRequest.setBookInfo(bookInfo);
        if(userCallbackRequest.isEditMessageNeeded()){
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(generateBookText(userCallbackRequest));
            editMessageText.setMessageId(userCallbackRequest.getMessageId());
            editMessageText.setChatId(userCallbackRequest.getChatId());
            editMessageText.setReplyMarkup(generateKeyboard(userCallbackRequest));
            return editMessageText;
        } else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(generateBookText(userCallbackRequest));
            sendMessage.setChatId(userCallbackRequest.getChatId());
            sendMessage.setReplyMarkup(generateKeyboard(userCallbackRequest));
            return sendMessage;
        }

    }

    public SendMessage emptyEditMessage(UserCallbackRequest userCallbackRequest){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateEmptyText());
        sendMessage.setChatId(userCallbackRequest.getChatId());
        sendMessage.setReplyMarkup(generateEmptyKeyboard());
        return sendMessage;
    }

    private InlineKeyboardMarkup generateEmptyKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return"));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),backToCartText()));

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),menuText()));

        keyboardRow.add(buttonCart);
        keyboardRow.add(buttonMainMenu);

        rowList.add(keyboardRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private String generateEmptyText() {
        return localeMessageService.getMessage("view.viewandedit.nobooksincart");
    }


    @Override
    protected InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(forwardBackButtons(userCallbackRequest));
        rowList.add(removeBook(userCallbackRequest));
        rowList.add(cartButton());

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public List<InlineKeyboardButton> forwardBackButtons(UserCallbackRequest userCallbackRequest){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        if(userCallbackRequest.getIndex() > 0){
            InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
            buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(getTypeString(),backText(), String.valueOf(userCallbackRequest.getIndex()), "true"));
            keyboardRow.add(buttonBack);
        }
        if(userCallbackRequest.getBookInfo().isHasNext()){
            InlineKeyboardButton buttonForward = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.forward"));
            buttonForward.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(getTypeString(),forwardText(),String.valueOf(userCallbackRequest.getIndex()), "true"));
            keyboardRow.add(buttonForward);
        }
        return keyboardRow;
    }

    private List<InlineKeyboardButton> removeBook(UserCallbackRequest userCallbackRequest) {
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonRemove = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.remove"));
        buttonRemove.setCallbackData(UserCallbackRequest.generateQueryMessageIndexEdit(getTypeString(),removeText(),String.valueOf(userCallbackRequest.getIndex()), "false"));
        keyboardRow.add(buttonRemove);
        return keyboardRow;
    }


    public List<InlineKeyboardButton> cartButton(){
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton buttonCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.viewandedit.return"));
        buttonCart.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),backToCartText()));

        keyboardRow.add(buttonCart);
        return keyboardRow;
    }

    private void removedFromCartMessage(UserCallbackRequest userCallbackRequest) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(localeMessageService.getMessage("view.viewandedit.removedfromcart"));
        answerCallbackQuery.setCallbackQueryId(userCallbackRequest.getCallBackId());
        answerCallbackQuery.setShowAlert(true);
        try {
            bot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public View getNextView(String messageText, UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(menuText()))
            return viewService.getMainMenuView();
        else if(messageText.equals(removeText())){
            removeBookFromCart(userCallbackRequest);
            userCallbackRequest.setIndex(0);
            removedFromCartMessage(userCallbackRequest);
            return viewService.getViewAndEditBooksInBasketView();
        } else if(messageText.equals(forwardText())){
            userCallbackRequest.setIndex(userCallbackRequest.getIndex()+1);
            return viewService.getViewAndEditBooksInBasketView();
        } else if(messageText.equals(backText())){
            userCallbackRequest.setIndex(userCallbackRequest.getIndex()-1);
            return viewService.getViewAndEditBooksInBasketView();
        } else if(messageText.equals(backToCartText()))
            return viewService.getBasketView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    private void removeBookFromCart(UserCallbackRequest userCallbackRequest) {
        Cart cart = cartService.getCart(userCallbackRequest.getUserId());
        List<BookAddedToCart> booksInCart = cart.getBooksInCart();
        BookAddedToCart bookAddedToCart = booksInCart.get(userCallbackRequest.getIndex());
        addRemoveBookToCartService.removeBookFromCart(bookAddedToCart);
    }

    public String generateBookText(UserCallbackRequest userCallbackRequest) {
        BookInfo bookInfo = userCallbackRequest.getBookInfo();
        Book book = bookInfo.getBook();
        CurrencyConversionRate currencyConversionRate = currencyConversionService.getConversionRate(userCallbackRequest.getUserId());
        BigDecimal convertedPrice = currencyConversionRate.getConvertedPrice(book.getPrice());
        return  localeMessageService.getMessage("view.bookcatalog.title") + ": " + book.getTitle() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.author") + ": " + book.getAuthor() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.price") + ": " + currencyConversionService.displayPrice(currencyConversionRate,convertedPrice) + "\n" +
                localeMessageService.getMessage("view.bookcatalog.publisher") + ": " + book.getPublisher() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.year") + ": " + book.getPublishingYear() + "\n" +
                book.getDescription() + "\n" +
                book.getLinkToAudioFile()  + "\n" +
                localeMessageService.getMessage("view.viewandedit.quantity") + ": " + bookInfo.getQuantity() + "\n" +
                localeMessageService.getMessage("view.viewandedit.totalcost") + ": " + currencyConversionService.displayPrice(currencyConversionRate,convertedPrice.multiply(new BigDecimal(bookInfo.getQuantity())));
    }
    public String forwardText(){
        return "forward";
    }

    public String backText(){
        return "back";
    }

    private String backToCartText() {
        return "backtocart";
    }

    private String removeText() {
        return "remove";
    }

    private String menuText() {
        return "mainmenu";
    }

    @Override
    protected String generateText() {
        return localeMessageService.getMessage("view.viewandedit.generate");
    }

    @Override
    public String getTypeString() {
        return "viewandedit";
    }
}
