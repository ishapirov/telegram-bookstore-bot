package com.ishapirov.telegrambot.views.bookcatalog.allbooks;

import com.ishapirov.telegrambot.bot.Bot;
import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.BookRetrieve;
import com.ishapirov.telegrambot.services.LocaleMessageService;
import com.ishapirov.telegrambot.services.UserProfileService;
import com.ishapirov.telegrambot.services.ViewService;
import com.ishapirov.telegrambot.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        Pageable pageable = PageRequest.of(userCallbackRequest.getPage(), 1);
        Book book = bookRetrieve.getAllBooks(pageable).getContent().get(0);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateBookText(book));
        sendMessage.setReplyMarkup(generateKeyboard(userCallbackRequest));
        sendMessage.setChatId(userCallbackRequest.getChatId());
        try {
            sendBookPhoto(userCallbackRequest,book);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sendMessage;
    }


    public void sendBookPhoto(UserCallbackRequest userCallbackRequest,Book book) throws IOException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(userCallbackRequest.getChatId());
        sendPhoto.setPhoto(book.getTitle(),new ByteArrayInputStream(book.getPicture()));
        try {
            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),backText()));
        InlineKeyboardButton buttonForward = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.forward"));
        buttonForward.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),forwardText()));

        keyboardRow1.add(buttonBack);
        keyboardRow1.add(buttonForward);

        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
        InlineKeyboardButton buttonAddToCart = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.addtocart"));
        buttonAddToCart.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),cartText()));
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.bookcatalog.mainmenu"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),mainmenuText()));

        keyboardRow2.add(buttonAddToCart);
        keyboardRow2.add(buttonMainMenu);

        rowList.add(keyboardRow1);
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
                localeMessageService.getMessage("view.bookcatalog.price") + ": " + book.getPrice() +
                localeMessageService.getMessage("view.bookcatalog.publisher") + ": " + book.getPublisher() + "\n" +
                localeMessageService.getMessage("view.bookcatalog.year") + ": " + book.getPublishingYear() +
                book.getDescription() + "\n" +
                book.getLinkToAudioFile();
    }


    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(forwardText())){
            userCallbackRequest.setPage(userCallbackRequest.getPage()+1);
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(backText())){
            userCallbackRequest.setPage(userCallbackRequest.getPage()-1);
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals(cartText())){
            //add book to cart
            return viewService.getBookCatalogView();
        }
        else if(messageText.equals("Меню") || messageText.equals("Назад"))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "bookcatalog";
    }
}
