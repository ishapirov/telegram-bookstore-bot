package com.ishapirov.telegrambot.commands.pay;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.commands.Command;
import com.ishapirov.telegrambot.controllers.payment.PaymentController;
import com.ishapirov.telegrambot.controllers.payment.PaymentControllerInfo;
import com.ishapirov.telegrambot.services.inputprocessing.UserCallbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayCommand implements Command {
    @Autowired
    PaymentController paymentController;

    private final ButtonAction actionWhichTriggersCommand = ButtonAction.PAY;

    @Override
    public Object execute(UserCallbackRequest userCallbackRequest) {
        PaymentControllerInfo paymentControllerInfo = new PaymentControllerInfo();
        paymentControllerInfo.setUserId(userCallbackRequest.getUserId());
        return paymentController.getPaymentInfo(paymentControllerInfo);
    }

    @Override
    public ButtonAction getButtonActionTrigger() {
        return actionWhichTriggersCommand;
    }
}
