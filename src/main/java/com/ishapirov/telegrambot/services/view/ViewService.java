package com.ishapirov.telegrambot.services.view;

import com.ishapirov.telegrambot.buttonactions.ButtonAction;
import com.ishapirov.telegrambot.exceptionhandling.exceptions.UnexpectedInputException;
import com.ishapirov.telegrambot.views.TelegramView;
import com.ishapirov.telegrambot.views.cart.CartView;
import com.ishapirov.telegrambot.views.cart.ViewRemoveBooksInCartView;
import com.ishapirov.telegrambot.views.bookcatalog.BookCatalogView;
import com.ishapirov.telegrambot.views.bookcatalog.CatalogMenuView;
import com.ishapirov.telegrambot.views.bookcatalog.KidBooksSelectAgeView;
import com.ishapirov.telegrambot.views.bookcatalog.ParentingBooksSelectCategoryView;
import com.ishapirov.telegrambot.views.booksordered.BooksOrderedView;
import com.ishapirov.telegrambot.views.currency.CurrencySelectionView;
import com.ishapirov.telegrambot.views.language.LanguageSelectionView;
import com.ishapirov.telegrambot.views.mainmenu.MainMenuView;
import com.ishapirov.telegrambot.views.manager.ManagerContactInformationView;
import com.ishapirov.telegrambot.views.payment.PaymentView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ViewService {

    @Resource()
    Map<String, TelegramView> telegramViewMap;

    public TelegramView getTelegramView(String string){
        return telegramViewMap.get(string);
    }

    public MainMenuView getMainMenuView(){ return (MainMenuView) telegramViewMap.get(MainMenuView.TYPE_STRING);}

    public CartView getBasketView() { return (CartView) telegramViewMap.get(CartView.TYPE_STRING);}

    public BookCatalogView getBookCatalogView() { return (BookCatalogView) telegramViewMap.get(BookCatalogView.TYPE_STRING);}

    public CatalogMenuView getCatalogMenuView() { return (CatalogMenuView) telegramViewMap.get(CatalogMenuView.TYPE_STRING);}

    public CurrencySelectionView getCurrencySelectionView() { return (CurrencySelectionView) telegramViewMap.get(CurrencySelectionView.TYPE_STRING);}

    public ManagerContactInformationView getManagerContactInformationView() { return (ManagerContactInformationView) telegramViewMap.get(ManagerContactInformationView.TYPE_STRING);}

    public KidBooksSelectAgeView getKidBooksSelectAgeView() { return (KidBooksSelectAgeView) telegramViewMap.get(KidBooksSelectAgeView.TYPE_STRING);}

    public ParentingBooksSelectCategoryView getParentingBooksSelectCategoryView() { return (ParentingBooksSelectCategoryView) telegramViewMap.get(ParentingBooksSelectCategoryView.TYPE_STRING);}

    public PaymentView getPaymentView() { return (PaymentView) telegramViewMap.get(PaymentView.TYPE_STRING);};

    public LanguageSelectionView getLanguageSelectView() { return (LanguageSelectionView) telegramViewMap.get(LanguageSelectionView.TYPE_STRING);}

    public ViewRemoveBooksInCartView getViewRemoveBooksInBasketView(){ return (ViewRemoveBooksInCartView) telegramViewMap.get(ViewRemoveBooksInCartView.TYPE_STRING);}

    public BooksOrderedView getBooksOrderedView(){
        return (BooksOrderedView) telegramViewMap.get(BooksOrderedView.TYPE_STRING);
    }
    public Map<String, TelegramView> getViewMap(){
        return telegramViewMap;
    }

    public TelegramView getNextView(ButtonAction action){
        if(action.equals(ButtonAction.GO_TO_MAIN_MENU))
            return getMainMenuView();
        else if(action.equals(ButtonAction.GO_TO_CATALOG_MENU))
            return getCatalogMenuView();
        else if(action.equals(ButtonAction.GO_TO_MANAGER_INFO))
            return getManagerContactInformationView();
        else if(action.equals(ButtonAction.GO_TO_BASKET))
            return getBasketView();
        else if(action.equals(ButtonAction.GO_TO_CURRENCY_SELECT))
            return getCurrencySelectionView();
        else if(action.equals(ButtonAction.GO_TO_ORDERS))
            return getBooksOrderedView();
        else if(action.equals(ButtonAction.GO_TO_VIEW_REMOVE))
            return getViewRemoveBooksInBasketView();
        else if(action.equals(ButtonAction.GO_TO_BOOK_CATALOG))
            return getBookCatalogView();
        else if(action.equals(ButtonAction.PAY))
            return getPaymentView();
        else if(action.equals(ButtonAction.BACK_A_BOOK_IN_CART))
            return getViewRemoveBooksInBasketView();
        else if(action.equals(ButtonAction.FORWARD_A_BOOK_IN_CART))
            return getViewRemoveBooksInBasketView();
        else if(action.equals(ButtonAction.REMOVE_BOOK_FROM_CART))
            return getViewRemoveBooksInBasketView();
        else if(action.equals(ButtonAction.BACK_A_BOOK_IN_CATALOG))
            return getBookCatalogView();
        else if(action.equals(ButtonAction.FORWARD_A_BOOK_IN_CATALOG))
            return getBookCatalogView();
        else if(action.equals(ButtonAction.INCREASE_QUANTITY))
            return getBookCatalogView();
        else if(action.equals(ButtonAction.DECREASE_QUANTITY))
            return getBookCatalogView();
        else if(action.equals(ButtonAction.ADD_BOOK_TO_CART))
            return getBookCatalogView();
        else if(action.equals(ButtonAction.GO_TO_KIDS_CATEGORIES))
            return getKidBooksSelectAgeView();
        else if(action.equals(ButtonAction.GO_TO_PARENTING_CATEGORIES))
            return getParentingBooksSelectCategoryView();
        else if(action.equals(ButtonAction.GO_TO_LANGUAGE_SELECT))
            return getLanguageSelectView();
        else if(action.equals(ButtonAction.SELECT_ENG) || action.equals(ButtonAction.SELECT_RUS))
            return getMainMenuView();
        else if(action.equals(ButtonAction.SELECT_USD) || action.equals(ButtonAction.SELECT_RUB) || action.equals(ButtonAction.SELECT_UZS))
            return getMainMenuView();
        throw new UnexpectedInputException("Unknown action");
    }



}
