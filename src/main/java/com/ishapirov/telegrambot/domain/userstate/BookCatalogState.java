package com.ishapirov.telegrambot.domain.userstate;

import com.ishapirov.telegrambot.domain.Book;
import com.ishapirov.telegrambot.domain.UserSession;
import com.ishapirov.telegrambot.services.BookRetrieve;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookCatalogState extends UserState {

    public SendMessage generateSendMessage(){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(generateText());
        sendMessage.setReplyMarkup(generateKeyboard());

        try {
            sendBookPhotos();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sendMessage;
    }



    public void sendBookPhotos() throws IOException {

        List<Book> bookList = BookRetrieve.getAllBooks();
        for(Book book:bookList){
            SendPhoto sendPhoto = new SendPhoto();
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(book.getPicture()));
            sendPhoto.setPhoto(String.valueOf(img));
        }

    }

    @Override
    public ReplyKeyboardMarkup generateKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        List<Book> bookList = BookRetrieve.getAllBooks();
        KeyboardRow row1 = new KeyboardRow();
        for(Book book:bookList){
            row1.add(book.getName());
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String generateText() {
        return "Каталог";
    }


    @Override
    public void changeSessionStateBasedOnInput(String messageText,UserSession userSession) {
        if(messageText.equals("Подборка книг для детей"))
            userSession.setUserState(new MainMenuState(userSession));
        else if(messageText.equals("Книги для мам"))
            userSession.setUserState(new MainMenuState(userSession));
        else if(messageText.equals("Каталог книг"))
            userSession.setUserState(new MainMenuState(userSession));
        else if(messageText.equals("Меню") || messageText.equals("Назад"))
            userSession.setUserState(new MainMenuState(userSession));
        else userSession.setUserState(new UnknownInputState(userSession,this));
    }

    @Override
    public State getState() {
        return State.BOOK_CATALOG;
    }
}
