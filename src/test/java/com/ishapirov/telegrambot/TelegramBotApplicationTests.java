package com.ishapirov.telegrambot;

import com.ishapirov.telegrambot.controller.WebHookController;
import com.ishapirov.telegrambot.services.cartservices.AddRemoveBookToCartService;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.inputprocessing.UserInputProcessorService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.testdomain.CustomCallback;
import com.ishapirov.telegrambot.testdomain.CustomMessage;
import com.ishapirov.telegrambot.testdomain.CustomUpdate;
import com.ishapirov.telegrambot.testdomain.CustomUser;
import com.ishapirov.telegrambot.views.bookcatalog.BookCatalogView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TelegramBotApplicationTests {

	@Autowired
	WebHookController controller;
	@Autowired
	ViewService viewService;
	@Autowired
	UserInputProcessorService userInputProcessorService;
	@Autowired
	CartService cartService;
	@Autowired
	AddRemoveBookToCartService addRemoveBookToCartService;
	@Autowired
	UserProfileService userProfileService;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	@Transactional
	void confirmNoRaceConditionDuringPurchase() {
		int user1Id = 1;
		int chat1Id = 1;

		int user2Id = 2;
		int chat2Id = 2;

		int messageId = 5;

		CustomUser user1 = new CustomUser(user1Id);
		CustomUser user2 = new CustomUser(user2Id);

		CustomMessage user1Message = new CustomMessage(chat1Id,messageId);
		CustomMessage user2Message = new CustomMessage(chat2Id,messageId);

		BookCatalogView bookCatalogView = viewService.getBookCatalogView();
		String callbackData = UserCallbackRequest.generateQueryMessage(bookCatalogView.getTypeString(),bookCatalogView.cartText(),false);

		CustomCallback user1OrderCallback = new CustomCallback(callbackData,user1,user1Message,"123");
		CustomCallback user2OrderCallback = new CustomCallback(callbackData,user2,user2Message,"123");

		CustomUpdate user1Order = new CustomUpdate(user1Message,user1OrderCallback);
		CustomUpdate user2Order = new CustomUpdate(user2Message,user2OrderCallback);

		userInputProcessorService.handleUpdate(user1Order);
		userInputProcessorService.handleUpdate(user2Order);

//		assertThat(cartService.getCart(user1Id).getBooksInCart().size()).isEqualTo(1);
//		assertThat(cartService.getCart(user2Id).getBooksInCart().size()).isEqualTo(1);
//
//
//		addRemoveBookToCartService.removeAllBooksFromCart(cartService.getCart(user1Id));
//		addRemoveBookToCartService.removeAllBooksFromCart(cartService.getCart(user2Id));
//
//		assertThat(cartService.getCart(user1Id).getBooksInCart().size()).isEqualTo(0);
//		assertThat(cartService.getCart(user2Id).getBooksInCart().size()).isEqualTo(0);
	}


}
