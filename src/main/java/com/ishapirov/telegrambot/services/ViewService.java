package com.ishapirov.telegrambot.services;

import com.ishapirov.telegrambot.views.*;
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

}
