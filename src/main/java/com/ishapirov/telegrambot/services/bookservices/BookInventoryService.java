package com.ishapirov.telegrambot.services.bookservices;

import com.ishapirov.telegrambot.domain.book.KidBook;
import com.ishapirov.telegrambot.domain.book.ParentingBook;
import com.ishapirov.telegrambot.domain.bookinventory.BookInventory;
import com.ishapirov.telegrambot.repositories.BookInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookInventoryService {
    @Autowired
    private BookInventoryRepository bookInventoryRepository;
    @Autowired
    private BookRetrievalService bookRetrievalService;

    public int getQuantity(String bookISBN){
        Optional<BookInventory> bookInventory = bookInventoryRepository.findByBookISBN(bookISBN);
        if(bookInventory.isEmpty())
            return 0;
        return bookInventory.get().getQuantity();
    }

    public void addQuantity(String bookISBN,int quantity){
        Optional<BookInventory> getBookInventory = bookInventoryRepository.findByBookISBN(bookISBN);
        if(getBookInventory.isEmpty())
            return;
        BookInventory bookInventory = getBookInventory.get();
        Integer currentQuantity = bookInventory.getQuantity();
        bookInventory.setQuantity(currentQuantity + quantity);
        bookInventoryRepository.save(bookInventory);
    }

    public void subtractQuantity(String bookISBN,int quantity){
        Optional<BookInventory> getBookInventory = bookInventoryRepository.findByBookISBN(bookISBN);
        if(getBookInventory.isEmpty())
            return;
        BookInventory bookInventory = getBookInventory.get();
        Integer currentQuantity = bookInventory.getQuantity();
        bookInventory.setQuantity(currentQuantity - quantity);
        bookInventoryRepository.save(bookInventory);
    }

    public void saveParentingBook(ParentingBook parentingBook, int quantity){
        bookRetrievalService.saveParentingBook(parentingBook);
        BookInventory bookInventory = new BookInventory(parentingBook.getBookISBN(),parentingBook,quantity);
        bookInventoryRepository.save(bookInventory);
    }

    public void saveKidBook(KidBook kidBook, int quantity){
        bookRetrievalService.saveKidBook(kidBook);
        BookInventory bookInventory = new BookInventory(kidBook.getBookISBN(),kidBook,quantity);
        bookInventoryRepository.save(bookInventory);
    }


}
