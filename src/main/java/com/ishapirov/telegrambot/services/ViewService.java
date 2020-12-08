package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.views.*;
import com.ishapirov.telegrambot.views.basket.BasketView;
import com.ishapirov.telegrambot.views.bookcatalog.CatalogMenuView;
import com.ishapirov.telegrambot.views.bookcatalog.BookCatalogView;
import com.ishapirov.telegrambot.views.bookcatalog.KidBooksSelectAgeView;
import com.ishapirov.telegrambot.views.bookcatalog.ParentingBooksSelectCategoryView;
import com.ishapirov.telegrambot.views.currency.CurrencySelectionView;
import com.ishapirov.telegrambot.views.mainmenu.MainMenuView;
import com.ishapirov.telegrambot.views.manager.ManagerContactInformationView;
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

    public Map<String,View> getViewMap(){
        return viewMap;
    }
}
