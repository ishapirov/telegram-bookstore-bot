package com.ishapirov.telegrambot.domain.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name="book_type",
        discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String bookISBN;
    private String title;
    private String author;
    private BigDecimal price;
    private String publisher;
    private Date publishingYear;
    private String description;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    private String linkToAudioFile;


}
