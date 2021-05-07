package com.ishapirov.telegrambot.controllers.gotoview;

import com.ishapirov.telegrambot.controllers.gotoview.dto.MainMenuControllerInfo;
import com.ishapirov.telegrambot.controllers.gotoview.dto.ShippingOrderControllerInfo;
import com.ishapirov.telegrambot.controllers.gotoview.dto.UserIDControllerInfo;
import com.ishapirov.telegrambot.domain.bookordered.BookOrdered;
import com.ishapirov.telegrambot.domain.shippingorder.ShippingOrder;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.services.orders.OrdersService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.views.booksordered.dto.BookOrderedInfo;
import com.ishapirov.telegrambot.views.booksordered.dto.ShippingOrderInfo;
import com.ishapirov.telegrambot.views.booksordered.dto.ShippingOrderViewInfo;
import com.ishapirov.telegrambot.views.dto.LocaleDTO;
import com.ishapirov.telegrambot.views.mainmenu.dto.MainMenuViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GoToController {
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    CurrencyConversionService currencyConversionService;
    
    public MainMenuViewDTO mainMenuData(MainMenuControllerInfo mainMenuControllerInfo) {
        return new MainMenuViewDTO(userProfileService.getCurrencyForUser(mainMenuControllerInfo.getUserId()).getCurrencyCode(), userProfileService.getLocaleForUser(mainMenuControllerInfo.getUserId()));
    }

    @Transactional
    public ShippingOrderViewInfo shippingOrderData(ShippingOrderControllerInfo shippingOrderControllerInfo){
        ShippingOrderViewInfo shippingOrderViewInfo = new ShippingOrderViewInfo();
        List<ShippingOrderInfo> shippingOrderInfoList = new ArrayList<>();
        for(ShippingOrder shippingOrder: ordersService.getUserShippingOrders(shippingOrderControllerInfo.getUserId())){
            ShippingOrderInfo shippingOrderInfo = new ShippingOrderInfo();
            List<BookOrderedInfo> bookOrderedInfoList = new ArrayList<>();
            for(BookOrdered bookOrdered: shippingOrder.getBooksOrdered()){
                BookOrderedInfo bookOrderedInfo = new BookOrderedInfo();
                bookOrderedInfo.setQuantity(bookOrdered.getQuantity());
                bookOrderedInfo.setTitle(bookOrdered.getBook().getTitle());
                bookOrderedInfo.setPrice(currencyConversionService.displayPrice(shippingOrder.getCurrency(),bookOrdered.getBook().getPrice()));
                bookOrderedInfoList.add(bookOrderedInfo);
            }
            shippingOrderInfo.setBooksOrdered(bookOrderedInfoList);
            shippingOrderInfo.setDateOrdered(shippingOrder.getDateOrdered());
            shippingOrderInfo.setTotalPrice(currencyConversionService.displayPriceAlreadyConverted(shippingOrder.getCurrency(),shippingOrder.getTotalCost()));
            shippingOrderInfoList.add(shippingOrderInfo);
        }
        shippingOrderViewInfo.setShippingOrders(shippingOrderInfoList);
        shippingOrderViewInfo.setLocale(userProfileService.getLocaleForUser(shippingOrderControllerInfo.getUserId()));
        return shippingOrderViewInfo;
    }

    public Object basketData(UserIDControllerInfo userIDControllerInfo){
        return new LocaleDTO(userProfileService.getLocaleForUser(userIDControllerInfo.getUserId()));
    }

    public Object kidsCategoriesData(UserIDControllerInfo userIDControllerInfo){
        return new LocaleDTO(userProfileService.getLocaleForUser(userIDControllerInfo.getUserId()));
    }

    public Object parentingCategoriesData(UserIDControllerInfo userIDControllerInfo){
        return new LocaleDTO(userProfileService.getLocaleForUser(userIDControllerInfo.getUserId()));
    }

    public Object catalogMenuData(UserIDControllerInfo userIDControllerInfo){
        return new LocaleDTO(userProfileService.getLocaleForUser(userIDControllerInfo.getUserId()));
    }
    
    public Object currencySelectData(UserIDControllerInfo userIDControllerInfo){
        return new LocaleDTO(userProfileService.getLocaleForUser(userIDControllerInfo.getUserId()));
    }

    public Object languageSelectData(UserIDControllerInfo userIDControllerInfo){
        return new LocaleDTO(userProfileService.getLocaleForUser(userIDControllerInfo.getUserId()));
    }

    public Object managerData(UserIDControllerInfo userIDControllerInfo){
        return new LocaleDTO(userProfileService.getLocaleForUser(userIDControllerInfo.getUserId()));
    }

}
