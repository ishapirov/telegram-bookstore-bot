package com.ishapirov.telegrambot.controllers.gotoview;

import com.ishapirov.telegrambot.controllers.gotoview.dto.MainMenuControllerInfo;
import com.ishapirov.telegrambot.controllers.gotoview.dto.ShippingOrderControllerInfo;
import com.ishapirov.telegrambot.domain.bookordered.BookOrdered;
import com.ishapirov.telegrambot.domain.shippingorder.ShippingOrder;
import com.ishapirov.telegrambot.services.currency.CurrencyConversionService;
import com.ishapirov.telegrambot.services.orders.OrdersService;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.views.booksordered.dto.BookOrderedInfo;
import com.ishapirov.telegrambot.views.booksordered.dto.ShippingOrderInfo;
import com.ishapirov.telegrambot.views.booksordered.dto.ShippingOrderViewInfo;
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
        return new MainMenuViewDTO(userProfileService.getCurrencyForUser(mainMenuControllerInfo.getUserId()).getCurrencyCode());
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
        return shippingOrderViewInfo;
    }

    public Object basketData(){
        return null;
    }

    public Object kidsCategoriesData(){
        return null;
    }

    public Object parentingCategoriesData(){
        return null;
    }

    public Object catalogMenuData(){
        return null;
    }
    
    public Object currencySelectData(){
        return null;
    }

    public Object managerData(){
        return null;
    }




}
