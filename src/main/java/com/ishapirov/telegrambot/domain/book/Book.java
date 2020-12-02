package com.ishapirov.telegrambot.domain.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookNumber;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    private String name;
    private String author;
    private String publisher;
    private Date publishingYear;
    private String description;
    private String linkToAudioFile;
}
