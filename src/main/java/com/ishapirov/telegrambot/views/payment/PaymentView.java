package com.ishapirov.telegrambot.views.payment;

import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.services.cartservices.CartService;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionRate;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.services.view.ViewService;
import com.ishapirov.telegrambot.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentView extends View {
    @Autowired
    ViewService viewService;
    @Autowired
    LocaleMessageService localeMessageService;
    @Value("${payment.code}")
    private String PAYMENT_CODE;
    @Autowired
    CurrencyConversionService currencyConversionService;
    @Autowired
    CartService cartService;

    @Override
    @Transactional
    public BotApiMethod<?> generateMessage(UserCallbackRequest userCallbackRequest){
        if(cartService.getCart(userCallbackRequest.getUserId()).getBooksInCart().size() == 0)
            return emptyCartMessage(userCallbackRequest);
        CurrencyConversionRate currency = currencyConversionService.getConversionRate(userCallbackRequest.getUserId());
        BigDecimal cartPrice = cartService.getTotalCartCost(userCallbackRequest,currency);
        List<LabeledPrice> prices = new ArrayList<>();
        LabeledPrice labeledPrice = new LabeledPrice("label",cartPrice.multiply(new BigDecimal(100)).intValue());
        prices.add(labeledPrice);

        SendInvoice sendInvoice = new SendInvoice((int) userCallbackRequest.getChatId(),
                "Book Payment",
                "your payment for books",
                "test",
                PAYMENT_CODE,
                "start",
                currency.getCurrency().getCurrencyCode(),
                prices
                );
        sendInvoice.setNeedEmail(true);
        sendInvoice.setNeedPhoneNumber(true);
        sendInvoice.setNeedShippingAddress(true);
        sendInvoice.setFlexible(true);
        return sendInvoice;
    }

    private BotApiMethod<?> emptyCartMessage(UserCallbackRequest userCallbackRequest) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userCallbackRequest.getChatId());
        sendMessage.setText(emptyText());
        sendMessage.setReplyMarkup(emptyKeyboard());
        return sendMessage;
    }

    private ReplyKeyboard emptyKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate"));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), mainMenuText(),true));
        keyboardButtonsRow1.add(buttonMainMenu);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(), backText(),true));
        keyboardButtonsRow1.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private String emptyText() {
        return localeMessageService.getMessage("view.viewandedit.nobooksincart");
    }

    @Override
    protected InlineKeyboardMarkup generateKeyboard(UserCallbackRequest userCallbackRequest) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        InlineKeyboardButton buttonPay = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.payment.pay"));
        buttonPay.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),payText(),true));
        buttonPay.setPay(true);
        keyboardButtonsRow1.add(buttonPay);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back"));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(getTypeString(),backText(),true));
        keyboardButtonsRow1.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private String payText() {
        return "pay";
    }

    public String backText(){
        return "back";
    }

    private String mainMenuText() {
        return "mainmenu";
    }

    @Override
    protected String generateText() {
        return localeMessageService.getMessage("view.payment.generate");
    }

    @Override
    public String getTypeString() {
        return "payment";
    }

    @Override
    public View getNextView(String messageText, UserCallbackRequest userCallbackRequest) {
        if(messageText.equals(viewService.getMainMenuView().getTypeString()) || messageText.equals(mainMenuText()))
            return viewService.getMainMenuView();
        else if(messageText.equals(backText()))
            return viewService.getBasketView();
        throw new UnexpectedInputException("Unexpected input");
    }

}
