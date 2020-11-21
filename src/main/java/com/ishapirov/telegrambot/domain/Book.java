package com.ishapirov.telegrambot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
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
