package com.ishapirov.telegrambot.services.view;

import com.ishapirov.telegrambot.views.*;
import com.ishapirov.telegrambot.views.basket.BasketView;
import com.ishapirov.telegrambot.views.basket.ViewAndEditBooksInBasketView;
import com.ishapirov.telegrambot.views.bookcatalog.CatalogMenuView;
import com.ishapirov.telegrambot.views.bookcatalog.BookCatalogView;
import com.ishapirov.telegrambot.views.bookcatalog.KidBooksSelectAgeView;
import com.ishapirov.telegrambot.views.bookcatalog.ParentingBooksSelectCategoryView;
import com.ishapirov.telegrambot.views.booksordered.BooksOrderedView;
import com.ishapirov.telegrambot.views.currency.CurrencySelectionView;
import com.ishapirov.telegrambot.views.mainmenu.MainMenuView;
import com.ishapirov.telegrambot.views.manager.ManagerContactInformationView;
import com.ishapirov.telegrambot.views.payment.PaymentView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ViewService {

    @Resource()
    Map<String, View> viewMap;

    public View getView(String string){
        return viewMap.get(string);
    }

    public MainMenuView getMainMenuView(){ return (MainMenuView) viewMap.get("mainmenu");}

    public BasketView getBasketView() { return (BasketView) viewMap.get("basket");}

    public BookCatalogView getBookCatalogView() { return (BookCatalogView) viewMap.get("bookcatalog");}

    public CatalogMenuView getCatalogMenuView() { return (CatalogMenuView) viewMap.get("catalogmenu");}

    public CurrencySelectionView getCurrencySelectionView() { return (CurrencySelectionView) viewMap.get("currencyselection");}

    public ManagerContactInformationView getManagerContactInformationView() { return (ManagerContactInformationView) viewMap.get("managercontactinformation");}

    public KidBooksSelectAgeView getKidBooksSelectAgeView() { return (KidBooksSelectAgeView) viewMap.get("kidselect");}

    public ParentingBooksSelectCategoryView getParentingBooksSelectCategoryView() { return (ParentingBooksSelectCategoryView) viewMap.get("parentingselect");}

    public PaymentView getPaymentView() { return (PaymentView) viewMap.get("payment");};

    public ViewAndEditBooksInBasketView getViewAndEditBooksInBasketView(){ return (ViewAndEditBooksInBasketView) viewMap.get("viewandedit");}

    public BooksOrderedView getBooksOrderedView(){
        return (BooksOrderedView) viewMap.get("booksordered");
    }
    public Map<String,View> getViewMap(){
        return viewMap;
    }
}
