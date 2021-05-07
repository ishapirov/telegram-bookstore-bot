package com.ishapirov.telegrambot.controllers.language;

import com.ishapirov.telegrambot.controllers.language.dto.UpdatedLanguage;
import com.ishapirov.telegrambot.services.userprofile.UserProfileService;
import com.ishapirov.telegrambot.views.mainmenu.dto.MainMenuViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LanguageSelectionController {
    @Autowired
    UserProfileService userProfileService;

    public MainMenuViewDTO updateLanguage(UpdatedLanguage updatedLanguage) {
        userProfileService.setLanguageForUser(updatedLanguage.getUserId(), updatedLanguage.getLocaleString());
        return new MainMenuViewDTO(userProfileService.getCurrencyForUser(updatedLanguage.getUserId()).getCurrencyCode(), updatedLanguage.getLocaleString());
    }
}
