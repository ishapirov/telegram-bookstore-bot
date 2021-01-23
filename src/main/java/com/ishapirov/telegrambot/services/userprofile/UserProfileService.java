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
        UserProfile userProfile = getUserProfile(userId);
        return userProfile.getCurrency();
    }

    private UserProfile createNewProfile(Integer userId) {
        UserProfile userProfile = new UserProfile(userId, Currency.getInstance("RUB"));
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    public boolean doesProfileExist(Integer userId){
        return userProfileRepository.existsById(userId);
    }

    public void saveProfile(UserProfile userProfile){
        userProfileRepository.save(userProfile);
    }
}
