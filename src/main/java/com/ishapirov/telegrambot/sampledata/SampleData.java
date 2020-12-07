package com.ishapirov.telegrambot.sampledata;

import com.ishapirov.telegrambot.domain.book.KidBook;
import com.ishapirov.telegrambot.domain.book.KidBookCategory;
import com.ishapirov.telegrambot.domain.book.ParentingBook;
import com.ishapirov.telegrambot.domain.book.ParentingBookCategory;
import com.ishapirov.telegrambot.services.BookRetrieve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class SampleData {
    @Autowired
    BookRetrieve bookRetrieve;


    @EventListener
    public void appReady(ApplicationReadyEvent event) throws IOException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ClassPathResource jsaCoverImgFile = new ClassPathResource("static/images/platorepublic.jpg");
        byte[] arrayData = new byte[(int) jsaCoverImgFile.contentLength()];
        jsaCoverImgFile.getInputStream().read(arrayData);
        ParentingBook parentingBook = new ParentingBook();
        parentingBook.setBookISBN("1420931695");
        parentingBook.setAuthor("Plato");
        parentingBook.setParentingBookCategory(ParentingBookCategory.INSPIRATION);
        parentingBook.setDescription("Plato's Republic is a classic which explores many philosophical themes such as justice and democracy");
        parentingBook.setLinkToAudioFile("https://www.youtube.com/watch?v=CqGsg01ycpk&ab_channel=WatchTheJRE");
        parentingBook.setPicture(arrayData);
        parentingBook.setTitle("The Republic");
        parentingBook.setPublisher("Plato Inc.");
        parentingBook.setPublishingYear(simpleDateFormat.parse("2014-02-14"));
        parentingBook.setPrice(BigDecimal.valueOf(9.95));

        bookRetrieve.saveParentingBook(parentingBook);

        jsaCoverImgFile = new ClassPathResource("static/images/hungrycaterpillar.jpg");
        arrayData = new byte[(int) jsaCoverImgFile.contentLength()];
        jsaCoverImgFile.getInputStream().read(arrayData);
        KidBook kidBook = new KidBook();
        kidBook.setBookISBN("0141380934");
        kidBook.setAuthor("Eric Carle");
        kidBook.setKidBookCategory(KidBookCategory.EIGHTEEN_MONTHS_TO_THREE_YEARS);
        kidBook.setDescription("The Very Hungry Caterpillar is a classic which explores many philosophical themes such as justice and democracy");
        kidBook.setLinkToAudioFile("https://www.youtube.com/watch?v=75NQK-Sm1YY&ab_channel=IlluminatedFilms");
        kidBook.setPicture(arrayData);
        kidBook.setTitle("The Very Hungry Caterpillar");
        kidBook.setPublisher("World Publishing Company");
        kidBook.setPublishingYear(simpleDateFormat.parse("1969-06-03"));
        kidBook.setPrice(BigDecimal.valueOf(14.11));

        bookRetrieve.saveKidBook(kidBook);
    }
}
