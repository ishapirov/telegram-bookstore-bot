package com.ishapirov.telegrambot.services.userprofile;

import com.ishapirov.telegrambot.domain.user.UserProfile;
import com.ishapirov.telegrambot.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Optional;

@Service
public class UserProfileService {
    @Autowired
    UserProfileRepository userProfileRepository;

    public UserProfile getUserProfile(Integer userId){
        Optional<UserProfile> userProfile = userProfileRepository.findUserProfileByUserId(userId);
        if(userProfile.isEmpty())
            return createNewProfile(userId);
        else
            return userProfile.get();
    }

    public void setCurrencyForUser(Integer userId, Currency currency){
        UserProfile userProfile = getUserProfile(userId);
        userProfile.setCurrency(currency);
        userProfileRepository.save(userProfile);
    }

    public Currency getCurrencyForUser(Integer userId){
        return getUserProfile(userId).getCurrency();
    }

    private UserProfile createNewProfile(Integer userId) {
        UserProfile userProfile = new UserProfile(userId, Currency.getInstance("USD"), "en-US");
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    public String getLocaleForUser(Integer userId){
        return getUserProfile(userId).getLocaleString();
    }

    public void setLanguageForUser(Integer userId, String locale){
        UserProfile userProfile = getUserProfile(userId);
        userProfile.setLocaleString(locale);
        userProfileRepository.save(userProfile);
    }
}
