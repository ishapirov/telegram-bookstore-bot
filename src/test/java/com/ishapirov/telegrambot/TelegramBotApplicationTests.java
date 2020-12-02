package com.ishapirov.telegrambot;

import com.ishapirov.telegrambot.controller.WebHookController;
import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.testdomain.CustomCallback;
import com.ishapirov.telegrambot.testdomain.CustomMessage;
import com.ishapirov.telegrambot.testdomain.CustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TelegramBotApplicationTests {

	@Autowired
	WebHookController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void testUserCallbackRequest() {
		Message message = new CustomMessage(1);
		User user = new CustomUser(123);
		CallbackQuery callback = new CustomCallback("mainmenu-currency",user,message);
		UserCallbackRequest userCallbackRequest = new UserCallbackRequest(callback);
		assertThat(userCallbackRequest.getChatId() == 1);
		assertThat(userCallbackRequest.getUserId() == 123);
		assertThat(userCallbackRequest.getButtonClicked().equals("currency"));
		assertThat(userCallbackRequest.getViewInWhichButtonWasClicked().equals("mainmenu"));
	}

}
