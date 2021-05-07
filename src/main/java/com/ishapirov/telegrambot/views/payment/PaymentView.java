package com.ishapirov.telegrambot.views.payment;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import com.ishapirov.telegrambot.services.localemessage.LocaleMessageService;
import com.ishapirov.telegrambot.views.TelegramView;
import com.ishapirov.telegrambot.views.payment.dto.PaymentViewInfo;
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
public class PaymentView implements TelegramView {
    @Autowired
    LocaleMessageService localeMessageService;
    @Value("${payment.code}")
    private String PAYMENT_CODE;

    public static final String TYPE_STRING = "payment";

    @Override
    @Transactional
    public BotApiMethod<?> generateMessage(Object object, long chatId, int messageId, String callbackId, boolean editMessagePreferred) {
        PaymentViewInfo paymentViewInfo = (PaymentViewInfo) object;
        if(paymentViewInfo.isCartEmpty())
            return emptyCartMessage(chatId,paymentViewInfo.getLocaleString());

        List<LabeledPrice> prices = new ArrayList<>();
        LabeledPrice labeledPrice = new LabeledPrice("label",paymentViewInfo.getTotalCost().multiply(new BigDecimal(100)).intValue());
        prices.add(labeledPrice);

        SendInvoice sendInvoice = new SendInvoice((int) chatId,
                "Book Payment",
                "your payment for books",
                "test",
                PAYMENT_CODE,
                "start",
                paymentViewInfo.getCurrency(),
                prices
        );
        sendInvoice.setNeedEmail(true);
        sendInvoice.setNeedPhoneNumber(true);
        sendInvoice.setNeedShippingAddress(true);
        sendInvoice.setFlexible(true);
        return sendInvoice;
    }

    private BotApiMethod<?> emptyCartMessage(long chatId,String locale) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(emptyText(locale));
        sendMessage.setReplyMarkup(emptyKeyboard(locale));
        return sendMessage;
    }

    private ReplyKeyboard emptyKeyboard(String locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        InlineKeyboardButton buttonMainMenu = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.mainmenu.generate",locale));
        buttonMainMenu.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_MAIN_MENU,true));
        keyboardButtonsRow1.add(buttonMainMenu);

        InlineKeyboardButton buttonBack = new InlineKeyboardButton().setText(localeMessageService.getMessage("view.back",locale));
        buttonBack.setCallbackData(UserCallbackRequest.generateQueryMessage(ButtonAction.GO_TO_BASKET,true));
        keyboardButtonsRow1.add(buttonBack);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private String emptyText(String locale) {
        return localeMessageService.getMessage("view.viewandedit.nobooksincart",locale);
    }


    protected String generateText(String locale) {
        return localeMessageService.getMessage("view.payment.generate",locale);
    }

    @Override
    public String getTypeString() {
        return TYPE_STRING;
    }


}
