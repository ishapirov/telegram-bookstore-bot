package com.ishapirov.telegrambot.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class StateService {
    @Autowired
    BookRetrieve bookRetrieve;


}
