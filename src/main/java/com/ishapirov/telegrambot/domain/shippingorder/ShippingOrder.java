package com.ishapirov.telegrambot.domain.shippingorder;

import com.ishapirov.telegrambot.domain.bookordered.BookOrdered;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.payments.OrderInfo;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ShippingOrder {
    @Id
    private String providerPaymentChargeId;

    private int userId;

    @OneToMany(mappedBy = "shippingOrder")
    private List<BookOrdered> booksOrdered = new ArrayList<>();;

    private BigDecimal totalCost;
    private String currency;

    private Date dateOrdered;
    private String shippingOptionSelected;
    private String city;
    private String countryCode;
    private String postCode;
    private String state;
    private String streetLine1;
    private String streetLine2;
    private String email;
    private String phoneNumber;
    private String telegramPaymentChargeId;


    public ShippingOrder(Message message){
        SuccessfulPayment successfulPayment = message.getSuccessfulPayment();
        this.providerPaymentChargeId = successfulPayment.getProviderPaymentChargeId();

        this.userId = message.getFrom().getId();
        this.totalCost = new BigDecimal(successfulPayment.getTotalAmount()).divide(new BigDecimal(100));
        this.currency = successfulPayment.getCurrency();
        this.dateOrdered = new Date();
        this.shippingOptionSelected = successfulPayment.getShippingOptionId();

        OrderInfo orderInfo = successfulPayment.getOrderInfo();
        this.city = orderInfo.getShippingAddress().getCity();
        this.countryCode = orderInfo.getShippingAddress().getCountryCode();
        this.postCode = orderInfo.getShippingAddress().getPostCode();
        this.state = orderInfo.getShippingAddress().getState();
        this.streetLine1 = orderInfo.getShippingAddress().getStreetLine1();
        this.streetLine2 = orderInfo.getShippingAddress().getStreetLine2();
        this.email = orderInfo.getEmail();
        this.phoneNumber = orderInfo.getPhoneNumber();

        this.telegramPaymentChargeId = successfulPayment.getTelegramPaymentChargeId();
    }

}
