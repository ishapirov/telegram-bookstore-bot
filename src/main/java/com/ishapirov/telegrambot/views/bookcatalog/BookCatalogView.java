package com.ishapirov.telegrambot.views.bookcatalog;

import com.ishapirov.telegrambot.bot.Bot;
import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.domain.book.BookInfo;
import com.ishapirov.telegrambot.domain.user.UserProfile;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.BookRetrieve;
import com.ishapirov.telegrambot.services.LocaleMessageService;
import com.ishapirov.telegrambot.services.UserProfileService;
import com.ishapirov.telegrambot.services.ViewService;
import com.ishapirov.telegrambot.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookCatalogView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    BookRetrieve bookRetrieve;
    @Autowired
    LocaleMessageService localeMessageService;
    @Autowired
    Bot bot;

    @Override
    public SendMessage generateSendMessage(UserCallbackRequest userCallbackRequest){
        BookInfo bookInfo = bookRetrieve.getBook(userCallbackRequest);
        if(bookInfo == null)
            return noBooksSendMessage(userCallbackRequest);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateBookText(bookInfo.getBook()));
        sendMessage.setReplyMarkup(generateKeyboardWithForwardBackButtons(userCallbackRequest,bookInfo));
        sendMessage.setChatId(userCallbackRequest.getChatId());
        sendBookPhoto(userCallbackRequest, bookInfo.getBook());

        return sendMessage;
    }

    private SendMessage noBooksSendMessage(UserCallbackRequest userCallbackRequest) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(noBooks());
        sendMessage.setReplyMarkup(noBooksKeyboard(userCallbackRequest));
        sendMessage.setChatId(userCallbackRequest.getChatId());
        return sendMessage;
    }

    private String noBooks() {
        return localeMessageService.getMessage("view.bookcatalog.nobooks");
    }

    public InlineKeyboardMarkup noBooksKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();

        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.mainmenu"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),mainmenuText()));
        keyboardRow1.add(buttonMainMenu);
        rowList.add(keyboardRow1);
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

    public InlineKeyboardMarkup generateKeyboardWithForwardBackButtons(UserCallbackRequest userCallbackRequest, BookInfo bookInfo){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();

        if(userCallbackRequest.getIndex() > 0){
            InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
            buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndex(getTypeString(),backText(),userCallbackRequest.getBookType(),userCallbackRequest.getBookSubType(), String.valueOf(userCallbackRequest.getIndex())));
            keyboardRow1.add(buttonBack);
        }
        if(bookInfo.isHasNext()){
            InlineKeyboardButton buttonForward = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.forward"));
            buttonForward.setCallbackData(UserCallbackRequest.generateQueryMessageWithFilterIndex(getTypeString(),forwardText(),userCallbackRequest.getBookType(),userCallbackRequest.getBookSubType(), String.valueOf(userCallbackRequest.getIndex())));
            keyboardRow1.add(buttonForward);
        }
        rowList.add(keyboardRow1);
        rowList.addAll(generateKeyboard(userCallbackRequest).getKeyboard());
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    @Override
    public InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
        InlineKeyboardButton buttonAddToCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.addtocart"));
        buttonAddToCart.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),cartText()));
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.mainmenu"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),mainmenuText()));

        keyboardRow2.add(buttonAddToCart);
        keyboardRow2.add(buttonMainMenu);

        rowList.add(keyboardRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public String forwardText(){
        return "forward";
    }

    public String backText(){
        return "back";
    }

    public String cartText(){
        return "addtocart";
    }

    public String mainmenuText(){
        return "mainmenu";
    }

    @Override
    public String generateText() {
        return localeMessageService.getMessage("view.bookcatalog.generate");
    }

    public String generateBookText(Book book) {
        return  localeMessageService.getMessage("view.bookcatalog.title") + ": " + book.getTitle() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.author") + ": " + book.getAuthor() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.price") + ": " + book.getPrice() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.publisher") + ": " + book.getPublisher() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.year") + ": " + book.getPublishingYear() +
                book.getDescription() + "\n" +
                book.getLinkToAudioFile();

    }


    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(forwardText())){
            userCallbackRequest.setIndex(userCallbackRequest.getIndex()+1);
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(backText())){
            userCallbackRequest.setIndex(userCallbackRequest.getIndex()-1);
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(cartText())){
            UserProfile userProfile = userProfileService.getUserProfile(userCallbackRequest.getUserId());
            userProfile.getBooksInCart().add(bookRetrieve.getBook(userCallbackRequest).getBook());
            userProfileService.saveProfile(userProfile);
            addedToCartMessage(userCallbackRequest);
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(mainmenuText()))
            return viewService.getMainMenuView();
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

    @Override
    public String getTypeString() {
        return "bookcatalog";
    }
}
