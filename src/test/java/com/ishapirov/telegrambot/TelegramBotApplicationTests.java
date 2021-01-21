package com.ishapirov.telegrambot;

import com.ishapirov.telegrambot.controller.WebHookController;
import com.ishapirov.telegrambot.services.view.ViewService;
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

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
