package com.ishapirov.telegrambot.views;

import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.domain.book.Book;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.BookRetrieve;
import com.ishapirov.telegrambot.services.LocaleMessageService;
import com.ishapirov.telegrambot.services.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookCatalogView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    BookRetrieve bookRetrieve;
    @Autowired
    LocaleMessageService localeMessageService;

    @Override
    public SendMessage generateSendMessage(UserCallbackRequest userCallbackRequest){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard(userCallbackRequest));

//        try {
//            sendBookPhotos();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return sendMessage;
    }


    public void sendBookPhotos() throws IOException {

        List<Book> bookList = bookRetrieve.getAllBooks();
        for(Book book:bookList){
            SendPhoto sendPhoto = new SendPhoto();
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(book.getPicture()));
            sendPhoto.setPhoto(String.valueOf(img));
        }

    }

    @Override
    public InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<Book> bookList = bookRetrieve.getAllBooks();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for(Book book:bookList){
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton().setText(book.getName());
            button.setCallbackData(getTypeString() + "-"+book.getName());
            keyboardRow.add(button);
            rowList.add(keyboardRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return localeMessageService.getMessage("view.bookcatalog.generate");
    }


    @Override
    public View getNextView(String messageText,UserCallbackRequest userCallbackRequest) {
        if(messageText.equals("Подборка книг для детей"))
            return viewService.getMainMenuView();
        else if(messageText.equals("Книги для мам"))
            return viewService.getMainMenuView();
        else if(messageText.equals("Каталог книг"))
            return viewService.getMainMenuView();
        else if(messageText.equals("Меню") || messageText.equals("Назад"))
            return viewService.getMainMenuView();
        else throw new UnexpectedInputException("Unexpected input");
    }

    @Override
    public String getTypeString() {
        return "bookcatalog";
    }
}
