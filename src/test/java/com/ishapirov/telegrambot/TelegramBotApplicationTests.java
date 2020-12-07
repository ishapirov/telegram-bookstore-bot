package com.ishapirov.telegrambot;

import com.ishapirov.telegrambot.controller.WebHookController;
import com.ishapirov.telegrambot.domain.UserCallbackRequest;
import com.ishapirov.telegrambot.services.ViewService;
import com.ishapirov.telegrambot.testdomain.CustomCallback;
import com.ishapirov.telegrambot.testdomain.CustomMessage;
import com.ishapirov.telegrambot.testdomain.CustomUser;
import com.ishapirov.telegrambot.views.mainmenu.MainMenuView;
import com.ishapirov.telegrambot.views.View;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;

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

	@Test
	void testUserCallbackRequest() {
		Message message = new CustomMessage(1);
		User user = new CustomUser(123);
		MainMenuView mainmenuview = viewService.getMainMenuView();
		String data = UserCallbackRequest.generateQueryMessage(mainmenuview.getTypeString(),mainmenuview.currencyText());
		CallbackQuery callback = new CustomCallback(data,user,message);
		UserCallbackRequest userCallbackRequest = new UserCallbackRequest(callback);
		assertThat(userCallbackRequest.getChatId() == 1);
		assertThat(userCallbackRequest.getUserId() == 123);
		assertThat(userCallbackRequest.getButtonClicked().equals("currency"));
		assertThat(userCallbackRequest.getViewInWhichButtonWasClicked().equals("mainmenu"));
	}

	@Test
	void testGetNextView() {
		Message message = new CustomMessage(1);
		User user = new CustomUser(123);
		MainMenuView mainmenuview = viewService.getMainMenuView();
		String data = UserCallbackRequest.generateQueryMessage(mainmenuview.getTypeString(),mainmenuview.currencyText());
		CallbackQuery callback = new CustomCallback(data,user,message);
		UserCallbackRequest userCallbackRequest = new UserCallbackRequest(callback);
		assertThat(viewService.getView(userCallbackRequest.getViewInWhichButtonWasClicked()).equals(mainmenuview));
		View nextView = mainmenuview.getNextView(userCallbackRequest.getButtonClicked(),userCallbackRequest);
		assertThat(nextView.equals(viewService.getCurrencySelectionView()));
	}

	@Test
	void testViewService() {
		for(View view: new ArrayList<View>(viewService.getViewMap().values()))
			assertThat(view != null);
	}

//	@Test
//	void testUserCallbackRequestWithPage() {
//		Message message = new CustomMessage(1);
//		User user = new CustomUser(123);
//		BookCatalogView bookCatalogView = viewService.getBookCatalogView();
//		String data = UserCallbackRequest.generateQueryMessage(bookCatalogView.getTypeString(),bookCatalogView.currencyText())
//		CallbackQuery callback = new CustomCallback("mainmenu,currency,0",user,message);
//		UserCallbackRequest userCallbackRequest = new UserCallbackRequest(callback);
//		assertThat(userCallbackRequest.getChatId() == 1);
//		assertThat(userCallbackRequest.getUserId() == 123);
//		assertThat(userCallbackRequest.getButtonClicked().equals("currency"));
//		assertThat(userCallbackRequest.getViewInWhichButtonWasClicked().equals("mainmenu"));
//		assertThat(userCallbackRequest.getPage().equals(0));
//	}

}
