package com.ishapirov.telegrambot;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.inputprocessing.TelegramMessageHandler;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.testdomain.*;
import com.ishapirov.telegrambot.views.bookcatalog.BookCatalogView;
import com.ishapirov.telegrambot.webhookcontroller.WebHookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TelegramBotApplicationTests {

	@Autowired
	WebHookController controller;
	@Autowired
	ViewService viewService;
	@Autowired
	TelegramMessageHandler telegramMessageHandler;
	@Autowired
	CartService cartService;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void confirmNoRaceConditionDuringPurchase() {
		int user1Id = 1;
		int chat1Id = 1;

		int user2Id = 2;
		int chat2Id = 2;

		int messageId = 5;

		CustomUser user1 = new CustomUser(user1Id);
		CustomUser user2 = new CustomUser(user2Id);

		CustomMessage user1Message = new CustomMessage(chat1Id,messageId,user1,null);
		CustomMessage user2Message = new CustomMessage(chat2Id,messageId,user2,null);

		BookCatalogView bookCatalogView = viewService.getBookCatalogView();
		String callbackData = UserCallbackRequest.generateQueryMessage(ButtonAction.ADD_BOOK_TO_CART,false);

		CustomCallback user1OrderCallback = new CustomCallback(callbackData,user1,user1Message,"123");
		CustomCallback user2OrderCallback = new CustomCallback(callbackData,user2,user2Message,"246");

		CustomUpdate user1Order = new CustomUpdate(user1Message,user1OrderCallback,null);
		CustomUpdate user2Order = new CustomUpdate(user2Message,user2OrderCallback,null);

		telegramMessageHandler.handleUpdate(user1Order);
		telegramMessageHandler.handleUpdate(user2Order);

		assertThat(cartService.getCart(user1Id).getBooksInCart().size()).isEqualTo(1);
		assertThat(cartService.getCart(user2Id).getBooksInCart().size()).isEqualTo(1);

		String preCheckoutId1 = "user1";
		CustomPreCheckoutQuery preCheckoutQuery1 = new CustomPreCheckoutQuery(preCheckoutId1,user1);
		CustomUpdate user1UpdateOrder = new CustomUpdate(null,null,preCheckoutQuery1);
		telegramMessageHandler.handleUpdate(user1UpdateOrder);

		CustomShippingAddress shippingAddress = new CustomShippingAddress("Atlanta","1","30005","GA","Some Street","Some More Address");
		CustomOrderInfo orderInfo = new CustomOrderInfo(shippingAddress,"coolemail@email.com","678-123-1234");
		CustomSuccessfulPayment successfulPayment = new CustomSuccessfulPayment("123",1000,"USD","option1",orderInfo,"1");
		CustomMessage user1SuccessfulPaymentMessage = new CustomMessage(chat1Id,messageId+1,user1,successfulPayment);
		CustomUpdate successfulPaymentUpdate = new CustomUpdate(user1SuccessfulPaymentMessage,null,null);
		telegramMessageHandler.handleUpdate(successfulPaymentUpdate);

		String preCheckoutId2 = "user2";
		CustomPreCheckoutQuery preCheckoutQuery2 = new CustomPreCheckoutQuery(preCheckoutId2,user2);
		CustomUpdate user2UpdateOrder = new CustomUpdate(null,null,preCheckoutQuery2);
		telegramMessageHandler.handleUpdate(user2UpdateOrder);


		cartService.removeAllBooksFromCart(cartService.getCart(user1Id));
		cartService.removeAllBooksFromCart(cartService.getCart(user2Id));

		assertThat(cartService.getCart(user1Id).getBooksInCart().size()).isEqualTo(0);
		assertThat(cartService.getCart(user2Id).getBooksInCart().size()).isEqualTo(0);
	}


}
