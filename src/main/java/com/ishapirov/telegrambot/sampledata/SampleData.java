package com.ishapirov.telegrambot.sampledata;

import com.ishapirov.telegrambot.domain.book.ParentingBook;
import com.ishapirov.telegrambot.domain.book.ParentingBookCategory;
import com.ishapirov.telegrambot.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class SampleData {
    @Autowired
    private BookRepository bookRepository;


//    @EventListener
    public void appReady(ApplicationReadyEvent event) throws IOException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ClassPathResource jsaCoverImgFile = new ClassPathResource("static/images/platorepublic.png");
        byte[] arrayData = new byte[(int) jsaCoverImgFile.contentLength()];
        jsaCoverImgFile.getInputStream().read(arrayData);
        ParentingBook testBook = new ParentingBook();
        testBook.setAuthor("Plato");
        testBook.setParentingBookCategory(ParentingBookCategory.INSPIRATION);
        testBook.setDescription("Plato's Republic is a classic which explores many philosophical themes such as justice and democracy");
        testBook.setLinkToAudioFile("https://www.youtube.com/watch?v=CqGsg01ycpk&ab_channel=WatchTheJRE");
        testBook.setPicture(arrayData);
        testBook.setName("The Republic");
        testBook.setPublisher("Plato Inc.");
        testBook.setPublishingYear(simpleDateFormat.parse("2014-02-14"));

        bookRepository.save(testBook);
    }
}
